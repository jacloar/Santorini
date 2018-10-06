import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests that Worker behaves as desired
 */
public class WorkerTest {

  /**
   * Tests that getRow returns the correct row for the worker
   */
  @Test
  public void testGetRow() {
    Worker w = new Worker(3, 2);

    assertEquals(w.getRow(), 3);
  }

  /**
   * Tests that getRow returns the correct row for the worker
   */
  @Test
  public void testGetRow2() {
    Worker w = new Worker(0, 5);

    assertEquals(w.getRow(), 0);
  }

  /**
   * Tests that getCol returns the correct col for the worker
   */
  @Test
  public void testGetCol() {
    Worker w = new Worker(3, 2);

    assertEquals(w.getCol(), 2);
  }

  /**
   * Tests that getCol returns the correct col for the worker
   */
  @Test
  public void testGetCol2() {
    Worker w = new Worker(0, 5);

    assertEquals(w.getCol(), 5);
  }

  /**
   * Tests that getPosn returns a posn with row/col equivalent to that given to the worker
   */
  @Test
  public void testGetPosn() {
    Worker w = new Worker(3, 2);

    Posn p = w.getPosn();
    assertEquals(p.getRow(), 3);
    assertEquals(p.getCol(), 2);
  }

  /**
   * Tests that getPosn returns a posn with row/col equivalent to that given to the worker
   */
  @Test
  public void testGetPosn2() {
    Worker w = new Worker(0, 5);

    Posn p = w.getPosn();
    assertEquals(p.getRow(), 0);
    assertEquals(p.getCol(), 5);
  }

  /**
   * Tests that the Posn from getPosn has the same position as that given to the worker
   */
  @Test
  public void testPosnConstructor() {
    Posn p = new Posn(3, 2);
    Worker w = new Worker(p);

    assertTrue(p.samePosn(w.getPosn()));
  }

  /**
   * Tests that hasPosn returns true if the workers position is given
   */
  @Test
  public void testHasPosn() {
    Worker w = new Worker(3, 2);

    assertTrue(w.hasPosn(3, 2));
  }

  /**
   * Tests that hasPosn returns true if the workers position is given
   */
  @Test
  public void testHasPosn2() {
    Worker w = new Worker(0, 5);

    assertTrue(w.hasPosn(0, 5));
  }

  /**
   * Tests that hasPosn returns false if the workers position is not given
   */
  @Test
  public void testHasPosnFalse() {
    Worker w = new Worker(3, 2);

    assertFalse(w.hasPosn(0, 5));
  }

  /**
   * Tests that moveBy moves the worker in the desired direction
   */
  @Test
  public void testMoveBy() {
    Posn p = new Posn(3, 2);
    Worker w = new Worker(p);

    assertTrue(p.samePosn(w.getPosn()));

    w.moveBy(-1, 1);

    assertFalse(p.samePosn(w.getPosn()));
    assertEquals(w.getRow(), 2);
    assertEquals(w.getCol(), 3);
  }

  /**
   * Tests that moveBy moves the worker in the desired direction
   */
  @Test
  public void testMoveBy3() {
    Posn p = new Posn(3, 2);
    Worker w = new Worker(p);

    assertTrue(p.samePosn(w.getPosn()));

    w.moveBy(1, 0);

    assertFalse(p.samePosn(w.getPosn()));
    assertEquals(w.getRow(), 4);
    assertEquals(w.getCol(), 2);
  }
}
