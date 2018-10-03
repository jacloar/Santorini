
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

  /**
   * Creates a new empty board with no buildings and no workers
   */
  public Board() {
    this.grid = createGrid(gridSize);
    this.workers = new ArrayList<>(4);
  }

  /**
   * Creates a new board with buildings in the inputGrid and any given workers.
   *
   * inputGrid does not need to be rectangular, however, it cannot exceed gridSize x gridSize
   * ie, cannot have more than gridSize rows, and no row can have more than gridSize elements.
   *
   * Unspecified cells are given new Building of height 0.
   * If any Building in the inputGrid is null, creates new Building of height 0.
   *
   * @param inputGrid grid to generate this grid from
   * @param workers list of Workers
   */
  public Board(Building[][] inputGrid, List<Worker> workers) {
    if (inputGrid.length > gridSize) {
      throw new IllegalArgumentException(String.format("grid size cannot be larger than %d", gridSize));
    }

    this.grid = createGrid(gridSize);

    for (int i = 0; i < inputGrid.length; i += 1) {
      if (inputGrid[i].length > gridSize) {
        throw new IllegalArgumentException(String.format("grid size cannot be larger than %d", gridSize));
      }

      for (int j = 0; j < inputGrid[i].length; j += 1) {
        if (inputGrid[i][j] != null) {
          grid[i][j] = inputGrid[i][j];
        } else {
          grid[i][j] = new Building(0);
        }
      }
    }

    this.workers = workers;
  }

  /**
   * Creates a size by size 2 dimensional array of Buildings with height 0.
   *
   * If given 6, will return a 6x6 array of Buildings
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
  public Worker placeWorker(int x, int y) throws IllegalArgumentException {
    validateCoordinates(x, y);

    Worker worker = new Worker(x, y);
    workers.add(worker);
    return worker;
  }

  @Override
  public int[][] getGrid() {
    int[][] intGrid = new int[gridSize][gridSize];

    for (int i = 0; i < intGrid.length; i += 1) {
      for (int j = 0; j < intGrid[i].length; j += 1) {
        intGrid[i][j] = grid[i][j].getHeight();
      }
    }

    return intGrid;
  }

  @Override
  public List<Posn> getWorkers() {
    ArrayList<Posn> workerPosns = new ArrayList<>(workers.size());

    for (Worker w : workers) {
      workerPosns.add(w.getPosn());
    }

    return workerPosns;
  }

  @Override
  public int getHeightAt(int x, int y) throws IllegalArgumentException {
    validateCoordinates(x,y);
    return grid[x][y].getHeight();
  }


  /**
   * Ensures that the given x and y coordinates are on the grid. (within [0,5])
   *
   * If given (0, 3) will do nothing
   * If given (-1, 2) will throw error
   * If given (2, 6) will throw error
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