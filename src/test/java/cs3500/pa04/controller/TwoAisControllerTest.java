package cs3500.pa04.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import cs3500.pa04.controller.viewctrl.CliViewController;
import cs3500.pa04.model.game.PlayerBoards;
import cs3500.pa04.model.player.HuntDestroyAiPlayer;
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
 * Tests TwoAisControllerTest
 */
class TwoAisControllerTest {
  static final String RANDOM_NAME = "Random AI";
  static final String HUNT_DESTROY_NAME = "Hunt Destroy AI";
  static final int SEED = 413;
  static CliViewController cliViewController;
  static Appendable appendable;
  static PlayerBoards testRandomAiBoards;
  static RandomAiPlayer testRandomAi;
  static PlayerBoards testHuntDestroyAiBoards;
  static HuntDestroyAiPlayer testHuntDestroyAi;
  static TwoAisController runner;


  /**
   * Creates dummy players
   */
  @BeforeAll
  public static void setup() {
    testRandomAiBoards = new PlayerBoards(SEED);
    testRandomAi = new RandomAiPlayer(RANDOM_NAME, testRandomAiBoards, SEED);
    testHuntDestroyAiBoards = new PlayerBoards(SEED);
    testHuntDestroyAi = new HuntDestroyAiPlayer(HUNT_DESTROY_NAME, testHuntDestroyAiBoards, SEED);
  }

  /**
   * Runs a dummy session with AI ties
   */
  @Test
  public void testGameRunnerTie() {
    // Shots to a single position to cause loss of player
    resetForInputTest("6 6\n" + "2 2 1 1\n");
    runner = new TwoAisController(cliViewController,
        testRandomAi, testRandomAiBoards,
        testRandomAi, testRandomAiBoards);

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
        Random AI and Random AI tied by destroying each other simultaneously.""";
    assertEquals(expOut, appendable.toString());
  }


  /**
   * Runs a dummy session where player 1 wins
   * Additionally, confirms HuntDestroy superiority
   */
  @Test
  public void testGameRunnerAi1Win() {
    // Shots to a single position to cause loss of player
    resetForInputTest("6 6\n" + "2 2 1 1\n");
    runner = new TwoAisController(cliViewController,
        testHuntDestroyAi, testHuntDestroyAiBoards,
        testRandomAi, testRandomAiBoards);

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
        Hunt Destroy AI won by destroying all ships of Random AI.""";
    assertEquals(expOut, appendable.toString());
  }

  /**
   * Runs a dummy session where player 2 wins
   * Additionally, confirms HuntDestroy superiority
   */
  @Test
  public void testGameRunnerAi2Win() {
    // Shots to a single position to cause loss of player
    resetForInputTest("6 6\n" + "2 2 1 1\n");
    runner = new TwoAisController(cliViewController,
        testRandomAi, testRandomAiBoards,
        testHuntDestroyAi, testHuntDestroyAiBoards);

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
        Hunt Destroy AI won by destroying all ships of Random AI.""";
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