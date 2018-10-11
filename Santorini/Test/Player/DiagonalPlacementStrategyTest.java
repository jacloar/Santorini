package Player;

import static org.junit.Assert.assertTrue;

import Common.Board;
import Common.IBoard;
import Common.Posn;
import java.util.ArrayList;
import org.junit.Test;

/**
 * Tests the DiagonalPlacementStrategy class
 */
public class DiagonalPlacementStrategyTest {

  /**
   * Tests that the strategy will place a worker at (0,0) if given an empty board.
   */
  @Test
  public void testEmptyBoard() {
    IPlacementStrategy strategy = new DiagonalPlacementStrategy();

    IBoard board = new Board();

    Posn placement = strategy.placeWorker(6, board.getWorkers(), new ArrayList<>());

    assertTrue(placement.samePosn(0, 0));
  }

  /**
   * Tests that the strategy will place a worker at (2,2) if there are workers at (0,0) and (1,1)
   */
  @Test
  public void testBoardWithWorkersOnDiagonal() {
    IPlacementStrategy strategy = new DiagonalPlacementStrategy();

    IBoard board = new Board();

    board.placeWorker(0, 0);
    board.placeWorker(1, 1);
    board.placeWorker(3, 3);

    Posn placement = strategy.placeWorker(6, board.getWorkers(), new ArrayList<>());

    assertTrue(placement.samePosn(2, 2));
  }

  /**
   * Tests that the strategy will place a worker at (0,0) with workers already at (1,0), (0,1), and (4,3)
   */
  @Test
  public void testBoardWithWorkersNotOnDiagonal() {
    IPlacementStrategy strategy = new DiagonalPlacementStrategy();

    IBoard board = new Board();

    board.placeWorker(1, 0);
    board.placeWorker(0, 1);
    board.placeWorker(4, 3);

    Posn placement = strategy.placeWorker(6, board.getWorkers(), new ArrayList<>());

    assertTrue(placement.samePosn(0, 0));
  }

  /**
   * Tests that strategy throws an exception if there are no spaces left on the diagonal
   */
  @Test(expected = IllegalArgumentException.class)
  public void testStragegyThrowsException() {
    IPlacementStrategy strategy = new DiagonalPlacementStrategy();

    IBoard board = new Board();

    board.placeWorker(0, 0);
    board.placeWorker(1, 1);

    strategy.placeWorker(2, board.getWorkers(), new ArrayList<>());
  }
}
