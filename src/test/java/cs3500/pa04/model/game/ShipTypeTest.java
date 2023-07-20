package cs3500.pa04.model.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa04.model.game.ShipType;
import org.junit.jupiter.api.Test;

/**
 * Tests ShipType's associated value access
 */
class ShipTypeTest {
  /**
   * Tests ShipType's associated value access
   */
  @Test
  public void testShipType() {
    ShipType carrier = ShipType.CARRIER;
    assertEquals(6, carrier.shipLength);

    ShipType battleship = ShipType.BATTLESHIP;
    assertEquals(5, battleship.shipLength);

    ShipType destroyer = ShipType.DESTROYER;
    assertEquals(4, destroyer.shipLength);

    ShipType submarine = ShipType.SUBMARINE;
    assertEquals(3, submarine.shipLength);
  }
}