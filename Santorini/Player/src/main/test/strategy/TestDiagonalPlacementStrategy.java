package strategy;

import common.board.Board;
import common.board.IBoard;
import common.data.PlaceWorkerAction;
import org.junit.Test;

public class TestDiagonalPlacementStrategy {

  /**
   * Tests that the diagonal placement strategy does not throw an error when placing 4 workers
   */
  @Test
  public void testDiagonalPlacementStrategyDoenstError() {
    IPlacementStrategy strategy = new DiagonalPlacementStrategy();

    IBoard board = new Board();

    for (int i = 0; i < 4; i += 1) {
      PlaceWorkerAction action = strategy.getPlaceWorker(board);
      board.createWorker("playerName", action.getRow(), action.getColumn());
    }
  }
}
