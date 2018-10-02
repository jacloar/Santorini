import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardTest {

  /**
   * This tests makes sure that after we call placeWorker on an empty board
   * that it shows up at the given position.
   */
  @Test
  public void testPlaceWorker() {
    Board emptyBoard = new Board();
    Worker worker = emptyBoard.placeWorker(1,2);

    assertEquals(worker.getX(), 1);
    assertEquals(worker.getY(), 2);

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
    assertEquals(worker.getX(), 0);
    assertEquals(worker.getY(), 3);
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

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidWorkerBuild() {
    Board emptyBoard = new Board();
    Worker worker = emptyBoard.placeWorker(0,2);
    emptyBoard.workerBuild(worker, -1, 0);
  }
}
