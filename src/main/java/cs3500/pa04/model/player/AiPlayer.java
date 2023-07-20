package cs3500.pa04.model.player;

import cs3500.pa04.model.game.CellState;
import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.PlayerBoardsInteractive;
import java.util.ArrayList;
import java.util.Random;

/**
 * Abstract class that abstracts common methods for local players
 */
public abstract class AiPlayer extends AbstractPlayer {
  protected Random aiRandom;

  /**
   * Constructor for AiPlayer
   *
   * @param name String name of the player
   * @param boards PlayerBoardsInteractive player pov boards that are going to keep track of the
   *               state of the game
   * @param seed int seed for predictable behaviour of Random, used for testing
   */
  public AiPlayer(String name, PlayerBoardsInteractive boards, int seed) {
    super(name, boards);
    this.aiRandom = new Random(seed);
  }

  /**
   * Constructs a list of Coord of all UNKNOWN cells
   *
   * @return Constructs a list of Coord of all UNKNOWN cells
   */
  protected ArrayList<Coord> collectAllUnknownCells() {
    CellState[][] opponentBoard = playerPovBoards.getOpponentBoardState();
    ArrayList<Coord> unknownCells = new ArrayList<>();

    for (int i = 0; i < boardHeight; i++) {
      for (int j = 0; j < boardWidth; j++) {
        if (opponentBoard[i][j] == CellState.UNKNOWN) {
          unknownCells.add(new Coord(j, i));
        }
      }
    }
    return unknownCells;
  }
}
