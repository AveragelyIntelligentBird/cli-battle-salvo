package cs3500.pa04.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.controller.json.GameType;
import cs3500.pa04.controller.json.JsonUtils;
import cs3500.pa04.controller.json.records.EndGameRequestJson;
import cs3500.pa04.controller.json.records.MessageJson;
import cs3500.pa04.controller.json.records.SetupRequestJson;
import cs3500.pa04.controller.json.records.VolleyJson;
import cs3500.pa04.controller.viewctrl.CliViewController;
import cs3500.pa04.model.GameResult;
import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.PlayerBoards;
import cs3500.pa04.model.game.ShipType;
import cs3500.pa04.model.player.RandomAiPlayer;
import cs3500.pa04.view.AppendableOutCh;
import cs3500.pa04.view.GameView;
import cs3500.pa04.view.ReadableInCh;
import cs3500.pa04.view.UiReader;
import cs3500.pa04.view.UiWriter;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests ProxyController
 */
class ProxyControllerTest {
  static final String PLAYER_NAME = "NAME";
  static final int SEED = 413;
  static final int BOARD_HEIGHT = 7;
  static final int BOARD_WIDTH = 6;
  private static Map<ShipType, Integer> shipSpecifications;
  static Coord coord1;
  static Coord coord2;
  static Coord coord3;
  static Coord coord4;
  static Coord coord5;
  static Coord coord6;
  static PlayerBoards testAiBoards;
  static RandomAiPlayer testAi;
  static CliViewController cliViewController;
  static Appendable appendable;
  private ByteArrayOutputStream testLog;
  private ProxyController controller;

  /**
   * Presets convenience variables for testing
   */
  @BeforeAll
  public static void setup() {
    shipSpecifications = new HashMap<>(Map.of(
        ShipType.CARRIER, 2,
        ShipType.BATTLESHIP, 2,
        ShipType.DESTROYER, 1,
        ShipType.SUBMARINE, 1));

    coord1 = new Coord(0, 0);
    coord2 = new Coord(1, 0);
    coord3 = new Coord(2, 0);
    coord4 = new Coord(3, 0);
    coord5 = new Coord(4, 0);
    coord6 = new Coord(5, 0);

    testAiBoards = new PlayerBoards(SEED);
    testAi = new RandomAiPlayer(PLAYER_NAME, testAiBoards, SEED);
  }


  /**
   * Reset the test log before each test is run.
   */
  @BeforeEach
  public void setupEach() {
    this.testLog = new ByteArrayOutputStream();
    assertEquals("", logToString());

    Readable readable = new BufferedReader(new StringReader(""));
    UiReader reader = new ReadableInCh(readable);
    appendable = new StringBuilder();
    UiWriter writer = new AppendableOutCh(appendable);

    cliViewController = new CliViewController(new GameView(reader, writer));
  }

  /**
   * Runs a dummy session with RandomAiPlayer
   */
  @Test
  public void testProxyController() {
    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(
        createJsonReq("join", null),
        createJsonReq("setup",
            new SetupRequestJson(BOARD_WIDTH, BOARD_HEIGHT, shipSpecifications)),
        createJsonReq("take-shots", null),
        createJsonReq("report-damage",
            new VolleyJson(List.of(coord1, coord2, coord3, coord4, coord5, coord6))),
        createJsonReq("successful-hits", new VolleyJson(List.of())),
        createJsonReq("end-game",
            new EndGameRequestJson(GameResult.WIN, "Test win"))));

    try {
      controller = new ProxyController(cliViewController, testAi, GameType.SINGLE, socket);
      controller.run();
    } catch (Exception e) {
      fail();
    }

    String expMocketInput =
        // Join
        "{\"method-name\":\"join\","
        + "\"arguments\":{\"name\":\"NAME\",\"game-type\":\"SINGLE\"}}"
        + System.lineSeparator()
        // Setup
        + "{\"method-name\":\"setup\",\"arguments\":{\"fleet\":["
        + "{\"coord\":{\"x\":5,\"y\":0},\"length\":6,\"direction\":\"VERTICAL\"},"
        + "{\"coord\":{\"x\":1,\"y\":0},\"length\":6,\"direction\":\"VERTICAL\"},"
        + "{\"coord\":{\"x\":0,\"y\":6},\"length\":5,\"direction\":\"HORIZONTAL\"},"
        + "{\"coord\":{\"x\":2,\"y\":1},\"length\":5,\"direction\":\"VERTICAL\"},"
        + "{\"coord\":{\"x\":0,\"y\":2},\"length\":4,\"direction\":\"VERTICAL\"},"
        + "{\"coord\":{\"x\":4,\"y\":0},\"length\":3,\"direction\":\"VERTICAL\"}]}}"
        + System.lineSeparator()
        // Take shots
        + "{\"method-name\":\"take-shots\",\"arguments\":{\"coordinates\":["
        + "{\"x\":5,\"y\":2},{\"x\":0,\"y\":6},{\"x\":5,\"y\":0},"
        + "{\"x\":1,\"y\":4},{\"x\":1,\"y\":3},{\"x\":2,\"y\":3}]}}"
        + System.lineSeparator()
        // Report damage
        + "{\"method-name\":\"report-damage\",\"arguments\":{\"coordinates\":["
        + "{\"x\":1,\"y\":0},{\"x\":4,\"y\":0},{\"x\":5,\"y\":0}]}}"
        + System.lineSeparator()
        // Successful hits
        + "{\"method-name\":\"successful-hits\",\"arguments\":{}}"
        + System.lineSeparator()
        // End game
        + "{\"method-name\":\"end-game\",\"arguments\":{}}"
        + System.lineSeparator();
    assertEquals(expMocketInput, logToString());

    String expViewOut = """
                
        ______       _   _   _      _____       _           \s
        | ___ \\     | | | | | |    /  ___|     | |          \s
        | |_/ / __ _| |_| |_| | ___\\ `--.  __ _| |_   _____ \s
        | ___ \\/ _` | __| __| |/ _ \\`--. \\/ _` | \\ \\ / / _ \\\s
        | |_/ / (_| | |_| |_| |  __/\\__/ / (_| | |\\ V / (_) |
        \\____/ \\__,_|\\__|\\__|_|\\___\\____/ \\__,_|_| \\_/ \\___/\s
                
        Hello! Welcome to the OOD BattleSalvo Game!
        NAME will be playing a remote session.
                
        Game resulted in a WIN.
        Reason: Test win""";
    assertEquals(expViewOut, appendable.toString());
  }

  /**
   * Runs a dummy session with RandomAiPlayer
   */
  @Test
  public void testProxyControllerFail() {
    // Create the client with all necessary messages
    Mocket socket = new Mocket(this.testLog, List.of(
        createJsonReq("unexpected-message", null)));

    try {
      controller = new ProxyController(cliViewController, testAi, GameType.SINGLE, socket);
    } catch (Exception e) {
      fail();
    }

    assertThrows(IllegalArgumentException.class, () -> controller.run());
  }

  /**
   * Converts the ByteArrayOutputStream log to a string in UTF_8 format
   *
   * @return String representing the current log buffer
   */
  private String logToString() {
    return testLog.toString(StandardCharsets.UTF_8);
  }

  /**
   * Create a MessageJson for some name and arguments
   *
   * @param messageName name of the type of message
   * @param messageObject object to embed in a message json
   * @return a MessageJson for the object converted to a String
   */
  private String createJsonReq(String messageName, Record messageObject) {
    MessageJson messageJson;
    if (messageObject == null) {
      messageJson = new MessageJson(messageName, new ObjectMapper().createObjectNode());
    } else {
      messageJson = new MessageJson(messageName, JsonUtils.serializeRecord(messageObject));
    }
    return JsonUtils.serializeRecord(messageJson).toString();
  }
}