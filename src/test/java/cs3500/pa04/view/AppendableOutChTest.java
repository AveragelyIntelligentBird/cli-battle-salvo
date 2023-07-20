package cs3500.pa04.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests AppendableOutCh and its methods
 */
class AppendableOutChTest {
  static final String VALUE = "input";
  Appendable appendable;
  UiWriter writer;

  /**
   * Initializes
   */
  @BeforeEach
  public void setUp() {
    this.appendable = new StringBuilder();
    this.writer = new AppendableOutCh(this.appendable);
  }

  /**
   * Tests successful run of write() in WriteImpl
   */
  @Test
  public void testSuccess() {
    assertEquals("", this.appendable.toString());
    try {
      this.writer.write(VALUE);
    } catch (IOException e) {
      fail();
    }

    assertEquals(VALUE, this.appendable.toString());
  }

  /**
   * Tests unsuccessful run of write() in WriteImpl
   */
  @Test
  public void testFailure() {
    this.writer = new AppendableOutCh(new MockAppendable());
    Exception exc = assertThrows(IOException.class,
        () -> this.writer.write(VALUE), "Mock throwing an error");
    assertEquals("Mock throwing an error", exc.getMessage());
  }
}