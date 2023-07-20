package cs3500.pa04.model.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Structure, representing coordinates within a board
 */
public class Coord {
  private final int valX;
  private final int valY;

  /**
   * Constructor for Coord, instantiates a final value for both fields
   *
   * @param valX coordinate, int
   * @param valY coordinate, int
   */
  @JsonCreator
  public Coord(@JsonProperty("x") int valX,
               @JsonProperty("y") int valY) {
    this.valX = valX;
    this.valY = valY;
  }

  /**
   * Getter for x coordinate
   *
   * @return int value of a coordinate
   */
  public int getX() {
    return valX;
  }

  /**
   * Getter for y coordinate
   *
   * @return int value of a coordinate
   */
  public int getY() {
    return valY;
  }

  /**
   * Overrides the equals method for more robust operation of remove in ArrayLists
   *
   * @param obj to be compared with, expecting a Coord
   * @return true if coords are equal
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Coord that)) {
      return false;
    }

    return this.valX == that.valX && this.valY == that.valY;
  }
}
