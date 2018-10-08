package Player;

import Common.Posn;
import java.util.List;

/**
 * Interface for the strategy class. Makes decisions about player moves
 * based on the current state of the board.
 */
public interface IStrategy {
  // The strategy knows the dimensions of the board
  // The strategy knows the target height to win the game
  // The strategy knows the maximum height for a building.

  /**
   * Determines where to place a new worker based on the current state of the board.
   *
   * @param heights the heights of all the Buildings on the board
   * @param workersOnBoard a list of the positions of all the workers on the board
   * @param myWorkers a list of the positions of the workers, if any, belonging to the player
   * @return Posn representing the position to place a new worker
   */
  Posn placeWorker(int[][] heights, List<Posn> workersOnBoard, List<Posn> myWorkers);


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
