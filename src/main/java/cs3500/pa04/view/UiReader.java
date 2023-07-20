package cs3500.pa04.view;

/**
 * Interface, representing a user interface input channel that can be
 * read from
 */
public interface UiReader {
  /**
   * Reads the contents of a message to a string
   *
   * @return the message contents
   */
  String read();
}
