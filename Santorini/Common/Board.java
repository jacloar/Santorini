package Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the board in a game of Santorini. Allows the Workers to move and build.
 * Can create a grid of Buildings. Can add a worker to the list of workers.
 *
 * The Board has a rectangular 2D array of Buildings and a List of Workers.
 */
public class Board implements IBoard{

  // The size of the grid is a gridSize x gridSize square
  public static int gridSize = 6;
  // The max height Buildings can reach
  public static int maxHeight = 4;

  // Rectangular arrangement of Buildings representing the grid
  private Building[][] grid;
  private List<Worker> workers;

  public Board() {
    this.grid = createGrid(gridSize);
    this.workers = new ArrayList<>(4);
  }

  /**
   * Creates a size by size 2 dimensional array of Buildings with height 0. **This method should be
   * private, but we think it is important and wanted to include it here**
   *
   * @param size Size of the grid
   */
  private Building[][] createGrid(int size) {
    Building[][] grid = new Building[size][size];

    for (int i = 0; i < grid.length; i += 1) {
      for (int j = 0; j < grid[i].length; j += 1) {
        grid[i][j] = new Building();
      }
    }

    return grid;
  }


  @Override
  public void workerMove(Worker worker, int dx, int dy) {
    validateCoordinates(worker.getX() + dx, worker.getY() + dy);

    worker.moveBy(dx, dy);
  }

  @Override
  public void workerBuild(Worker worker, int dx, int dy) throws IllegalArgumentException, IllegalStateException {
    int x = worker.getX() + dx;
    int y = worker.getY() + dy;

    validateCoordinates(x, y);

    grid[x][y].increaseHeight();
  }

  @Override
  public Worker placeWorker(int x, int y) {
    validateCoordinates(x, y);

    Worker worker = new Worker(x, y);
    workers.add(worker);
    return worker;
  }

  @Override
  public Building[][] getGrid() {
    return grid;
  }

  @Override
  public List<Worker> getWorkers() {
    return workers;
  }


  /**
   * Ensures that the given x and y coordinates are on the grid. (within [0,6])
   *
   * @param x x coordinate to check
   * @param y y coordinate to check
   * @throws IllegalArgumentException if given x or y is not on the grid.
   */
  private void validateCoordinates(int x, int y) throws IllegalArgumentException {
    if (x >= grid.length || x < 0) {
      throw new IllegalArgumentException(String.format("x coordinate must be within [0,%d]", grid.length - 1));
    }

    if (y >= grid[0].length || y < 0) {
      throw new IllegalArgumentException(String.format("y coordinate must be within [0,%d]", grid[0].length - 1));
    }
  }
}