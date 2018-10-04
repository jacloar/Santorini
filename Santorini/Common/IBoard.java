
import java.util.List;

/**
 * Represents the board in a game of Santorini. Allows the Workers to move and build.
 * Can create a grid of Buildings. Can add a worker to the list of workers.
 *
 * The Board has a 2D array of Buildings and a List of Workers.
 *
 * Changes in x occur in the north/south direction
 * Changes in y occur in the east/west direction
 */
public interface IBoard {

  /**
   * Moves the given worker to the position at its current x + dx and its current y + dy.
   *
   * If worker has posn (1, 2), x = 1, y = -1: New worker posn will be (2, 1)
   * If worker has posn (0, 1), x = -1, y = 1: Will throw error
   *
   * @param worker Worker to move
   * @param dx Change in x
   * @param dy Change in y
   * @throws IllegalArgumentException if worker is getting moved off the board.
   */
  void workerMove(Worker worker, int dx, int dy) throws IllegalArgumentException;

  /**
   * Builds one floor onto the Building at the worker's current x + dx and its current y + dy.
   *
   * If worker has posn (1, 2), x = 1, y = -1: Worker will build to tile (2, 1)
   * If worker has posn (0, 1), x = -1, y = 1: Will throw error
   *
   * @param worker Worker to build
   * @param dx Change in x
   * @param dy Change in y
   * @throws IllegalArgumentException if given invalid coordinates
   * @throws IllegalStateException if building height would become greater max height
   */
  void workerBuild(Worker worker, int dx, int dy) throws IllegalArgumentException, IllegalStateException;

  /**
   * Creates a worker at the given coordinates. Called during game set up when Players are
   * first placing their Workers.
   * x and y are in range [0, boardSize]
   *
   * If x = 1, y = 2: Will return new worker with posn (1, 2).
   * If x = 0, y = 0: Will return worker with posn (0, 0).
   * If x = -1, y = 2: Will throw exception.
   *
   * @param x x coordinate of where to put the worker
   * @param y y coordinate of where to put the worker
   * @return the created Worker so the player knows which workers are theirs
   * @throws IllegalArgumentException if given coordinates not on board
   */
  Worker placeWorker(int x, int y) throws IllegalArgumentException;

  /**
   * Returns a representation of the 2d array of buildings as heights.
   *
   * @return 2d array of integers representing building heights
   */
  int[][] getHeights();

  /**
   * Returns a representation of the list of workers as Posns
   *
   * @return List of Posns representing the workers
   */
  List<Posn> getWorkers();

  /**
   * Gets the height of the building at the given x and y
   * @param x x position on the grid
   * @param y y position on the grid
   * @return the height of the specified building.
   * @throws IllegalArgumentException if the specified coordinates are off the board.
   */
  int getHeightAt(int x, int y) throws IllegalArgumentException;
}