package cs3500.pa04.model.game;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests PlayerBoards
 */
class PlayerBoardsTest {
  static final int SEED = 413;
  static final int BOARD_HEIGHT = 7;
  static final int BOARD_WIDTH = 6;
  static CellState[][] expPlayerBoard;
  static CellState[][] expOpponentBoard;
  static ArrayList<Coord> missShotsCoords;
  static ArrayList<Coord> submarineHitCoords;
  static ArrayList<Coord> missAndHitShots;
  static Ship testSubmarine;
  static PlayerBoardsInteractive testBoardsInteractive;
  static PlayerBoardsState testBoardsState;
  static ArrayList<Ship> survShips;

  /**
   * Initializes ship specs to test LocalHumanPlayerTest
   */
  @BeforeAll
  public static void setup() {
    missShotsCoords = new ArrayList<>(List.of(
        new Coord(0, 0), new Coord(0, 1), new Coord(5, 6)
    ));
    submarineHitCoords = new ArrayList<>(List.of(
        new Coord(4, 0), new Coord(4, 1), new Coord(4, 2)
    ));
    missAndHitShots = new ArrayList<>(missShotsCoords);
    missAndHitShots.addAll(submarineHitCoords);

    Map<ShipType, Integer> specs = new HashMap<>(Map.of(
        ShipType.CARRIER, 2,
        ShipType.BATTLESHIP, 2,
        ShipType.DESTROYER, 1,
        ShipType.SUBMARINE, 1));
    PlayerBoards mockBoards = new PlayerBoards(SEED);
    mockBoards.setupBoards(BOARD_HEIGHT + 3, BOARD_WIDTH + 3, specs);

    PlayerBoards testBoards = new PlayerBoards(SEED);
    testBoardsState = testBoards;
    testBoardsInteractive = testBoards;
    survShips = testBoards.setupBoards(BOARD_HEIGHT, BOARD_WIDTH, specs);

    for (Ship curShip : survShips) {
      if (curShip.getShipType() == ShipType.SUBMARINE) {
        testSubmarine = curShip;
        break;
      }
    }

    expPlayerBoard = new CellState[BOARD_HEIGHT][BOARD_WIDTH];
    for (CellState[] row : expPlayerBoard) {
      Arrays.fill(row, CellState.SHIP); // Board is almost entirely ship at this point
    }
    expPlayerBoard[0][0] = CellState.EMPTY;
    expPlayerBoard[0][2] = CellState.EMPTY;
    expPlayerBoard[0][3] = CellState.EMPTY;
    expPlayerBoard[1][0] = CellState.EMPTY;
    expPlayerBoard[1][3] = CellState.EMPTY;
    expPlayerBoard[2][3] = CellState.EMPTY;
    expPlayerBoard[3][3] = CellState.EMPTY;
    expPlayerBoard[3][4] = CellState.EMPTY;
    expPlayerBoard[4][3] = CellState.EMPTY;
    expPlayerBoard[4][4] = CellState.EMPTY;
    expPlayerBoard[5][3] = CellState.EMPTY;
    expPlayerBoard[5][4] = CellState.EMPTY;
    expPlayerBoard[6][5] = CellState.EMPTY;

    expOpponentBoard = new CellState[BOARD_HEIGHT][BOARD_WIDTH];
    for (CellState[] row : expOpponentBoard) {
      Arrays.fill(row, CellState.UNKNOWN);
    }
  }

  /**
   * Tests PlayerBoardsTest's name, setup, reportDamage methods
   */
  @Test
  public void testSetupAndDamage() {
    // Testing that the setup went correct
    CellState[][] prodPlayerBoard = testBoardsState.getPlayerBoardState();
    for (int i = 0; i < BOARD_HEIGHT; i++) {
      assertArrayEquals(expPlayerBoard[i], prodPlayerBoard[i]);
    }

    CellState[][] prodOpponentBoard = testBoardsState.getOpponentBoardState();
    for (int i = 0; i < BOARD_HEIGHT; i++) {
      assertArrayEquals(expOpponentBoard[i], prodOpponentBoard[i]);
    }

    // Couldn't separate into two methods because must maintain predictable behaviour of the board
    // Testing ReportDamage from here on
    PlayerBoards mockBoards = new PlayerBoards(SEED);
    assertThrows(IllegalStateException.class, () -> mockBoards.reportDamage(submarineHitCoords));

    assertEquals(6, testBoardsInteractive.numberOfAllowedShots());
    assertTrue(survShips.contains(testSubmarine));

    // Test for cases where no shots hit
    ArrayList<Coord> prodSuccessfulShots = testBoardsInteractive.reportDamage(missShotsCoords);
    assertEquals(6, testBoardsInteractive.numberOfAllowedShots());
    assertTrue(survShips.contains(testSubmarine));
    assertTrue(prodSuccessfulShots.isEmpty());

    expPlayerBoard[0][0] = CellState.MISS;
    expPlayerBoard[1][0] = CellState.MISS;
    expPlayerBoard[6][5] = CellState.MISS;
    prodPlayerBoard = testBoardsState.getPlayerBoardState();
    for (int i = 0; i < BOARD_HEIGHT; i++) {
      assertArrayEquals(expPlayerBoard[i], prodPlayerBoard[i]);
    }
    prodOpponentBoard = testBoardsState.getOpponentBoardState();
    for (int i = 0; i < BOARD_HEIGHT; i++) {
      assertArrayEquals(expOpponentBoard[i], prodOpponentBoard[i]);
    }

    // Test for cases where some shots hit
    prodSuccessfulShots = testBoardsInteractive.reportDamage(missAndHitShots);
    assertEquals(5, testBoardsInteractive.numberOfAllowedShots());
    assertEquals(new HashSet<>(prodSuccessfulShots), new HashSet<>(submarineHitCoords));
    assertTrue(testBoardsState.playerHasSurvivingShips());
    assertFalse(survShips.contains(testSubmarine));

    expPlayerBoard[0][4] = CellState.HIT;
    expPlayerBoard[1][4] = CellState.HIT;
    expPlayerBoard[2][4] = CellState.HIT;
    prodPlayerBoard = testBoardsState.getPlayerBoardState();
    for (int i = 0; i < BOARD_HEIGHT; i++) {
      assertArrayEquals(expPlayerBoard[i], prodPlayerBoard[i]);
    }
    prodOpponentBoard = testBoardsState.getOpponentBoardState();
    for (int i = 0; i < BOARD_HEIGHT; i++) {
      assertArrayEquals(expOpponentBoard[i], prodOpponentBoard[i]);
    }
  }

  /**
   * Tests PlayerBoardsTest's successfulHits and takeShots
   */
  @Test
  public void testSuccessfulHitsAndTakeShots() {
    PlayerBoards mockBoards = new PlayerBoards(SEED);
    assertThrows(IllegalStateException.class, () ->
        mockBoards.successfulHits(submarineHitCoords, submarineHitCoords));

    CellState[][] prodOpponentBoard = testBoardsState.getOpponentBoardState();
    for (int i = 0; i < BOARD_HEIGHT; i++) {
      assertArrayEquals(expOpponentBoard[i], prodOpponentBoard[i]);
    }

    testBoardsInteractive.successfulHits(submarineHitCoords, missAndHitShots);

    expOpponentBoard[0][4] = CellState.HIT;
    expOpponentBoard[1][4] = CellState.HIT;
    expOpponentBoard[2][4] = CellState.HIT;
    expOpponentBoard[0][0] = CellState.MISS;
    expOpponentBoard[1][0] = CellState.MISS;
    expOpponentBoard[6][5] = CellState.MISS;
    prodOpponentBoard = testBoardsState.getOpponentBoardState();
    for (int i = 0; i < BOARD_HEIGHT; i++) {
      assertArrayEquals(expOpponentBoard[i], prodOpponentBoard[i]);
    }
  }


}