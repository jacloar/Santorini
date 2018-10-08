package Common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests that the Posn class behaves as expected
 */
public class PosnTest {

  /**
   * Tests that getRow returns the expected row.
   */
  @Test
  public void testGetRow() {
    Posn p = new Posn(4, 7);

    assertEquals(p.getRow(), 4);
  }

  /**
   * Tests that getCol returns the expected col.
   */
  @Test
  public void testGetCol() {
    Posn p = new Posn(4, 7);

    assertEquals(p.getCol(), 7);
  }

  /**
   * Tests that samePosn is true if given a Posn with the same row/col.
   */
  @Test
  public void testSamePosnTrue() {
    Posn p = new Posn(4, 7);

    assertTrue(p.samePosn(new Posn(4, 7)));
  }

  /**
   * Tests that samePosn is false if given a Posn with a different row.
   */
  @Test
  public void testSamePosnFalseRow() {
    Posn p = new Posn(4, 7);

    assertFalse(p.samePosn(new Posn(3, 7)));
  }

  /**
   * Tests that samePosn is false if given a Posn with a different col.
   */
  @Test
  public void testSamePosnFalseCol() {
    Posn p = new Posn(4, 7);

    assertFalse(p.samePosn(new Posn(4, 6)));
  }
}
