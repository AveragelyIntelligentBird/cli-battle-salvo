package cs3500.pa04.controller.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.Ship;
import cs3500.pa04.model.game.ShipOrientation;

/**
 * Represents an adaptor from Ship class (model representation) to a serializable ShipJson as
 * is required by the assignment
 */
public class ShipJsonAdapter {
  private final Coord coord;
  private final int length;
  private final ShipOrientation direction;

  /**
   * Utility constructor
   *
   * @param ship to be converted to JSON
   */
  public ShipJsonAdapter(Ship ship) {
    this.coord = ship.getStartingCoord();
    this.length = ship.getShipType().shipLength;
    this.direction = ship.getShipOrientation();
  }

  /**
   * JSON creator version of constructor to assist in serializing the ships as JSON
   *
   * @param coord starting coordinate
   * @param length length of the ship
   * @param direction direction in which the ship is oriented
   */
  @JsonCreator
  public ShipJsonAdapter(@JsonProperty("coord") Coord coord,
                         @JsonProperty("length") int length,
                         @JsonProperty("direction") ShipOrientation direction) {
    this.coord = coord;
    this.length = length;
    this.direction = direction;
  }

  /**
   * Getter for coord
   *
   * @return coord at which ship begins
   */
  public Coord getCoord() {
    return coord;
  }

  /**
   * Getter for ship length
   *
   * @return int ship length
   */
  public int getLength() {
    return length;
  }

  /**
   * Getter for direction
   *
   * @return ShipOrientation direction in which the ship stretches from staring coord
   */
  public ShipOrientation getDirection() {
    return direction;
  }
}
