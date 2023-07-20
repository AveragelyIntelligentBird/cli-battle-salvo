package cs3500.pa04.model.game;

/**
 * Enumerates all possible types of a battleship
 * The associated value is its length in cells
 */
public enum ShipType {
  CARRIER(6),
  BATTLESHIP(5),
  DESTROYER(4),
  SUBMARINE(3);

  public final int shipLength;

  /**
   * Private constructor, needed to associate shipLength with the stored values
   *
   * @param shipLength int length of a given ship type
   */
  private ShipType(int shipLength) {
    this.shipLength = shipLength;
  }
}
