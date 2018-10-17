import java.util.List;

/**
 * Represents the board in a game of Santorini. Allows the Workers to move and build.
 * Can create a grid of Buildings. Can add a worker to the list of workers.
 *
 * The Board has a 2D array of Buildings and a List of Workers.
 */
interface iBoard {

  // Fields:
  //   2D array of Buildings
  //   List of Workers

  /**
   * Creates a size by size 2 dimensional array of Buildings with height 0.
   * **This method should be private, but we think it is important and wanted to include it here**
   *
   * @param size Size of the grid
   */
  Building[][] createGrid(int size);

  /**
   * Moves the given worker to the position at its current x + dx and its current y + dy.
   *
   * @param worker Worker to move
   * @param dx Change in x
   * @param dy Change in y
   */
  void workerMove(Worker worker, int dx, int dy);

  /**
   * Builds one floor onto the Building at the worker's current x + dx and its current y + dy.
   *
   * @param worker Worker to build
   * @param dx Change in x
   * @param dy Change in y
   */
  void workerBuild(Worker worker, int dx, int dy);

  /**
   * Places the given worker at the specified position.
   * Called during game set up when Players are first placing their Workers.
   *
   * @param worker Worker to add to the Board
   * @param x x coordinate of where to put the worker
   * @Param y y coordinate of where to put the worker
   */
  void placeWorker(Worker worker, int x, int y);

  /**
   * Returns a copy of the 2 dimensional array of Buildings.
   *
   * @return 2d array of Buildings
   */
  Building[][] getGrid();

  /**
   * Returns a copy of the list of Workers.
   *
   * @return list of workers
   */
  List<Worker> getWorkers();
}

/* DATA DEFINITIONS */

/**
 * A Worker has a position.
 * It can move in 8 directions: N, NE, E, SE, S, SW, W, or NW by increments of 1.
 */
/*class Worker {

  int x;
  int y;

}*/

/**
 * A Building has a height.
 * Buildings can increase their height from 0 up to 4 by increments of 1.
 */
/*class Building {

  int height;

}*/