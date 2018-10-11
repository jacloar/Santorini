package Player;

import Common.Posn;
import java.util.List;

/**
 * Interface for a player's movement strategy. The movement includes both the selected worker moving
 * and then the selected worker building. We assume all workers have already been placed.
 */
public interface IMovementStrategy {

  /**
   * Determines which worker to move, where to move them, and where to build (if not at target
   * height already).
   *
   * @param heights the heights of all the Buildings on the board
   * @param workersOnBoard a list of the positions of all the workers on the board
   * @param myWorkers a list of the positions of the workers, if any, belonging to the player
   * @return Move representing how the player should move
   */
  Move makeMove(int[][] heights, List<Posn> workersOnBoard, List<Posn> myWorkers);
}
