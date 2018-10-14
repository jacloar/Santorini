package Common;

import java.util.List;

/**
 * Implementation of the rule checker
 */
public class Rules implements IRules {

  /**
   * Constructor for rules class.
   * Doesnt do anything.
   */
  public Rules() {

  }

  @Override
  public boolean isValidWorkerMove(int[][] heights, List<Posn> allWorkers, Posn workerPosn, int dRow, int dCol) {

    if (!isValidModification(heights, allWorkers, workerPosn, dRow, dCol)) {
      return false;
    }

    int toRow = workerPosn.getRow() + dRow;
    int toCol = workerPosn.getCol() + dCol;

    int currentHeight = heights[workerPosn.getRow()][workerPosn.getCol()];
    int toHeight = heights[toRow][toCol];

    // Verify that the building moving to is not more than 1 greater than current building
    return toHeight - currentHeight <= 1;
  }

  @Override
  public boolean isValidWorkerMove(IBoard board, Worker worker, int dRow, int dCol) {
    return isValidWorkerMove(board.getHeights(), board.getWorkers(), worker.getPosn(), dRow, dCol);
  }

  @Override
  public boolean isValidWorkerBuild(int[][] heights, List<Posn> allWorkers, Posn workerPosn, int dRow, int dCol) {

    return isValidModification(heights, allWorkers, workerPosn, dRow, dCol);

  }

  @Override
  public boolean isValidWorkerBuild(IBoard board, Worker worker, int dRow, int dCol) {
    return isValidWorkerBuild(board.getHeights(), board.getWorkers(), worker.getPosn(), dRow, dCol);
  }

  /**
   * Method to check similar requirements for isValidWorkerMove and isValidWorkerBuild
   *
   * @param heights heights for game being played
   * @param allWorkers positions of all workers on the board
   * @param workerPosn position of worker making change
   * @param dRow change in row
   * @param dCol change in col
   * @return true if proposal passes checks, false otherwise
   */
  private boolean isValidModification(int[][] heights, List<Posn> allWorkers, Posn workerPosn, int dRow, int dCol) {

    // Verify dRow in range [-1, 1]
    if (!(-1 <= dRow && dRow <= 1)) {
      return false;
    }

    // Verify dCol in range [-1, 1]
    if (!(-1 <= dCol && dCol <= 1)) {
      return false;
    }

    // Verify the total change is not 0
    if (dRow == 0 && dCol == 0) {
      return false;
    }

    int toRow = workerPosn.getRow() + dRow;
    int toCol = workerPosn.getCol() + dCol;

    if(!isEmptySpace(heights, allWorkers, toRow, toCol)) {
      return false;
    }

    int toHeight = heights[toRow][toCol];

    // Verify worker not modifying to building of max height
    return toHeight < Board.maxHeight;
  }

  private boolean isEmptySpace(int[][] heights, List<Posn> allWorkers, int row, int col) {
    // Verify row modifying is on board
    if (row < 0 || row >= Board.gridSize) {
      return false;
    }

    // Verify col modifying is on board
    if (col < 0 || col >= Board.gridSize) {
      return false;
    }

    // Verify that the position modifying is not occupied
    Posn toPosn = new Posn(row, col);
    for (Posn p : allWorkers) {
      if (p.samePosn(toPosn)) {
        return false;
      }
    }

    return heights[row][col] < 4;
  }

  @Override
  public boolean isValidPlaceWorker(int[][] heights, List<Posn> allWorkers, int row, int col) {
    return isEmptySpace(heights, allWorkers, row, col);
  }

  @Override
  public boolean isValidPlaceWorker(IBoard board, int row, int col) {
    return isValidPlaceWorker(board.getHeights(), board.getWorkers(), row, col);
  }

  @Override
  public boolean isGameOver(int[][] heights, List<Posn> allWorkers) {
    return false;
  }

  @Override
  public boolean didIWin(int[][] heights, List<Posn> allWorkers, List<Posn> myWorkers) {
    return false;
  }
}
