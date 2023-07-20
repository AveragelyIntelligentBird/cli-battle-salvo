package cs3500.pa04.model.player;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
 * Tests RandomAiPlayer
 */
class RandomAiPlayerTest {
  static final String PLAYER_NAME = "TEST PLAYER";
  static final int SEED = 413;
  static final int BOARD_HEIGHT = 7;
  static final int BOARD_WIDTH = 6;
  static ArrayList<Coord> submarineHitCoords;
  static Player testAi;
  Coord coord1;
  Coord coord2;
  Coord coord3;
  Coord coord4;
  Coord coord5;
  Coord coord6;

  /**
   * Initializes ship specs to test RandomAiPlayer
   */
  @BeforeAll
  public static void setup() {
    submarineHitCoords = new ArrayList<>(List.of(
        new Coord(4, 0), new Coord(4, 1), new Coord(4, 2)
    ));

    Map<ShipType, Integer> specs = new HashMap<>(Map.of(
        ShipType.CARRIER, 2,
        ShipType.BATTLESHIP, 2,
        ShipType.DESTROYER, 1,
        ShipType.SUBMARINE, 1));
    testAi = new RandomAiPlayer(PLAYER_NAME, new PlayerBoards(SEED), SEED);
    testAi.setup(BOARD_HEIGHT, BOARD_WIDTH, specs);
  }

  /**
   * Tests RandomAiPlayer's constructor and takeShots()
   */
  @Test
  public void testConstructorAndShots() {
    ArrayList<Coord> testAiShots = (ArrayList<Coord>) testAi.takeShots();
    assertEquals(6, testAiShots.size());

    coord1 = new Coord(5, 2);
    coord2 = new Coord(0, 6);
    coord3 = new Coord(5, 0);
    coord4 = new Coord(1, 4);
    coord5 = new Coord(1, 3);
    coord6 = new Coord(2, 3);
    ArrayList<Coord> expShots = new ArrayList<>(
        List.of(coord1, coord2, coord3, coord4, coord5, coord6));
    assertArrayEquals(expShots.toArray(), testAiShots.toArray());

    // Number of shots correspond to the number of surviving ships
    testAi.reportDamage(submarineHitCoords);
    testAiShots = (ArrayList<Coord>) testAi.takeShots();
    assertEquals(5, testAiShots.size());

    coord1 = new Coord(4, 6);
    coord2 = new Coord(3, 1);
    coord3 = new Coord(0, 0);
    coord4 = new Coord(1, 0);
    coord5 = new Coord(4, 0);
    expShots = new ArrayList<>(
        List.of(coord1, coord2, coord3, coord4, coord5));
    assertArrayEquals(expShots.toArray(), testAiShots.toArray());

    // If fewer unmarked cells than ships, shoot to every remaining cell
    ArrayList<Coord> shotsToEveryCellExceptTwo = new ArrayList<>();
    for (int i = 0; i < BOARD_HEIGHT; i++) {
      for (int j = 0; j < BOARD_WIDTH; j++) {
        shotsToEveryCellExceptTwo.add(new Coord(j, i));
      }
    }
    // Shots that haven't been yet fired
    coord1 = new Coord(1, 1);
    coord2 = new Coord(1, 2);

    shotsToEveryCellExceptTwo.remove(coord1);
    shotsToEveryCellExceptTwo.remove(coord2);
    testAi.successfulHits(shotsToEveryCellExceptTwo);
    testAiShots = (ArrayList<Coord>) testAi.takeShots();
    assertEquals(2, testAiShots.size());

    expShots = new ArrayList<>(List.of(coord1, coord2));
    assertArrayEquals(expShots.toArray(), testAiShots.toArray());
  }
}