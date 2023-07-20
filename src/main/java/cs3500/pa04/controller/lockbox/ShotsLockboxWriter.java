package cs3500.pa04.controller.lockbox;

import cs3500.pa04.model.game.Coord;
import java.util.ArrayList;

/**
 * Write-only interface processed by the Controller to deliver user shots to the player
 */
public interface ShotsLockboxWriter {
  /**
   * Setter for user shots currently stored in the lockbox
   *
   * @param shots List of Coord representing shots that the user took
   */
  void recieveUserShots(ArrayList<Coord> shots);
}
