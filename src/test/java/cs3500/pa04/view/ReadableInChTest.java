package cs3500.pa04.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests ReadableInCh and its methods
 */
class ReadableInChTest {
  static final String VALUE = "input";
  Readable readable;
  UiReader reader;

  /**
   * Initializes
   */
  @BeforeEach
  public void setUp() {
    this.readable = new BufferedReader(new StringReader(VALUE));
    this.reader = new ReadableInCh(this.readable);
  }

  /**
   * Tests successful run of write() in WriteImpl
   */
  @Test
  public void testSuccess() {
    assertEquals(VALUE, this.reader.read());
  }
}