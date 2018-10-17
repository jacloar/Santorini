package Player;

import static org.junit.Assert.assertTrue;

import Common.Board;
import Common.IBoard;
import Common.Posn;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.Test;

/**
 * Tests the MaxDistancePlacementStrategy class
 */
public class MaxDistancePlacementStrategyTest {

  /**
   * Tests that the strategy will place a worker at (0,0) if the board is empty.
   */
  @Test
  public void testEmptyBoard() {
    IPlacementStrategy strategy = new MaxDistancePlacementStrategy();

    IBoard board = new Board();

    Posn placement = strategy.placeWorker(6, board.getWorkers(), new ArrayList<>());

    assertTrue(placement.samePosn(0, 0));
  }

  /**
   * Tests that the strategy will place a worker at (5,5) if there is one worker at (0,0)
   */
  @Test
  public void testWorkerInCorner() {
    IPlacementStrategy strategy = new MaxDistancePlacementStrategy();

    IBoard board = new Board();
    board.placeWorker(0, 0);

    Posn placement = strategy.placeWorker(6, board.getWorkers(), new ArrayList<>());

    assertTrue(placement.samePosn(5, 5));
  }

  /**
   * Tests that the strategy will place a worker at (5,0) if there are workers at (0,0), (0,5)
   */
  @Test
  public void testWorkersAlongEdge() {
    IPlacementStrategy strategy = new MaxDistancePlacementStrategy();

    IBoard board = new Board();
    board.placeWorker(0, 0);
    board.placeWorker(0, 5);

    Posn placement = strategy.placeWorker(6, board.getWorkers(), new ArrayList<>());

    assertTrue(placement.samePosn(5, 0));
  }

  /**
   * Tests that the strategy will place a worker at (5,0) if there are workers at (0,0), (0,5)
   */
  @Test
  public void testWorkersInAllCorners() {
    IPlacementStrategy strategy = new MaxDistancePlacementStrategy();

    IBoard board = new Board();
    board.placeWorker(0, 0);
    board.placeWorker(0, 5);
    board.placeWorker(5, 5);
    board.placeWorker(5, 0);

    Posn placement = strategy.placeWorker(6, board.getWorkers(), new ArrayList<>());

    assertTrue(placement.samePosn(0, 1));
  }

  /**
   * Tests that the strategy will place worker at (5, 5) if opponent worker at (0, 0) and own worker at (0, 5)
   */
  @Test
  public void testOpponentAndOwnWorkers() {
    IPlacementStrategy strategy = new MaxDistancePlacementStrategy();

    IBoard board = new Board();
    board.placeWorker(0, 0);
    board.placeWorker(0, 5);

    Posn placement = strategy.placeWorker(6, board.getWorkers(), Collections.singletonList(new Posn(0, 5)));

    assertTrue(placement.samePosn(5, 5));
  }
}
