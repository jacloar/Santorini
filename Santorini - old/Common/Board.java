package Common;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents the board in a game of Santorini. Allows the Workers to
 * move and build.
 * Can create a grid of Buildings. Can add a worker to the list of workers.
 *
 * The Board has a rectangular 2D array of Buildings and a List of Workers.
 *
 * Positive changes in row move south
 * Positive changes in col move east
 */
public class Board implements IBoard {

  // The size of the buildings is a gridSize x gridSize square
  public static int gridSize = 6;
  // The max height Buildings can reach
  public static int maxHeight = 4;

  // Rectangular arrangement of Buildings representing the buildings
  private Building[][] buildings;
  private List<Worker> workers;

  /**
   * Creates a new empty board with no buildings and no workers
   */
  public Board() {
    this.buildings = createBuildings(gridSize);
    this.workers = new ArrayList<>(4);
  }

  /**
   * Creates a new board with buildings in the inputGrid and any given
   * workers.
   *
   * inputGrid does not need to be rectangular, however, it cannot exceed
   * gridSize x gridSize
   * ie, cannot have more than gridSize rows, and no row can have more
   * than gridSize elements.
   *
   * Unspecified cells are given new Building of height 0.
   * If any Building in the inputGrid is null, creates new Building of
   * height 0.
   *
   * @param inputGrid buildings to generate this buildings from
   * @param workers list of Workers
   */
  public Board(Building[][] inputGrid, List<Worker> workers) {
    if (inputGrid.length > gridSize) {
      throw new IllegalArgumentException(
          String.format("buildings size cannot be larger than %d", gridSize));
    }

    this.buildings = createBuildings(gridSize);

    updateBuildings(inputGrid);

    validateWorkers(workers);
    this.workers = workers;
  }

  /**
   * Ensures that the coordinates for every worker are on the board.
   *
   * @param inputWorkers workers to validate
   */
  private void validateWorkers(List<Worker> inputWorkers) {
    for (Worker w : inputWorkers) {
      validateCoordinates(w.getRow(), w.getCol());
    }
  }

  /**
   * Updates the buildings field to have the buildings from the
   * inputBuildings.
   *
   * @param inputBuildings buildings to generate this.buildings from.
   */
  private void updateBuildings(Building[][] inputBuildings) {
    for (int i = 0; i < inputBuildings.length; i += 1) {
      if (inputBuildings[i].length > gridSize) {
        throw new IllegalArgumentException(
            String.format(
                "buildings size cannot be larger than %d",
                gridSize
            ));
      }

      for (int j = 0; j < inputBuildings[i].length; j += 1) {
        if (inputBuildings[i][j] != null) {
          buildings[i][j] = inputBuildings[i][j];
        } else {
          buildings[i][j] = new Building(0);
        }
      }
    }
  }

  /**
   * Creates a size by size 2 dimensional array of Buildings with height 0.
   *
   * If given 6, will return a 6x6 array of Buildings
   *
   * @param size Size of the buildings
   */
  private Building[][] createBuildings(int size) {
    Building[][] grid = new Building[size][size];

    for (int i = 0; i < grid.length; i += 1) {
      for (int j = 0; j < grid[i].length; j += 1) {
        grid[i][j] = new Building();
      }
    }

    return grid;
  }


  @Override
  public void workerMove(Worker worker, int dRow, int dCol) {
    validateCoordinates(
        worker.getRow() + dRow,
        worker.getCol() + dCol
    );

    worker.moveBy(dRow, dCol);
  }

  @Override
  public void workerBuild(Worker worker, int dRow, int dCol)
      throws IllegalArgumentException, IllegalStateException {
    int x = worker.getRow() + dRow;
    int y = worker.getCol() + dCol;

    validateCoordinates(x, y);

    buildings[x][y].increaseHeight();
  }

  @Override
  public Worker placeWorker(int row, int col)
      throws IllegalArgumentException {
    validateCoordinates(row, col);

    Worker worker = new Worker(row, col);
    workers.add(worker);
    return worker;
  }

  @Override
  public int[][] getHeights() {
    int[][] intGrid = new int[gridSize][gridSize];

    for (int i = 0; i < intGrid.length; i += 1) {
      for (int j = 0; j < intGrid[i].length; j += 1) {
        intGrid[i][j] = buildings[i][j].getHeight();
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
  public int getHeightAt(int row, int col) throws IllegalArgumentException {
    validateCoordinates(row, col);
    return buildings[row][col].getHeight();
  }


  /**
   * Ensures that the given row and col coordinates are on the grid.
   * (within [0,5])
   *
   * If given (0, 3) will do nothing
   * If given (-1, 2) will throw error
   * If given (2, 6) will throw error
   *
   * @param row row coordinate to check
   * @param col col coordinate to check
   * @throws IllegalArgumentException if given row or col is not on the grid.
   */
  private void validateCoordinates(int row, int col)
      throws IllegalArgumentException {
    if (row >= buildings.length || row < 0) {
      throw new IllegalArgumentException(
          String.format("row must be within [0,%d]", buildings.length - 1));
    }

    if (col >= buildings[0].length || col < 0) {
      throw new IllegalArgumentException(
          String.format("col must be within [0,%d]", buildings[0].length - 1));
    }
  }
}