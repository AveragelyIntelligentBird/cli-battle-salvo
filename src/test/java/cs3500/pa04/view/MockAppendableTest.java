package cs3500.pa04.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Tests MockAppendable and its methods
 */
class MockAppendableTest {
  static final String VALUE = "input";

  /**
   * Tests unsuccessful uses of MockAppendable
   */
  @Test
  public void testMockFailure() {
    Appendable mockAppendable = new MockAppendable();

    Exception exc = assertThrows(IOException.class,
        () -> mockAppendable.append(VALUE), "Mock throwing an error");
    assertEquals("Mock throwing an error", exc.getMessage());

    exc = assertThrows(IOException.class,
        () -> mockAppendable.append(VALUE, 0, 1), "Mock throwing an error");
    assertEquals("Mock throwing an error", exc.getMessage());

    exc = assertThrows(IOException.class,
        () -> mockAppendable.append('i'), "Mock throwing an error");
    assertEquals("Mock throwing an error", exc.getMessage());
  }
}