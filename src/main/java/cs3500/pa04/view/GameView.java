package cs3500.pa04.view;

import java.io.IOException;
import java.util.Objects;

/**
 * Key View class of the project, manages in-session io
 */
public class GameView {
  private final UiReader inCh;
  private final UiWriter outCh;

  /**
   * Constructor for the CliGameView class
   *
   * @param input Readable source of characters to be translated
   * @param output Appendable translated output
   */
  public GameView(UiReader input, UiWriter output) {
    this.inCh = Objects.requireNonNull(input);
    this.outCh = Objects.requireNonNull(output);
  }

  /**
   * Displays a given string
   *
   * @param str to be printed to the output
   * @throws IOException if outCh.write fails, handled by controller
   */
  public void display(String str) throws IOException {
    outCh.write(str);
  }

  /**
   * Gets a single line of user input to be parsed by controller
   *
   * @return String read from input channel
   */
  public String getLineOfInput() {
    return inCh.read();
  }
}
