package cs3500.pa04.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import cs3500.pa04.controller.viewctrl.CliViewController;
import cs3500.pa04.model.GameResult;
import cs3500.pa04.model.game.CellState;
import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.ShipType;
import cs3500.pa04.view.AppendableOutCh;
import cs3500.pa04.view.GameView;
import cs3500.pa04.view.ReadableInCh;
import cs3500.pa04.view.UiReader;
import cs3500.pa04.view.UiWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CliViewControllerTest {
  private static final String DIVIDER = "------------------------------------------------------\n";
  static final int BOARD_HEIGHT = 7;
  static final int BOARD_WIDTH = 6;
  static Appendable appendable;
  static CliViewController cliViewController;
  static CellState[][] testPlayerBoard;
  static CellState[][] testOpponentBoard;

  /**
   * Initializes CliViewController
   */
  @BeforeAll
  public static void setup() {
    testPlayerBoard = new CellState[BOARD_HEIGHT][BOARD_WIDTH];
    for (CellState[] row : testPlayerBoard) {
      Arrays.fill(row, CellState.EMPTY);
    }
    // Placing a ship, hits and misses to verify render
    testPlayerBoard[0][0] = CellState.SHIP;
    testPlayerBoard[1][0] = CellState.SHIP;
    testPlayerBoard[2][0] = CellState.SHIP;
    testPlayerBoard[3][0] = CellState.HIT;
    testPlayerBoard[0][1] = CellState.MISS;

    testOpponentBoard = new CellState[BOARD_HEIGHT][BOARD_WIDTH];
    for (CellState[] row : testOpponentBoard) {
      Arrays.fill(row, CellState.UNKNOWN);
    }
    // Placing hits and misses to verify render
    testOpponentBoard[4][3] = CellState.HIT;
    testOpponentBoard[6][2] = CellState.MISS;
  }

  /**
   * Tests displayWelcomeMessage method
   */
  @Test
  public void testDisplayWelcomeMessage() {
    resetForInputTest("");
    String expOut = """

        ______       _   _   _      _____       _           \s
        | ___ \\     | | | | | |    /  ___|     | |          \s
        | |_/ / __ _| |_| |_| | ___\\ `--.  __ _| |_   _____ \s
        | ___ \\/ _` | __| __| |/ _ \\`--. \\/ _` | \\ \\ / / _ \\\s
        | |_/ / (_| | |_| |_| |  __/\\__/ / (_| | |\\ V / (_) |
        \\____/ \\__,_|\\__|\\__|_|\\___\\____/ \\__,_|_| \\_/ \\___/\s

        """ + "Hello! Welcome to the OOD BattleSalvo Game!";

    try {
      cliViewController.displayWelcomeMessage();
    } catch (IOException e) {
      fail();
    }
    assertEquals(expOut, appendable.toString());
  }

  /**
   * Tests displayPlayerPovBoards method
   */
  @Test
  public void testDisplayPlayerPovBoards() {
    resetForInputTest("");
    assertThrows(IllegalStateException.class,
        () -> cliViewController.displayPlayerPovBoards(testPlayerBoard, testOpponentBoard));

    resetForInputTest(BOARD_HEIGHT + " " + BOARD_WIDTH);
    try {
      cliViewController.getBoardDimensions();
    } catch (IOException e) {
      fail();
    }
    assertThrows(IllegalArgumentException.class,
        () -> cliViewController.displayPlayerPovBoards(null, null));
    assertThrows(IllegalArgumentException.class,
        () -> cliViewController.displayPlayerPovBoards(testPlayerBoard, null));
    assertThrows(IllegalArgumentException.class,
        () -> cliViewController.displayPlayerPovBoards(null, testOpponentBoard));

    String expOut = "First, select a height and width for the playing board."
        + "\nThe value for each dimension must be an integer in the range [6, 15], inclusive."
        + "\nPlease enter two space separated values below.\n" + DIVIDER + DIVIDER
        + """
          YOUR BOARD          ENEMY BOARD         BOARD LEGEND:
          # _ . . . .         ~ ~ ~ ~ ~ ~         . - EMPTY
          # . . . . .         ~ ~ ~ ~ ~ ~         # - SHIP
          # . . . . .         ~ ~ ~ ~ ~ ~         X - HIT
          X . . . . .         ~ ~ ~ ~ ~ ~         _ - MISS
          . . . . . .         ~ ~ ~ X ~ ~         ~ - UNKNOWN
          . . . . . .         ~ ~ ~ ~ ~ ~\s
          . . . . . .         ~ ~ _ ~ ~ ~\s
          """;

    try {
      cliViewController.displayPlayerPovBoards(testPlayerBoard, testOpponentBoard);
    } catch (IOException e) {
      fail();
    }
    assertEquals(expOut, appendable.toString());
  }

  /**
   * Tests displayMsg method
   */
  @Test
  public void testDisplayMsg() {
    resetForInputTest("");
    String expOut = "TEST MESSAGE";

    try {
      cliViewController.displayMsg(expOut);
    } catch (IOException e) {
      fail();
    }
    assertEquals(expOut, appendable.toString());
  }

  /**
   * Tests displayEndOfGameMsg method
   */
  @Test
  public void testDisplayEndOfGameMsg() {
    // Testing for victory
    resetForInputTest("");
    String expOut = """
                    
           _   _ _____ _____ _____ _____________   ___\s
          | | | |_   _/  __ \\_   _|  _  | ___ \\ \\ / / |
          | | | | | | | /  \\/ | | | | | | |_/ /\\ V /| |
          | | | | | | | |     | | | | | |    /  \\ / | |
          \\ \\_/ /_| |_| \\__/\\ | | \\ \\_/ / |\\ \\  | | |_|
           \\___/ \\___/ \\____/ \\_/  \\___/\\_| \\_| \\_/ (_)
                    
          """;
    try {
      cliViewController.displayEndOfGameMsg(GameResult.WIN);
    } catch (IOException e) {
      fail();
    }
    assertEquals(expOut, appendable.toString());

    // Testing for loss
    resetForInputTest("");
    expOut = "\nUnfortunately, you lost. Better luck next time!";
    try {
      cliViewController.displayEndOfGameMsg(GameResult.LOSE);
    } catch (IOException e) {
      fail();
    }
    assertEquals(expOut, appendable.toString());

    // Testing for tie
    resetForInputTest("");
    expOut = "\nGame resulted in a tie!";
    try {
      cliViewController.displayEndOfGameMsg(GameResult.DRAW);
    } catch (IOException e) {
      fail();
    }
    assertEquals(expOut, appendable.toString());
  }

  /**
   * Tests getBoardDimensions method
   */
  @Test
  public void testGetBoardDimensions() {
    resetForInputTest("1" + "\nnot numbers"
        + "\n-1 6" + "\n17 6" + "\n7 4" + "\n7 16"
        + "\n" + BOARD_HEIGHT + " " + BOARD_WIDTH);
    String expOut = "First, select a height and width for the playing board."
        + "\nThe value for each dimension must be an integer in the range [6, 15], inclusive."
        + "\nPlease enter two space separated values below.\n" + DIVIDER
        // First invalid input option - not two values
        + "You entered an unexpected number of values!"
        + "\nPlease, enter exactly two values for height and width below.\n" + DIVIDER
        // Second invalid input option - not parsable as ints
        + "One or more of the values you entered isn't an integer."
        + "\nPlease, enter integer values for height and width below.\n" + DIVIDER
        // Third invalid input option - board dimensions out of bounds (4 tries)
        + "One or more of the values you entered isn't within the valid range."
        + "\nPlease, enter values in the range [6, 15], inclusive.\n" + DIVIDER
        + "One or more of the values you entered isn't within the valid range."
        + "\nPlease, enter values in the range [6, 15], inclusive.\n" + DIVIDER
        + "One or more of the values you entered isn't within the valid range."
        + "\nPlease, enter values in the range [6, 15], inclusive.\n" + DIVIDER
        + "One or more of the values you entered isn't within the valid range."
        + "\nPlease, enter values in the range [6, 15], inclusive.\n" + DIVIDER
        // Finally valid input
        + DIVIDER;

    int[] prodBoardDim = new int[2];
    try {
      prodBoardDim = cliViewController.getBoardDimensions();
    } catch (IOException e) {
      fail();
    }
    assertEquals(expOut, appendable.toString());
    assertEquals(BOARD_HEIGHT, prodBoardDim[0]);
    assertEquals(BOARD_WIDTH, prodBoardDim[1]);
  }

  /**
   * Tests getShipSpecifications method
   */
  @Test
  public void testGetShipSpecifications() {
    resetForInputTest("");
    assertThrows(IllegalStateException.class,
        () -> cliViewController.getShipSpecifications());

    resetForInputTest(BOARD_HEIGHT + " " + BOARD_WIDTH // For board dim init
        + "\n1 2 3 4 5" + "\nnot numbers but four"
        + "\n3 2 2 2" + "\n0 1 2 3"
        + "\n1 1 1 1");

    int maxFleetSize = Math.min(BOARD_HEIGHT, BOARD_WIDTH);
    int numShipTypes = ShipType.values().length;
    String expOut = "First, select a height and width for the playing board."
        + "\nThe value for each dimension must be an integer in the range [6, 15], inclusive."
        + "\nPlease enter two space separated values below.\n" + DIVIDER + DIVIDER
        + "Now, specify the make up of the fleet. "
        + "The available ship types are " + Arrays.toString(ShipType.values()) + "."
        + "\nThe fleet must contain at least one ship of each type and at most "
        + maxFleetSize + " ships in total."
        + "\nThe desired number of ships of each type should be input "
        + "in the same order as the list above."
        + "\nPlease enter values in a single line, space separated.\n" + DIVIDER
        // First invalid input option - not four values
        + "You entered an unexpected number of values!"
        + "\nPlease, enter exactly " + numShipTypes + " values.\n" + DIVIDER
        // Second invalid input option - not parsable as ints
        + "One or more of the values you entered isn't an integer."
        + "\nPlease, enter integer values for number of ships below.\n" + DIVIDER
        // Third invalid input option - fleet size too big
        + "The fleet size you entered is too large!"
        + "\nPlease, make sure that the fleet doesn't have more than " + maxFleetSize
        + " ships.\n" + DIVIDER
        // Fourth invalid input option - less than one ship of each type
        + "One or more of the values you entered isn't valid."
        + "\nPlease, make sure that there is at least one ship of each type.\n" + DIVIDER
        // Finally valid input
        + DIVIDER;

    Map<ShipType, Integer> expSpecs = new HashMap<>(Map.of(
        ShipType.CARRIER, 1,
        ShipType.BATTLESHIP, 1,
        ShipType.DESTROYER, 1,
        ShipType.SUBMARINE, 1
    ));
    Map<ShipType, Integer> prodSpecs  = new HashMap<>();
    try {
      cliViewController.getBoardDimensions();
      prodSpecs = cliViewController.getShipSpecifications();
    } catch (IOException e) {
      fail();
    }
    assertEquals(expOut, appendable.toString());
    assertEquals(expSpecs, prodSpecs);
  }

  /**
   * Tests getNShots method
   */
  @Test
  public void testGetNumShots() {
    int expNumShots = 2;
    resetForInputTest("");
    assertThrows(IllegalStateException.class,
        () -> cliViewController.getNumShots(expNumShots));

    resetForInputTest(BOARD_HEIGHT + " " + BOARD_WIDTH // For board dim init
        + "\n1 2 3" + "\n1 2\nnot numbers"
        + "\n4 1\n-1 3" + "\n4 1\n9 3" + "\n4 1\n1 -3" + "\n4 1\n1 9"
        + "\n4 1\n1 3");

    String expOut = "First, select a height and width for the playing board."
        + "\nThe value for each dimension must be an integer in the range [6, 15], inclusive."
        + "\nPlease enter two space separated values below.\n" + DIVIDER + DIVIDER
        + "Enter 0-indexed coordinates of " + expNumShots + " shots to attack the opponent."
        + "\nPlease enter two space separated values for each shot, "
        + "with each shot on a new line.\n" + DIVIDER
        // First invalid input option - not two values in line
        + "You entered an unexpected number of values!"
        + "\nPlease, enter exactly two values. Now enter the Salvo from the beginning.\n" + DIVIDER
        // Second invalid input option - not parsable as ints
        + "One or more of the values you entered isn't an integer."
        + "\nPlease, enter integer values for shot coordinates."
        + "\nNow enter the Salvo from the beginning.\n" + DIVIDER
        // Third invalid input option - board dimensions out of bounds (4 tries)
        + "One or more of the values you entered isn't within the valid range."
        + "\nPlease, enter valid 0-indexed coordinates for each of the shots."
        + "\nNow enter the Salvo from the beginning.\n" + DIVIDER
        + "One or more of the values you entered isn't within the valid range."
        + "\nPlease, enter valid 0-indexed coordinates for each of the shots."
        + "\nNow enter the Salvo from the beginning.\n" + DIVIDER
        + "One or more of the values you entered isn't within the valid range."
        + "\nPlease, enter valid 0-indexed coordinates for each of the shots."
        + "\nNow enter the Salvo from the beginning.\n" + DIVIDER
        + "One or more of the values you entered isn't within the valid range."
        + "\nPlease, enter valid 0-indexed coordinates for each of the shots."
        + "\nNow enter the Salvo from the beginning.\n" + DIVIDER
        // Finally valid input
        + DIVIDER;

    List<Coord> expCoords = new ArrayList<>(List.of(new Coord(4, 1), new Coord(1, 3)));
    List<Coord> prodCoords  = new ArrayList<>();
    try {
      cliViewController.getBoardDimensions();
      prodCoords = cliViewController.getNumShots(expNumShots);
    } catch (IOException e) {
      fail();
    }
    assertEquals(expOut, appendable.toString());
    assertEquals(expCoords, prodCoords);
  }

  /**
   * Re-initializes cliViewController given input to be read
   *
   * @param testInput to be input ot the readable input
   */
  private void resetForInputTest(String testInput) {
    Readable readable = new BufferedReader(new StringReader(testInput));
    UiReader reader = new ReadableInCh(readable);
    appendable = new StringBuilder();
    UiWriter writer = new AppendableOutCh(appendable);

    cliViewController = new CliViewController(new GameView(reader, writer));
  }
}