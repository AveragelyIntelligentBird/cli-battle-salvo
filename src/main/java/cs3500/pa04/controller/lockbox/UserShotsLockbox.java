package cs3500.pa04.controller.lockbox;

import cs3500.pa04.model.game.Coord;
import java.util.ArrayList;

/**
 * Represents a lockbox to transfer user input shots to the model without exposing Player directly
 */
public class UserShotsLockbox implements ShotsLockboxReader, ShotsLockboxWriter {
  private ArrayList<Coord> mostRecentShots;

  /**
   * Setter for user shots currently stored in the lockbox
   *
   * @param shots List of Coord representing shots that the user took
   */
  @Override
  public void recieveUserShots(ArrayList<Coord> shots) {
    this.mostRecentShots = shots;
  }

  /**
   * Getter for user shots currently stored in the lockbox
   * Is accessed in HumanPlayer to get user input
   *
   * @return List of Coord representing shots that the user took
   */
  @Override
  public ArrayList<Coord> reportUserShots() {
    if (this.mostRecentShots == null) {
      throw new IllegalStateException();
    }
    return this.mostRecentShots;
  }


}
