package cs3500.pa04.model.game;

import java.util.ArrayList;

/**
 * Class representing a ship on a board with all the not destroyed coordinates
 */
public class Ship {
  private final ShipType shipType;
  private final ShipOrientation orientation;
  private final ArrayList<Coord> survShipCoords;

  /**
   * Constructor for the Ship class
   *
   * @param shipType is the type of the ship
   * @param survShipCoords is the list of the coordinates that have not yet been destroyed
   */
  public Ship(ShipType shipType, ShipOrientation orientation, ArrayList<Coord> survShipCoords) {
    this.shipType = shipType;
    this.orientation = orientation;

    if (survShipCoords.size() != shipType.shipLength) {
      throw new IllegalArgumentException();
    }
    this.survShipCoords = survShipCoords;
  }

  /**
   * Getter for ShipType
   *
   * @return ShipType of the current ship
   */
  public ShipType getShipType() {
    return this.shipType;
  }

  /**
   * Getter for orientation
   *
   * @return ShipOrientation of the current ship
   */
  public ShipOrientation getShipOrientation() {
    return this.orientation;
  }

  /**
   * Getter for survShipCoords
   *
   * @return ArrayList of Coord, representing list of coords of the ship not yet hit
   */
  public  ArrayList<Coord> getSurvShipCoords() {
    return this.survShipCoords;
  }

  /**
   * Processes a single shot to see whether it hits the ship
   * If so, the coordinate is removed from survShipCoords and true is returned
   *
   * @param shotCoord Coord of where the shot was shot
   * @return true if the ship was hit
   */
  public boolean isHitBy(Coord shotCoord) {
    for (Coord shipCoord : survShipCoords) {
      if (shipCoord.equals(shotCoord)) {
        survShipCoords.remove(shipCoord);
        return true;
      }
    }

    return false;
  }

  /**
   * Reports whether this ship has not destroyed cells
   *
   * @return true if the ship has been destroyed
   */
  public boolean isShipDestroyed() {
    return survShipCoords.isEmpty();
  }


  /**
   * @return the Coord that should represent the Starting Coord in a ShipJson
   */
  public Coord getStartingCoord() {
    // Return the first element of the arraylist
    return survShipCoords.get(0);
  }

}
