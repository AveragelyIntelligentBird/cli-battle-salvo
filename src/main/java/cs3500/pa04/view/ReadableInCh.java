package cs3500.pa04.view;

import java.util.Objects;
import java.util.Scanner;

/**
 * Implementation of the UiReader interface that will act a readable an input channel
 */
public class ReadableInCh implements UiReader {
  private final Scanner scanner;

  /**
   * Constructs the ReadableInCh object
   *
   * @param readable from which the input will be read
   */
  public ReadableInCh(Readable readable) {
    Readable readableInput = Objects.requireNonNull(readable);
    this.scanner = new Scanner(readableInput);
  }

  /**
   * Reads the contents of a message to a string
   *
   * @return the message contents
   */
  @Override
  public String read() {
    return scanner.nextLine();
  }
}
