package cs3500.pa04.model.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

/**
 * Tests Coord class
 */
class CoordTest {
  static final int xCoord = 413;
  static final int yCoord = 612;

  /**
   * Tests Coord's constructor, both its getters and equality method
   */
  @Test
  public void testCoord() {
    Coord coord = new Coord(xCoord, yCoord);
    assertEquals(xCoord, coord.getX());
    assertEquals(yCoord, coord.getY());

    Coord equalCoord = new Coord(xCoord, yCoord);
    assertEquals(coord, equalCoord);

    Coord notEqualCoord1 = new Coord(yCoord, xCoord);
    assertNotEquals(coord, notEqualCoord1);

    Coord notEqualCoord2 = new Coord(xCoord, xCoord);
    assertNotEquals(coord, notEqualCoord2);

    Coord notEqualCoord3 = new Coord(yCoord, yCoord);
    assertNotEquals(coord, notEqualCoord3);
  }

  /**
   * Tests built-in JSON creator
   */
  @Test
  public void testJsonConversion() {
    ObjectMapper mapper = new ObjectMapper();
    Coord testJsonCoord = new Coord(xCoord, yCoord);

    try {
      assertEquals("{\"x\":413,\"y\":612}", mapper.writeValueAsString(testJsonCoord));
    } catch (JsonProcessingException e) {
      fail();
    }
  }

}