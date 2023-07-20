package cs3500.pa04.model.player;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa04.controller.lockbox.UserShotsLockbox;
import cs3500.pa04.model.GameResult;
import cs3500.pa04.model.game.Coord;
import cs3500.pa04.model.game.PlayerBoards;
import cs3500.pa04.model.game.ShipType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests the LocalHumanPlayer class along with AbstractLocalPlayer methods
 */
class HumanPlayerTest {
  static final String PLAYER_NAME = "TEST PLAYER";
  static final int SEED = 413;
  static final int BOARD_HEIGHT = 7;
  static final int BOARD_WIDTH = 6;
  static HumanPlayer testPlayer;
  static UserShotsLockbox lockbox;

  /**
   * Initializes ship specs to test LocalHumanPlayerTest
   */
  @BeforeAll
  public static void setup() {
    Map<ShipType, Integer> specs = new HashMap<>(Map.of(
        ShipType.CARRIER, 2,
        ShipType.BATTLESHIP, 2,
        ShipType.DESTROYER, 1,
        ShipType.SUBMARINE, 1));

    PlayerBoards testBoards = new PlayerBoards(SEED);
    lockbox = new UserShotsLockbox();
    testPlayer = new HumanPlayer(PLAYER_NAME, testBoards, lockbox);
    testPlayer.setup(BOARD_HEIGHT, BOARD_WIDTH, specs);
  }

  /**
   * Tests Human Player's lockbox as an input source
   */
  @Test
  public void testTakeShotsViaLockbox() {
    assertEquals(PLAYER_NAME, testPlayer.name());

    ArrayList<Coord> testShots = new ArrayList<>(List.of(
        new Coord(0, 0), new Coord(0, 1), new Coord(0, 2)
    ));
    lockbox.recieveUserShots(testShots);

    assertArrayEquals(testShots.toArray(), testPlayer.takeShots().toArray());
  }

  /**
   * Mock tests endGame for code coverage
   */
  @Test
  public void testEndGame() {
    testPlayer.endGame(GameResult.WIN, "Not a test because is unimplemented in PA03");
  }

}