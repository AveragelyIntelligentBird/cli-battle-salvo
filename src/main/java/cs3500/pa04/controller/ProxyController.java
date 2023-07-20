package cs3500.pa04.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.controller.json.GameType;
import cs3500.pa04.controller.json.JsonUtils;
import cs3500.pa04.controller.json.ShipJsonAdapter;
import cs3500.pa04.controller.json.records.EndGameRequestJson;
import cs3500.pa04.controller.json.records.FleetJson;
import cs3500.pa04.controller.json.records.JoinResponseJson;
import cs3500.pa04.controller.json.records.MessageJson;
import cs3500.pa04.controller.json.records.SetupRequestJson;
import cs3500.pa04.controller.json.records.VolleyJson;
import cs3500.pa04.controller.viewctrl.ViewController;
import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.Ship;
import cs3500.pa04.model.player.Player;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a proxy controller, which is a stand-in for a remote game server
 */
public class ProxyController implements GameController {
  private final ViewController viewCtrl;
  private final Player player;
  private final GameType gameType;
  private final InputStream streamFromServer;
  private final PrintStream streamToServer;
  private final ObjectMapper mapper = new ObjectMapper();
  private boolean gameIsOver;

  /**
   * Constructor for the ProxyController
   *
   * @param viewCtrl view controller that is going to display end-of-game message
   * @param server server socket over which we will be receiving game directing over JSON
   * @param player Ai player to be competing with the server
   */
  public ProxyController(ViewController viewCtrl, Player player, GameType gameType, Socket server)
      throws IOException {
    this.viewCtrl = viewCtrl;
    this.streamFromServer = server.getInputStream();
    this.streamToServer = new PrintStream(server.getOutputStream());

    this.player = player;
    this.gameType = gameType;
    this.gameIsOver = false;
  }

  /**
   * Main runner of the controller. Listens for messages from server and controls
   * the game accordingly
   *
   * @throws Exception if an error occurs in processing JSON, handled by driver
   */
  @Override
  public void run() throws Exception {
    viewCtrl.displayWelcomeMessage();
    viewCtrl.displayMsg("\n" + player.name() + " will be playing a remote session.\n");

    JsonParser parser = this.mapper.getFactory().createParser(this.streamFromServer);

    while (!gameIsOver) {
      MessageJson message = parser.readValueAs(MessageJson.class);
      delegateMessage(message);
    }
  }

  /**
   * Given a received JSON message, delegates its processing to one of the helpers
   *
   * @param message a JSON containing the "name" and "game-type" of the user
   * @throws Exception if an error occurred when displaying the end of game message
   *                   handled in the Driver
   */
  private void delegateMessage(MessageJson message) throws Exception {
    // store the name and arguments of the MessageJson
    String name = message.messageName();
    JsonNode arguments = message.arguments();

    // Delegate the arguments to the proper method to handle
    MessageJson response;
    switch (name) {
      case "join" -> response = handleJoin();
      case "setup" -> response = handleSetup(arguments);
      case "take-shots" -> response = handleTakeShots();
      case "report-damage" -> response = handleReportDamage(arguments);
      case "successful-hits" -> response = handleSuccessfulHits(arguments);
      case "end-game" -> response = handleEndGame(arguments);
      default -> throw new IllegalArgumentException("Unexpected JSON message format.");
    }

    // Print out the response to the server
    try {
      streamToServer.println(mapper.writeValueAsString(response));
    } catch (JsonProcessingException e) {
      System.out.println("Failed trying to write response in join");
    }
  }

  /**
   * Handles the Join JSON server request
   *
   * @return MessageJson response to the given request
   */
  public MessageJson handleJoin() {
    // Serialize the response into a JsonNode, and wrap it in a MessageJson
    JsonNode joinResponse = JsonUtils.serializeRecord(
        new JoinResponseJson(player.name(), this.gameType));

    // Produce JSON response
    return new MessageJson("join", joinResponse);
  }

  /**
   * Handles the Setup JSON server request
   *
   * @param arguments width, height, and fleet-specifications for the setup() method
   * @return MessageJson response to the given request
   */
  public MessageJson handleSetup(JsonNode arguments) {
    // convert the arguments JsonNode into setupArgs type, so we can extract values
    SetupRequestJson setupArgs = mapper.convertValue(arguments, SetupRequestJson.class);

    // Use setup() to create a list of ships that hold coordinates on player board
    List<Ship> shipList = player.setup(setupArgs.height(), setupArgs.width(), setupArgs.specs());

    // Convert the list of Ships to List of ShipAdapter
    ArrayList<ShipJsonAdapter> fleet = new ArrayList<>();
    for (Ship ship : shipList) {
      fleet.add(new ShipJsonAdapter(ship));
    }

    // Produce JSON response
    JsonNode setupResponse = JsonUtils.serializeRecord(
        new FleetJson(fleet));

    // Produce JSON response
    return new MessageJson("setup", setupResponse);
  }


  /**
   * Handles the TakeShots JSON server request
   *
   * @return MessageJson response to the given request
   */
  public MessageJson handleTakeShots() {
    // Obtain player shots
    List<Coord> userShots = player.takeShots();

    // Produce JSON response
    JsonNode takeShotsResponse = JsonUtils.serializeRecord(
        new VolleyJson(userShots));

    // Produce JSON response
    return new MessageJson("take-shots", takeShotsResponse);
  }

  /**
   * Handles the ReportDamage JSON server request
   *
   * @param arguments the opponent's shots on this player's board
   * @return MessageJson response to the given request
   */
  public MessageJson handleReportDamage(JsonNode arguments) {
    // Extract the shots VolleyJson from request arguments
    VolleyJson opponentShots = this.mapper.convertValue(arguments, VolleyJson.class);

    // Process received volley to return hit shots
    List<Coord> hitShots = this.player.reportDamage(opponentShots.volley());

    // Convert the VolleyJson to a JsonNode so that it can be wrapped into MessageJson
    JsonNode reportDamageResponse = JsonUtils.serializeRecord(
        new VolleyJson(hitShots));

    // Produce JSON response
    return new MessageJson("report-damage", reportDamageResponse);
  }

  /**
   * Handles the SuccessfulHits JSON server request
   *
   * @param arguments the list of shots that successfully hit the opponent's ships
   * @return MessageJson response to the given request
   */
  public MessageJson handleSuccessfulHits(JsonNode arguments) {
    // Extract the shots VolleyJson from request arguments
    VolleyJson hitShots = this.mapper.convertValue(arguments, VolleyJson.class);

    // Process received volley to update opponent's boards state
    this.player.successfulHits(hitShots.volley());

    // Construct acknowledgement message to be sent to the server
    return new MessageJson("successful-hits", mapper.createObjectNode());
  }

  /**
   * Handles the EndGame JSON server request
   *
   * @param arguments a game result and a reason for the game result (as string)
   * @return MessageJson response to the given request
   */
  public MessageJson handleEndGame(JsonNode arguments) throws Exception {
    // Extract arguments and convert to an EndGameJson
    EndGameRequestJson gameResult = this.mapper.convertValue(arguments, EndGameRequestJson.class);

    // Prints out an end-of-game message, reporting the outcome
    viewCtrl.displayMsg("\nGame resulted in a " + gameResult.result() + "."
        + "\nReason: " + gameResult.reason());

    // Mark end of game
    gameIsOver = true;

    // Construct acknowledgement message to be sent to the server
    return new MessageJson("end-game", mapper.createObjectNode());
  }

}
