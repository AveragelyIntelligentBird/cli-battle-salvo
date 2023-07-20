package cs3500.pa04.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cs3500.pa04.controller.lockbox.UserShotsLockbox;
import cs3500.pa04.model.game.Coord;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests UserShotsLockbox
 */
class UserShotsLockboxTest {
  @Test
  public void testUserShotsLockbox() {
    UserShotsLockbox lockbox = new UserShotsLockbox();
    assertThrows(IllegalStateException.class, lockbox::reportUserShots);

    ArrayList<Coord> testShots = new ArrayList<>(List.of(
        new Coord(0, 0),
        new Coord(1, 0),
        new Coord(2, 0)
    ));
    lockbox.recieveUserShots(testShots);
    ArrayList<Coord> prodShots = lockbox.reportUserShots();
    assertArrayEquals(testShots.toArray(), prodShots.toArray());
  }

}