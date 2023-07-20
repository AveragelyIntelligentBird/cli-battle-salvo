package cs3500.pa04.controller.lockbox;

import cs3500.pa04.model.game.Coord;
import java.util.ArrayList;

/**
 * Read-only interface processed by the HumanPlayer to get user shots
 */
public interface ShotsLockboxReader {
  /**
   * Getter for user shots currently stored in the lockbox
   * Is accessed in HumanPlayer to get user input
   *
   * @return List of Coord representing shots that the user took
   */
  ArrayList<Coord> reportUserShots();
}
