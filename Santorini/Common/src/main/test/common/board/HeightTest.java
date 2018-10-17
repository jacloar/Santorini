package common.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Tests for the Height class
 */
public class HeightTest {

  /**
   * Tests creating a new Height with height 0 has height 0
   */
  @Test
  public void testNewHeight0() {
    Height height = new Height(0);

    assertEquals(0, height.getHeight());
  }

  /**
   * Tests creating a new Height with height 3 has height 3
   */
  @Test
  public void testNewHeight4() {
    Height height = new Height(4);

    assertEquals(4, height.getHeight());
  }

  /**
   * Tests creating a new Height with height 5 has height 5
   */
  @Test
  public void testNewHeight5() {
    Height height = new Height(5);

    assertEquals(5, height.getHeight());
  }

  /**
   * Tests that isWorker returns false
   */
  @Test
  public void testIsWorkerFalse() {
    Height height = new Height(0);

    assertFalse(height.isWorker());
  }

  /**
   * Tests that copy makes a copy of the Height
   */
  @Test
  public void testCopy() {
    Height height = new Height(2);
    ICell copyHeight = height.copy();

    assertEquals(height.getHeight(), copyHeight.getHeight());
    assertEquals(height.isWorker(), copyHeight.isWorker());
    assertNotEquals(height, copyHeight);
  }
}
