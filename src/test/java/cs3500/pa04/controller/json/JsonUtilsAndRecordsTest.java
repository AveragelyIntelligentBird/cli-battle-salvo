package cs3500.pa04.controller.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.controller.json.records.EndGameRequestJson;
import cs3500.pa04.controller.json.records.FleetJson;
import cs3500.pa04.controller.json.records.JoinResponseJson;
import cs3500.pa04.controller.json.records.MessageJson;
import cs3500.pa04.controller.json.records.SetupRequestJson;
import cs3500.pa04.controller.json.records.VolleyJson;
import cs3500.pa04.model.GameResult;
import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.Ship;
import cs3500.pa04.model.game.ShipOrientation;
import cs3500.pa04.model.game.ShipType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests JsonUtils and all the record formats
 */
class JsonUtilsAndRecordsTest {
  static String NAME = "NAME";
  static GameType GAME_TYPE = GameType.SINGLE;
  static ObjectMapper mapper = new ObjectMapper();
  static Coord coord1;
  static Coord coord2;
  static Coord coord3;
  static Coord coord4;
  static ShipJsonAdapter submarine;
  static ShipJsonAdapter destroyer;

  /**
   * Sets up the coordinates for ship testing
   */
  @BeforeAll
  public static void setup() {
    coord1 = new Coord(0, 0);
    coord2 = new Coord(1, 0);
    coord3 = new Coord(2, 0);
    coord4 = new Coord(3, 0);

    submarine = new ShipJsonAdapter(
        new Ship(ShipType.SUBMARINE, ShipOrientation.VERTICAL,
            new ArrayList<>(List.of(coord1, coord2, coord3))));
    destroyer = new ShipJsonAdapter(
        new Ship(ShipType.DESTROYER, ShipOrientation.VERTICAL,
            new ArrayList<>(List.of(coord1, coord2, coord3, coord4))));
  }

  /**
   * Tests serialize record via the Volley record
   */
  @Test
  public void testVolley() {
    VolleyJson volley = new VolleyJson(List.of(coord1, coord2, coord3, coord4));
    JsonNode volleyNode = JsonUtils.serializeRecord(volley);

    try {
      assertEquals("{\"coordinates\":[{\"x\":0,\"y\":0},{\"x\":1,\"y\":0},"
              + "{\"x\":2,\"y\":0},{\"x\":3,\"y\":0}]}", mapper.writeValueAsString(volleyNode));
    } catch (JsonProcessingException e) {
      fail();
    }
  }

  /**
   * Tests serialize record via the Fleet record
   */
  @Test
  public void testFleet() {
    FleetJson fleet = new FleetJson(List.of(destroyer, submarine));
    JsonNode fleetNode = JsonUtils.serializeRecord(fleet);

    try {
      assertEquals("{\"fleet\":["
          + "{\"coord\":{\"x\":0,\"y\":0},\"length\":4,\"direction\":\"VERTICAL\"},"
          + "{\"coord\":{\"x\":0,\"y\":0},\"length\":3,\"direction\":\"VERTICAL\"}]}",
          mapper.writeValueAsString(fleetNode));
    } catch (JsonProcessingException e) {
      fail();
    }
  }

  /**
   * Tests serialize record via the JoinResponse record
   */
  @Test
  public void testJoinResponse() {
    JoinResponseJson joinResponse = new JoinResponseJson(NAME, GAME_TYPE);
    JsonNode joinNode = JsonUtils.serializeRecord(joinResponse);

    try {
      assertEquals("{\"name\":\"" + NAME + "\",\"game-type\":\"" + GAME_TYPE + "\"}",
          mapper.writeValueAsString(joinNode));
    } catch (JsonProcessingException e) {
      fail();
    }
  }

  /**
   * Tests serialize record via the Message record
   */
  @Test
  public void testMessage() {
    MessageJson response = new MessageJson(NAME, mapper.createObjectNode());
    JsonNode messageNode = JsonUtils.serializeRecord(response);

    try {
      assertEquals("{\"method-name\":\"" + NAME + "\",\"arguments\":{}}",
          mapper.writeValueAsString(messageNode));
    } catch (JsonProcessingException e) {
      fail();
    }
  }

  /**
   * Tests deserialization via SetupRequest record
   */
  @Test
  public void testSetupRequest() {
    String testJsonMsg = """
        {
          "width": 6,
          "height": 6,
          "fleet-spec": {
            "CARRIER": 2,
            "BATTLESHIP": 2,
            "DESTROYER": 1,
            "SUBMARINE": 1
          }
        }""";
    SetupRequestJson setupRequest = null;
    try {
      setupRequest = mapper.readValue(testJsonMsg, SetupRequestJson.class);
    } catch (JsonProcessingException e) {
      fail();
    }

    int expHeight = 6;
    assertEquals(expHeight, setupRequest.height());

    int expWidth = 6;
    assertEquals(expWidth, setupRequest.width());

    Map<ShipType, Integer> expSpecs = new HashMap<>(Map.of(
        ShipType.CARRIER, 2,
        ShipType.BATTLESHIP, 2,
        ShipType.DESTROYER, 1,
        ShipType.SUBMARINE, 1));
    Map<ShipType, Integer> prodSpecs = setupRequest.specs();
    for (ShipType thisShipType : ShipType.values()) {
      assertEquals(expSpecs.get(thisShipType), prodSpecs.get(thisShipType));
    }
  }

  /**
   * Tests deserialization via EndGame record
   */
  @Test
  public void testEndGameRequest() {
    String testJsonMsg = """
        {
            "result": "DRAW",
            "reason": "Game resulted in a draw."
        }""";
    EndGameRequestJson endGameRequest = null;
    try {
      endGameRequest = mapper.readValue(testJsonMsg, EndGameRequestJson.class);
    } catch (JsonProcessingException e) {
      fail();
    }

    GameResult expResult = GameResult.DRAW;
    assertEquals(expResult, endGameRequest.result());

    String expReason = "Game resulted in a draw.";
    assertEquals(expReason, endGameRequest.reason());
  }
}