package cs3500.pa04.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import cs3500.pa04.controller.lockbox.UserShotsLockbox;
import cs3500.pa04.controller.viewctrl.CliViewController;
import cs3500.pa04.model.game.PlayerBoards;
import cs3500.pa04.model.player.HumanPlayer;
import cs3500.pa04.model.player.RandomAiPlayer;
import cs3500.pa04.view.AppendableOutCh;
import cs3500.pa04.view.GameView;
import cs3500.pa04.view.ReadableInCh;
import cs3500.pa04.view.UiReader;
import cs3500.pa04.view.UiWriter;
import java.io.BufferedReader;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests OneHumanOneAiController
 */
class OneHumanOneAiControllerTest {
  static final String PLAYER_NAME = "NAME";
  static final int SEED = 413;
  static CliViewController cliViewController;
  static Appendable appendable;
  static PlayerBoards testAiBoards;
  static RandomAiPlayer testAi;
  static PlayerBoards testUiBoards;
  static UserShotsLockbox lockbox;
  static HumanPlayer testUiPlayer;
  static OneHumanOneAiController runner;


  /**
   * Creates dummy players
   */
  @BeforeAll
  public static void setup() {
    testAiBoards = new PlayerBoards(SEED);
    testAi = new RandomAiPlayer(PLAYER_NAME, testAiBoards, SEED);
    lockbox =  new UserShotsLockbox();
    testUiBoards = new PlayerBoards(SEED);
    testUiPlayer = new HumanPlayer(PLAYER_NAME, testUiBoards, lockbox);
  }

  /**
   * Runs a dummy session
   */
  @Test
  public void testGameRunner() {
    // Shots to a single position to cause loss of player
    resetForInputTest("6 6\n" + "2 2 1 1\n"
        + "0 0\n0 0\n0 0\n0 0\n0 0\n0 0\n"
        + "0 0\n0 0\n0 0\n0 0\n0 0\n0 0\n"
        + "0 0\n0 0\n0 0\n0 0\n0 0\n0 0\n"
        + "0 0\n0 0\n0 0\n0 0\n0 0\n0 0\n"
        + "0 0\n0 0\n0 0\n0 0\n0 0\n"
        + "0 0\n0 0\n0 0\n");
    runner = new OneHumanOneAiController(cliViewController,
        testUiPlayer, testUiBoards, lockbox,
        testAi, testAiBoards);

    try {
      runner.run();
    } catch (Exception e) {
      fail();
    }

    String expOut = """
                
        ______       _   _   _      _____       _           \s
        | ___ \\     | | | | | |    /  ___|     | |          \s
        | |_/ / __ _| |_| |_| | ___\\ `--.  __ _| |_   _____ \s
        | ___ \\/ _` | __| __| |/ _ \\`--. \\/ _` | \\ \\ / / _ \\\s
        | |_/ / (_| | |_| |_| |  __/\\__/ / (_| | |\\ V / (_) |
        \\____/ \\__,_|\\__|\\__|_|\\___\\____/ \\__,_|_| \\_/ \\___/\s
                
        Hello! Welcome to the OOD BattleSalvo Game!First, select a height and width for the\s"""
        + """
        playing board.
        The value for each dimension must be an integer in the range [6, 15], inclusive.
        Please enter two space separated values below.
        ------------------------------------------------------
        ------------------------------------------------------
        Now, specify the make up of the fleet. The available ship types are [CARRIER,\s"""
        + """
        BATTLESHIP, DESTROYER, SUBMARINE].
        The fleet must contain at least one ship of each type and at most 6 ships in total.
        The desired number of ships of each type should be input in the same order as the\s"""
        + """
        list above.
        Please enter values in a single line, space separated.
        ------------------------------------------------------
        ------------------------------------------------------
        YOUR BOARD          ENEMY BOARD         BOARD LEGEND:
        . # . # # .         ~ ~ ~ ~ ~ ~         . - EMPTY
        . # # # # #         ~ ~ ~ ~ ~ ~         # - SHIP
        # # # # # #         ~ ~ ~ ~ ~ ~         X - HIT
        # # # # # #         ~ ~ ~ ~ ~ ~         _ - MISS
        # # # # # #         ~ ~ ~ ~ ~ ~         ~ - UNKNOWN
        . # # . # .         ~ ~ ~ ~ ~ ~\s
        Enter 0-indexed coordinates of 6 shots to attack the opponent.
        Please enter two space separated values for each shot, with each shot on a new line.
        ------------------------------------------------------
        ------------------------------------------------------
        YOUR BOARD          ENEMY BOARD         BOARD LEGEND:
        . # . # X .         _ ~ ~ ~ ~ ~         . - EMPTY
        . # # # # X         ~ ~ ~ ~ ~ ~         # - SHIP
        # # # # # #         ~ ~ ~ ~ ~ ~         X - HIT
        # # # # # #         ~ ~ ~ ~ ~ ~         _ - MISS
        # X # X X #         ~ ~ ~ ~ ~ ~         ~ - UNKNOWN
        . # # . X .         ~ ~ ~ ~ ~ ~\s
        Enter 0-indexed coordinates of 6 shots to attack the opponent.
        Please enter two space separated values for each shot, with each shot on a new line.
        ------------------------------------------------------
        ------------------------------------------------------
        YOUR BOARD          ENEMY BOARD         BOARD LEGEND:
        _ # . # X _         _ ~ ~ ~ ~ ~         . - EMPTY
        . # # # # X         ~ ~ ~ ~ ~ ~         # - SHIP
        # # # # # #         ~ ~ ~ ~ ~ ~         X - HIT
        # # # # # #         ~ ~ ~ ~ ~ ~         _ - MISS
        X X X X X X         ~ ~ ~ ~ ~ ~         ~ - UNKNOWN
        _ # # . X .         ~ ~ ~ ~ ~ ~\s
        Enter 0-indexed coordinates of 6 shots to attack the opponent.
        Please enter two space separated values for each shot, with each shot on a new line.
        ------------------------------------------------------
        ------------------------------------------------------
        YOUR BOARD          ENEMY BOARD         BOARD LEGEND:
        _ # . # X _         _ ~ ~ ~ ~ ~         . - EMPTY
        . X # # # X         ~ ~ ~ ~ ~ ~         # - SHIP
        X # # # # #         ~ ~ ~ ~ ~ ~         X - HIT
        X X # # X #         ~ ~ ~ ~ ~ ~         _ - MISS
        X X X X X X         ~ ~ ~ ~ ~ ~         ~ - UNKNOWN
        _ X # . X .         ~ ~ ~ ~ ~ ~\s
        Enter 0-indexed coordinates of 5 shots to attack the opponent.
        Please enter two space separated values for each shot, with each shot on a new line.
        ------------------------------------------------------
        ------------------------------------------------------
        YOUR BOARD          ENEMY BOARD         BOARD LEGEND:
        _ # . X X _         _ ~ ~ ~ ~ ~         . - EMPTY
        . X # X # X         ~ ~ ~ ~ ~ ~         # - SHIP
        X # # # X #         ~ ~ ~ ~ ~ ~         X - HIT
        X X # X X X         ~ ~ ~ ~ ~ ~         _ - MISS
        X X X X X X         ~ ~ ~ ~ ~ ~         ~ - UNKNOWN
        _ X # _ X .         ~ ~ ~ ~ ~ ~\s
        Enter 0-indexed coordinates of 5 shots to attack the opponent.
        Please enter two space separated values for each shot, with each shot on a new line.
        ------------------------------------------------------
        ------------------------------------------------------
        YOUR BOARD          ENEMY BOARD         BOARD LEGEND:
        _ # _ X X _         _ ~ ~ ~ ~ ~         . - EMPTY
        . X X X X X         ~ ~ ~ ~ ~ ~         # - SHIP
        X # # X X #         ~ ~ ~ ~ ~ ~         X - HIT
        X X # X X X         ~ ~ ~ ~ ~ ~         _ - MISS
        X X X X X X         ~ ~ ~ ~ ~ ~         ~ - UNKNOWN
        _ X X _ X _         ~ ~ ~ ~ ~ ~\s
        Enter 0-indexed coordinates of 3 shots to attack the opponent.
        Please enter two space separated values for each shot, with each shot on a new line.
        ------------------------------------------------------
        ------------------------------------------------------
                
        Unfortunately, you lost. Better luck next time!""";
    assertEquals(expOut, appendable.toString());
  }

  /**
   * Re-initializes cliViewController given input to be read
   * Cannot be labeled as BeforeEach because takes input
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