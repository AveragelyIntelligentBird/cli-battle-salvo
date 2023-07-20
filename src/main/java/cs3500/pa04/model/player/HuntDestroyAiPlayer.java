package cs3500.pa04.model.player;

import cs3500.pa04.model.game.CellState;
import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.PlayerBoardsInteractive;
import cs3500.pa04.model.game.Ship;
import cs3500.pa04.model.game.ShipType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a HuntDestroyAiPlayer, implementing Hunt-Destroy strategy
 */
public class HuntDestroyAiPlayer extends AiPlayer {
  private ArrayList<Coord> orderedHuntQueue;
  private final ArrayList<Coord> destroyExplorationQueue;
  private ArrayList<Coord> lastDestroyModeShots;
  private AiShootMode mode;

  /**
   * Constructor for ParityAiPlayer
   *
   * @param name String name of the AI player
   * @param boards PlayerBoardsInteractive for maintaining state of the game
   * @param seed int seed to initialize this AI's Random
   */
  public HuntDestroyAiPlayer(String name, PlayerBoardsInteractive boards, int seed) {
    super(name, boards, seed);
    this.mode = AiShootMode.HUNT;

    this.orderedHuntQueue = null;
    this.destroyExplorationQueue = new ArrayList<>();
    this.lastDestroyModeShots = new ArrayList<>();
  }

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    this.boardHeight = height;
    this.boardWidth = width;

    List<Ship> setUpShips = playerPovBoards.setupBoards(height, width, specifications);
    this.fleetSize = setUpShips.size();
    this.orderedHuntQueue = getOrderedHuntQueue();
    return setUpShips;
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    evaluatePreviousVolley();

    lastFiredShots = new ArrayList<>();
    int numShotsNeeded = playerPovBoards.numberOfAllowedShots();
    switch (mode) {
      case HUNT -> shootInHuntMode(numShotsNeeded);
      case DESTROY -> shootInDestroyMode(numShotsNeeded);
      default -> { }
    }

    return lastFiredShots;
  }

  /**
   * Evaluates previous volley and updates the destroy exploration queue
   */
  private void evaluatePreviousVolley() {
    for (Coord succShot : lastSuccShots) {
      if (lastDestroyModeShots.contains(succShot)) {
        destroyExplorationQueue.add(0, succShot);
      } else {
        destroyExplorationQueue.add(succShot);
      }
    }

    if (destroyExplorationQueue.size() != 0) {
      mode = AiShootMode.DESTROY;
    } else {
      mode = AiShootMode.HUNT;
    }
  }

  /**
   * Selects the necessary number of shots from the hunt pattern
   *
   * @param numShotsNeeded int number of shots needed from the AI player at this turn
   */
  private void shootInHuntMode(int numShotsNeeded) {
    CellState[][] board = playerPovBoards.getOpponentBoardState();

    ArrayList<Coord> backupUnknownQueue = collectAllUnknownCells();

    while (numShotsNeeded != 0) {
      Coord curHuntCoord;
      if (!orderedHuntQueue.isEmpty()) {
        curHuntCoord = orderedHuntQueue.remove(0);
      } else {
        curHuntCoord = backupUnknownQueue.remove(0);
        if (backupUnknownQueue.isEmpty()) {
          break;
        }
      }

      if (board[curHuntCoord.getY()][curHuntCoord.getX()] == CellState.UNKNOWN
          && !lastFiredShots.contains(curHuntCoord)) {
        lastFiredShots.add(curHuntCoord);
        numShotsNeeded -= 1;
      }
    }
  }

  /**
   * Selects the necessary number of shots around cells queued in destroyExplorationQueue
   *
   * @param numShotsNeeded int number of shots needed from the AI player at this turn
   */
  private void shootInDestroyMode(int numShotsNeeded) {
    ArrayList<Coord> cellsToExploreFrom = new ArrayList<>(destroyExplorationQueue);

    for (Coord curCell : cellsToExploreFrom) {
      ArrayList<Coord> possibleShots = getUnknownCellsAround(curCell);
      possibleShots.removeIf(lastFiredShots::contains);

      if (possibleShots.size() <= numShotsNeeded) {
        lastFiredShots.addAll(possibleShots);
        numShotsNeeded -= possibleShots.size();
        destroyExplorationQueue.remove(curCell);
      } else {
        lastFiredShots.addAll(possibleShots.stream().limit(numShotsNeeded).toList());
        numShotsNeeded = 0;
        break;
      }
    }
    lastDestroyModeShots = new ArrayList<>(lastFiredShots);

    if (numShotsNeeded != 0) {
      shootInHuntMode(numShotsNeeded);
    }
  }

  /**
   * Gets a list of possible shot locations around a given cell
   *
   * @param curCell coordinate of the current cell
   * @return list of coordinates where valid shots can be taken
   */
  private ArrayList<Coord> getUnknownCellsAround(Coord curCell) {
    ArrayList<Coord> shots = new ArrayList<>();
    CellState[][] opponentBoard = playerPovBoards.getOpponentBoardState();
    int x = curCell.getX();
    int y = curCell.getY();

    // Cell on the right
    if (x + 1 < boardWidth && opponentBoard[y][x + 1] == CellState.UNKNOWN) {
      shots.add(new Coord(x + 1, y));
    }

    // Cell on the left
    if (x - 1 >= 0 && opponentBoard[y][x - 1] == CellState.UNKNOWN) {
      shots.add(new Coord(x - 1, y));
    }

    // Cell above
    if (y + 1 < boardHeight && opponentBoard[y + 1][x] == CellState.UNKNOWN) {
      shots.add(new Coord(x, y + 1));
    }

    // Cell below
    if (y - 1 >= 0 && opponentBoard[y - 1][x] == CellState.UNKNOWN) {
      shots.add(new Coord(x, y - 1));
    }

    return shots;
  }


  /**
   * Constructs a hunt queue from hunt pattern cells ordered by ring location, from the outside in
   *
   * @return A list of lists of Coords, representing each parity shot, split into rings
   */
  private ArrayList<Coord> getOrderedHuntQueue() {
    // Stores the possible shots to choose from
    ArrayList<ArrayList<Coord>> listOfRows = new ArrayList<>();

    // Traverse the board, storing all parity coords in parityThree
    for (int i = 0; i < boardHeight; i++) {
      // Create currentRow to represent the current row
      ArrayList<Coord> currentRow = new ArrayList<>();
      for (int j = 0; j < boardWidth; j++) {
        // If it is on our expected grid, add it to the current row
        if ((j % 3 == 0) && (i % 3 == 0)) {
          currentRow.add(new Coord(j, i));
        } else if ((j % 3 == 1) && (i % 3 == 1)) {
          currentRow.add(new Coord(j, i));
        } else if ((j % 3 == 2) && (i % 3 == 2)) {
          currentRow.add(new Coord(j, i));
        }
      }
      // Add the ArrayList representing the currentRow
      listOfRows.add(currentRow);
    }

    return rowsToRingHuntQueue(listOfRows);
  }

  /**
   * Converts a row list of the hunt pattern to a hunt queue ordered by rind location, from outside
   * in
   *
   * @param rowsList List of hunt pattern rows
   * @return hunt pattern to a hunt queue ordered by rind location
   */
  private ArrayList<Coord> rowsToRingHuntQueue(ArrayList<ArrayList<Coord>> rowsList) {
    // Calculate the number of rings using the dimensions of the board
    int numOfRings = Math.min(boardWidth, boardHeight) / 2;

    // Create a variable to store the organized list
    ArrayList<Coord> organizedList = new ArrayList<>();

    // Create one ring at a time, from outermost working in
    for (int ring = 0; ring < numOfRings; ring++) {
      // Variable to hold the currentRing information
      ArrayList<Coord> currentRing = new ArrayList<>();

      // For each row from the original list, add the coords that belong in the current ring
      for (ArrayList<Coord> row : rowsList) {
        // Traverse the coordinates in the row
        for (Coord coord : row) {
          // Check if the current coordinate belongs to the current ring
          if (isInRing(coord, ring)) {
            currentRing.add(coord);
          }
        }
      }

      // Add the current ring to the organized list
      organizedList.addAll(currentRing);
    }

    return organizedList;
  }

  /**
   * Checks whether the given coordinate is in a given ring
   *
   * @param coord Coord to be detemined
   * @param ring int ring number
   * @return true if the given coordinate belongs to a given ring
   */
  private boolean isInRing(Coord coord, int ring) {
    int x = coord.getX();
    int y = coord.getY();

    // Logic for whether a coord is in a given ring
    return ((x == ring) && (y >= ring) && (y < boardHeight - ring)) // first column of each ring
        || ((x == boardWidth - 1 - ring) && (y >= ring) && (y < boardHeight - ring))
        // last column of each ring
        || (((x > ring) && (x < boardWidth - 1 - ring))
        && ((y == ring) || (y == boardHeight - 1 - ring)));
  }
}