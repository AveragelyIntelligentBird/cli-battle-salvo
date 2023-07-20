package cs3500.pa04.model.game;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.Ship;
import cs3500.pa04.model.game.ShipOrientation;
import cs3500.pa04.model.game.ShipType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests the Ship class
 */
class ShipTest {
  static Coord coord1;
  static Coord coord2;
  static Coord coord3;
  static Coord coord4;
  static Coord coord5;
  static Coord coord6;

  /**
   * Sets up the coordinates for ship testing
   */
  @BeforeAll
  public static void setup() {
    coord1 = new Coord(0, 0);
    coord2 = new Coord(1, 0);
    coord3 = new Coord(2, 0);
    coord4 = new Coord(3, 0);
    coord5 = new Coord(4, 0);
    coord6 = new Coord(5, 0);
  }

  /**
   * Tests all possible constructor combinations
   */
  @Test
  public void testShipConstructors() {
    Ship carrier = new Ship(ShipType.CARRIER, ShipOrientation.VERTICAL,
        new ArrayList<>(List.of(coord1, coord2, coord3, coord4, coord5, coord6)));
    assertEquals(ShipType.CARRIER, carrier.getShipType());
    assertEquals(ShipOrientation.VERTICAL, carrier.getShipOrientation());

    Ship battleship = new Ship(ShipType.BATTLESHIP, ShipOrientation.HORIZONTAL,
        new ArrayList<>(List.of(coord1, coord2, coord3, coord4, coord5)));
    assertEquals(ShipType.BATTLESHIP, battleship.getShipType());
    assertEquals(ShipOrientation.HORIZONTAL, battleship.getShipOrientation());

    Ship destroyer = new Ship(ShipType.DESTROYER, ShipOrientation.VERTICAL,
        new ArrayList<>(List.of(coord1, coord2, coord3, coord4)));
    assertEquals(ShipType.DESTROYER, destroyer.getShipType());

    Ship submarine = new Ship(ShipType.SUBMARINE, ShipOrientation.VERTICAL,
        new ArrayList<>(List.of(coord1, coord2, coord3)));
    assertEquals(ShipType.SUBMARINE, submarine.getShipType());
    assertArrayEquals(List.of(coord1, coord2, coord3).toArray(),
        submarine.getSurvShipCoords().toArray());

    assertThrows(IllegalArgumentException.class, () -> new Ship(ShipType.CARRIER,
        ShipOrientation.VERTICAL, new ArrayList<>(List.of(coord1, coord2, coord3))));
  }

  /**
   * Tests Ship methods
   */
  @Test
  public void testShipMethods() {
    Ship ship = new Ship(ShipType.SUBMARINE, ShipOrientation.VERTICAL,
        new ArrayList<>(List.of(coord1, coord2, coord3)));
    assertFalse(ship.isShipDestroyed());

    assertTrue(ship.isHitBy(coord1));
    assertTrue(ship.isHitBy(coord2));

    assertFalse(ship.isHitBy(coord4));
    assertFalse(ship.isShipDestroyed());

    assertTrue(ship.isHitBy(coord3));
    assertTrue(ship.isShipDestroyed());
  }

}