package cs3500.pa04.view;

import java.io.IOException;

/**
 * Interface, representing a user interface output channel that can be
 * written to
 */
public interface UiWriter {
  /**
   * Writes a given message to the output channel
   *
   * @param value the content to write
   * @throws IOException when cannot write to the desired output channel
   */
  void write(String value) throws IOException;
}
