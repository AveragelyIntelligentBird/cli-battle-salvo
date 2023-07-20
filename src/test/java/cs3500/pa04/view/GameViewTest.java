package cs3500.pa04.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests GameView class
 */
class GameViewTest {
  static final String VALUE = "input";
  static Appendable appendable;
  static GameView gameView;

  /**
   * Initializes GameView
   */
  @BeforeAll
  public static void setup() {
    Readable readable = new BufferedReader(new StringReader(VALUE));
    UiReader reader = new ReadableInCh(readable);

    appendable = new StringBuilder();
    UiWriter writer = new AppendableOutCh(appendable);

    gameView = new GameView(reader, writer);
  }

  /**
   * Tests GameView's display and getLineOfInput methods
   */
  @Test
  public void testGameView() {
    assertEquals("", appendable.toString());
    try {
      gameView.display(VALUE);
    } catch (IOException e) {
      fail();
    }
    assertEquals(VALUE, appendable.toString());

    assertEquals(VALUE, gameView.getLineOfInput());
  }
}