package cs3500.pa04.view;

import java.io.IOException;
import java.util.Objects;

/**
 * Implementation of the UiWriter interface that will act as an appendable output channel
 */
public class AppendableOutCh implements UiWriter {
  private final Appendable appendable;

  /**
   * Constructor of the AppendableOutCh class
   *
   * @param appendable output to which the translated output will be written
   */
  public AppendableOutCh(Appendable appendable) {
    this.appendable = Objects.requireNonNull(appendable);
  }

  /**
   * Writes a given message to the output channel
   *
   * @param value the content to write
   */
  @Override
  public void write(String value) throws IOException {
    try {
      appendable.append(value);
    } catch (IOException e) {
      System.err.println("Failed to write to the output channel, cannot continue.");
      throw e;
    }
  }
}
