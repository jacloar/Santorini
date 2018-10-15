package Player;

import Common.Board;
import Common.Posn;
import java.util.List;

/**
 * IStrategy with the diagonal placement strategy and the stay alive movement strategy
 */
public class DiagonalAndStayAliveStrategy implements IStrategy {

  private IPlacementStrategy placement;
  private IMovementStrategy movement;

  /**
   * Construct a new DiagonalAndStayAliveStrategy
   *
   * @param rounds number of rounds to stay alive for
   */
  public DiagonalAndStayAliveStrategy(int rounds) {
    placement = new DiagonalPlacementStrategy();
    movement = new StayAliveMovementStrategy(rounds);
  }

  @Override
  public Posn placeWorker(int[][] heights, List<Posn> workersOnBoard, List<Posn> myWorkers) {
    return placement.placeWorker(Board.gridSize, workersOnBoard, myWorkers);
  }

  @Override
  public Move makeMove(int[][] heights, List<Posn> workersOnBoard, List<Posn> myWorkers) {
    return movement.makeMove(heights, workersOnBoard, myWorkers);
  }
}
