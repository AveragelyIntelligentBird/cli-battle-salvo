package cs3500.pa04.model.player;

import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.PlayerBoardsInteractive;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a local AI player that always shoots its shots randomly
 * Used for PA03 submission and comparative testing against more advanced AIs
 */
public class RandomAiPlayer extends AiPlayer {
  /**
   * Constructor for RandomAiPlayer
   *
   * @param name String name of the player
   * @param boards PlayerBoardsInteractive player pov boards that are going to keep track of the
   *               state of the game
   * @param seed int seed for predictable behaviour of Random, used for testing
   */
  public RandomAiPlayer(String name, PlayerBoardsInteractive boards, int seed) {
    super(name, boards, seed);
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    ArrayList<Coord> possibleShotCoords = collectAllUnknownCells();
    Collections.shuffle(possibleShotCoords, aiRandom);

    int expNumShots = playerPovBoards.numberOfAllowedShots();
    lastFiredShots = (ArrayList<Coord>) possibleShotCoords.stream()
            .limit(expNumShots).collect(Collectors.toList());
    return lastFiredShots;
  }
}
