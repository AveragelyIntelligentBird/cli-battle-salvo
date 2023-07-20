package cs3500.pa04.controller.json;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.Ship;
import cs3500.pa04.model.game.ShipOrientation;
import cs3500.pa04.model.game.ShipType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests ShipJsonAdapter
 */
class ShipJsonAdapterTest {
  static Coord coord1;
  static Coord coord2;
  static Coord coord3;

  /**
   * Sets up the coordinates for ship testing
   */
  @BeforeAll
  public static void setup() {
    coord1 = new Coord(0, 0);
    coord2 = new Coord(1, 0);
    coord3 = new Coord(2, 0);
  }

  /**
   * Tests all possible constructor combinations
   */
  @Test
  public void testShipAdapter() {
    Ship submarine = new Ship(ShipType.SUBMARINE, ShipOrientation.VERTICAL,
        new ArrayList<>(List.of(coord1, coord2, coord3)));
    assertEquals(ShipType.SUBMARINE, submarine.getShipType());
    assertArrayEquals(List.of(coord1, coord2, coord3).toArray(),
        submarine.getSurvShipCoords().toArray());

    ObjectMapper mapper = new ObjectMapper();
    ShipJsonAdapter shipAdapter = new ShipJsonAdapter(submarine);

    try {
      assertEquals("{\"coord\":{\"x\":0,\"y\":0},\"length\":3,\"direction\":\"VERTICAL\"}",
          mapper.writeValueAsString(shipAdapter));
    } catch (JsonProcessingException e) {
      fail();
    }
  }

}