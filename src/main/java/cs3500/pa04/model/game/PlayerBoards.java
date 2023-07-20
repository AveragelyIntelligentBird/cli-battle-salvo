package cs3500.pa04.model.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents the player's POV board state and updates it according to in-game salvo exchange
 */
public class PlayerBoards implements PlayerBoardsInteractive {
  private final Random random;
  private int boardHeight;
  private int boardWidth;
  private ArrayList<Ship> survShips;
  private CellState[][] playerBoard;
  private CellState[][] opponentBoard;

  /**
   * Constructor for PlayerBoards
   *
   * @param seed int seed for predictable behaviour of Random, used for testing
   */
  public PlayerBoards(int seed) {
    this.random = new Random(seed);

    playerBoard = null;
    opponentBoard = null;
    survShips = null;
  }

  /**
   * Given the specifications for a board, returns a list of ships
   *
   * @param height         the height of the board
   * @param width          the width of the board
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public ArrayList<Ship> setupBoards(int height, int width, Map<ShipType, Integer> specifications) {
    this.boardHeight = height;
    this.boardWidth = width;

    this.opponentBoard = new CellState[height][width];
    for (CellState[] row : opponentBoard) {
      Arrays.fill(row, CellState.UNKNOWN);
    }

    this.playerBoard = new CellState[height][width];
    for (CellState[] row : playerBoard) {
      Arrays.fill(row, CellState.EMPTY);
    }

    ArrayList<ShipType> shipsToBePlaced = new ArrayList<>();
    for (ShipType shipType : ShipType.values()) {
      int n = specifications.get(shipType);
      shipsToBePlaced.addAll(Collections.nCopies(n, shipType));
    }
    this.survShips = populateBoardWithShips(shipsToBePlaced);

    return survShips;
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   *         ship on this board
   */
  @Override
  public ArrayList<Coord> reportDamage(ArrayList<Coord> opponentShotsOnBoard) {
    if (playerBoard == null || opponentBoard == null || survShips == null) {
      throw new IllegalStateException();
    }
    ArrayList<Coord> successfulHits = new ArrayList<>();

    for (Coord shotCoord : opponentShotsOnBoard) {
      boolean noShipWasHit = true;
      for (Ship curShip : survShips) {
        if (curShip.isHitBy(shotCoord)) {
          successfulHits.add(shotCoord);
          playerBoard[shotCoord.getY()][shotCoord.getX()] = CellState.HIT;
          noShipWasHit = false;
          break;
        }
      }
      if (noShipWasHit) {
        playerBoard[shotCoord.getY()][shotCoord.getX()] = CellState.MISS;
      }
    }

    survShips.removeIf(Ship::isShipDestroyed);
    return successfulHits;
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   * @param lastFiredShots list of shots previously shot by the player
   */
  @Override
  public void successfulHits(ArrayList<Coord> shotsThatHitOpponentShips,
                             ArrayList<Coord> lastFiredShots) {
    if (playerBoard == null || opponentBoard == null || survShips == null) {
      throw new IllegalStateException();
    }

    for (Coord successfulHitCoord : shotsThatHitOpponentShips) {
      opponentBoard[successfulHitCoord.getY()][successfulHitCoord.getX()] = CellState.HIT;
      lastFiredShots.remove(successfulHitCoord);
    }
    for (Coord missedHitCoord : lastFiredShots) {
      opponentBoard[missedHitCoord.getY()][missedHitCoord.getX()] = CellState.MISS;
    }

    lastFiredShots = new ArrayList<>();
  }

  /**
   * Getter for the current player board
   *
   * @return CellState 2d array representing the current state of player board
   */
  @Override
  public CellState[][] getPlayerBoardState() {
    return this.playerBoard;
  }

  /**
   * Getter for the current opponent board
   *
   * @return CellState 2d array representing the current state of opponent board
   */
  @Override
  public CellState[][] getOpponentBoardState() {
    return this.opponentBoard;
  }

  /**
   * Checks whether the player can continue the game
   *
   * @return true if the player still has surviving ships
   */
  @Override
  public boolean playerHasSurvivingShips() {
    return !this.survShips.isEmpty();
  }

  /**
   * How many shots a player can take
   *
   * @return int number of allowed shots in the next salvo
   */
  @Override
  public int numberOfAllowedShots() {
    int unknownCellCounter = 0;

    for (int i = 0; i < boardHeight; i++) {
      for (int j = 0; j < boardWidth; j++) {
        if (opponentBoard[i][j] == CellState.UNKNOWN) {
          unknownCellCounter++;
        }
      }
    }

    return Math.min(unknownCellCounter, this.survShips.size());
  }

  /**
   * Populates player's board with ships, updates playerBoard
   *
   * @param shipsToBePlaced list of ship types to be placed, ordered from smallest to largest
   * @return list of placed ships
   */
  private ArrayList<Ship> populateBoardWithShips(ArrayList<ShipType> shipsToBePlaced) {
    ArrayList<Ship> placedShips = new ArrayList<>();

    ArrayList<Coord> freeBoardCells = new ArrayList<>();
    for (int i = 0; i < boardHeight; i++) {
      for (int j = 0; j < boardWidth; j++) {
        freeBoardCells.add(new Coord(j, i));
      }
    }

    for (ShipType curShipType : shipsToBePlaced) {
      Ship placedShip;

      boolean shipNotPlaced = true;
      while (shipNotPlaced) {
        Collections.shuffle(freeBoardCells, random);
        Coord curCoord = freeBoardCells.get(0);

        if ((placedShip = canPlaceShipFrom(curCoord, curShipType)) != null) {
          placedShips.add(placedShip);
          freeBoardCells.removeAll(placedShip.getSurvShipCoords());

          shipNotPlaced = false;
        }
      }
    }

    return placedShips;
  }

  /**
   * Checks if a ship of a given length can be placed starting from the given coordinate
   * If so, places the ship and returns a list of coordinates that are now occupied by the ship
   *
   * @param curCoord starting coordinate from which the ship placement is being considered
   * @param shipType type of ship to be placed
   * @return a placed, fully initialized ship OR null if can't place ship
   */
  private Ship canPlaceShipFrom(Coord curCoord, ShipType shipType) {
    int shipLen = shipType.shipLength;
    ArrayList<Coord> shipCoords = new ArrayList<>();
    ShipOrientation shipOrientation = null;

    ArrayList<ShipOrientation> orientations = new ArrayList<>(List.of(ShipOrientation.values()));
    Collections.shuffle(orientations, random);
    for (ShipOrientation attemptedOrientation : orientations) {
      int curX = curCoord.getX();
      int curY = curCoord.getY();

      boolean placementFailed = false;
      switch (attemptedOrientation) {
        case HORIZONTAL -> {
          for (int i = 0; i < shipLen; i++) {
            if (curX < boardWidth && playerBoard[curY][curX] == CellState.EMPTY) {
              shipCoords.add(new Coord(curX, curY));
            } else {
              placementFailed = true;
              break;
            }
            curX++;
          }
        }
        case VERTICAL -> {
          for (int i = 0; i < shipLen; i++) {
            if (curY < boardHeight && playerBoard[curY][curX] == CellState.EMPTY) {
              shipCoords.add(new Coord(curX, curY));
            } else {
              placementFailed = true;
              break;
            }
            curY++;
          }
        }
        default -> { }
      }

      if (placementFailed) {
        shipCoords = new ArrayList<>();
      } else {
        shipOrientation = attemptedOrientation;
        break;
      }
    }

    if (shipCoords.isEmpty()) {
      return null;
    }

    for (Coord shipCoord : shipCoords) {
      //Places ship on board
      playerBoard[shipCoord.getY()][shipCoord.getX()] = CellState.SHIP;
    }
    return new Ship(shipType, shipOrientation, shipCoords);
  }
}
