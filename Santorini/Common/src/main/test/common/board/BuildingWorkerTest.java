package common.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BuildingWorkerTest {

  /**
   * Tests that a new worker has all the given attributes
   */
  @Test
  public void testNewWorker() {
    BuildingWorker worker = new BuildingWorker("one", 1, 0);

    assertTrue(worker.isWorker());

    assertEquals("one", worker.getPlayerName());
    assertEquals(1, worker.getWorkerNumber());
    assertEquals(0, worker.getHeight());
  }

  /**
   * Tests that a new worker has all given attributes for a different worker
   */
  @Test
  public void testNewWorker2() {
    BuildingWorker worker = new BuildingWorker("two", 2, 3);

    assertTrue(worker.isWorker());

    assertEquals("two", worker.getPlayerName());
    assertEquals(2, worker.getWorkerNumber());
    assertEquals(3, worker.getHeight());
  }

  /**
   * Tests that copy copies the BuildingWorker
   */
  @Test
  public void testCopy() {
    BuildingWorker worker = new BuildingWorker("one", 1, 2);
    ICell copy = worker.copy();

    assertTrue(copy.isWorker());

    assertEquals("one", copy.getPlayerName());
    assertEquals(1, copy.getWorkerNumber());
    assertEquals(2, copy.getHeight());

    assertNotEquals(worker, copy);
  }
}
