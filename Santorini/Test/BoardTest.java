import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the Board class
 */
public class BoardTest {

  /**
   * This tests makes sure that after we call placeWorker on an empty board
   * that it shows up at the given position.
   */
  @Test
  public void testPlaceWorker() {
    Board emptyBoard = new Board();
    Worker worker = emptyBoard.placeWorker(1,2);

    assertEquals(worker.getRow(), 1);
    assertEquals(worker.getCol(), 2);

  }
  /**
   * This tests makes sure that after we call placeWorker on an empty board
   * with invalid coordinates (off the board) that we get an Illegal argument
   * exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceWorkerOutOfBoundsXBig() {
    Board emptyBoard = new Board();
    emptyBoard.placeWorker(7,2);
  }
  /**
   * This tests makes sure that after we call placeWorker on an empty board
   * with invalid coordinates (off the board) that we get an Illegal argument
   * exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceWorkerOutOfBoundsYBig() {
    Board emptyBoard = new Board();
    emptyBoard.placeWorker(2,7);
  }
  /**
   * This tests makes sure that after we call placeWorker on an empty board
   * with invalid coordinates (off the board) that we get an Illegal argument
   * exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceWorkerOutOfBoundsXSmall() {
    Board emptyBoard = new Board();
    emptyBoard.placeWorker(-1,2);
  }
  /**
   * This tests makes sure that after we call placeWorker on an empty board
   * with invalid coordinates (off the board) that we get an Illegal argument
   * exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceWorkerOutOfBoundsYSmall() {
    Board emptyBoard = new Board();
    emptyBoard.placeWorker(2,-1);
  }

  /**
   * Tests that once a worker has been placed that it can be moved.
   */
  @Test
  public void testWorkerMove() {
    Board emptyBoard = new Board();
    Worker worker = emptyBoard.placeWorker(1,2);
    emptyBoard.workerMove(worker, -1, 1);
    assertEquals(worker.getRow(), 0);
    assertEquals(worker.getCol(), 3);
  }

  /**
   * Tests to make sure we get IllegalArgumentException when moving a worker
   * off the board.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testWorkerMoveOffBoard() {
    Board emptyBoard = new Board();
    Worker worker = emptyBoard.placeWorker(0,0);
    emptyBoard.workerMove(worker, -1, 1);
  }

  /**
   * Tests to make sure that a worker can perform a valid build operation on the board.
   */
  @Test
  public void testWorkerBuild() {
    Board emptyBoard = new Board();
    Worker worker = emptyBoard.placeWorker(1,2);
    assertEquals(emptyBoard.getHeightAt(2,2), 0);
    emptyBoard.workerBuild(worker, 1, 0);
    assertEquals(emptyBoard.getHeightAt(2,2),1);
  }

  /**
   * Tests that a worker building out of bounds throws an IllegalArgumentException
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidWorkerBuild() {
    Board emptyBoard = new Board();
    Worker worker = emptyBoard.placeWorker(0,2);
    emptyBoard.workerBuild(worker, -1, 0);
  }

  /**
   * Tests that the empty constructor creates an empty board.
   */
  @Test
  public void testEmptyConstructor() {
    Board emptyBoard = new Board();

    // Check that every building has height 0
    int[][] heights = emptyBoard.getHeights();
    for (int i = 0; i < heights.length; i += 1) {
      for (int j = 0; j < heights[i].length; j += 1) {
        assertEquals(heights[i][j], 0);
      }
    }

    // Check that there are no workers on the board
    assertEquals(emptyBoard.getWorkers().size(), 0);
  }
}
