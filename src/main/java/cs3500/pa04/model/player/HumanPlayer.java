package cs3500.pa04.model.player;

import cs3500.pa04.controller.lockbox.ShotsLockboxReader;
import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.PlayerBoardsInteractive;
import java.util.List;

/**
 * Represents a local human player interacting with the game via UI
 */
public class HumanPlayer extends AbstractPlayer {
  private final ShotsLockboxReader userShotsLockbox;

  /**
   * Constructor for AbstractLocalPlayer
   *
   * @param name String name of the player
   * @param boards PlayerBoardsInteractive player pov boards that are going to keep track of the
   *               state of the game
   * @param userShotsLockbox lockbox for getting user input from Controller
   */
  public HumanPlayer(String name, PlayerBoardsInteractive boards,
                     ShotsLockboxReader userShotsLockbox) {
    super(name, boards);
    this.userShotsLockbox = userShotsLockbox;
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    lastFiredShots = userShotsLockbox.reportUserShots();
    return lastFiredShots;
  }
}
