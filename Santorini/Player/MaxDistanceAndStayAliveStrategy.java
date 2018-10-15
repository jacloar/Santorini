package Player;

import Common.Board;
import Common.Posn;
import java.util.List;

/**
 * IStrategy that uses the MaxDistance placement strategy and the StayAlive movement strategy.
 */
public class MaxDistanceAndStayAliveStrategy implements IStrategy {

  private IPlacementStrategy placement;
  private IMovementStrategy movement;

  /**
   * Construct a new MaxDistanceAndStayAliveStrategy
   *
   * @param rounds number of rounds to stay alive for
   */
  public MaxDistanceAndStayAliveStrategy(int rounds) {
    placement = new MaxDistancePlacementStrategy();
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
