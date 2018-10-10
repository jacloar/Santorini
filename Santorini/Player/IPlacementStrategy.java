package Player;

import java.util.List;

import Common.Posn;

/**
 * Interface for a players placement strategy. A player may wish to mix and match their
 * placement strategies and their moving strategies. When players are placing workers
 * we assume that the board is all heights of 0.
 */
public interface IPlacementStrategy {

  // The strategy knows the dimensions of the board


  /**
   * Determines where to place a new worker based on the current state of the board.
   *
   * @param size the dimensions for the board
   * @param workersOnBoard a list of the positions of all the workers on the board
   * @param myWorkers a list of the positions of the workers, if any, belonging to the player
   * @return Posn representing the position to place a new worker
   */
  Posn placeWorker(int size, List<Posn> workersOnBoard, List<Posn> myWorkers);

}
