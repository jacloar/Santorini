package Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the rule checker
 */
public class Rules implements IRules {

  /**
   * Constructor for rules class. Doesnt do anything.
   */
  public Rules() {

  }

  @Override
  public boolean isValidWorkerMove(int[][] heights, List<Posn> allWorkers, Posn workerPosn,
      int dRow, int dCol) {

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
  public boolean isValidWorkerBuild(int[][] heights, List<Posn> allWorkers, Posn workerPosn,
      int dRow, int dCol) {

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
  private boolean isValidModification(int[][] heights, List<Posn> allWorkers, Posn workerPosn,
      int dRow, int dCol) {

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

    if (!isEmptySpace(heights, allWorkers, toRow, toCol)) {
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
  public boolean isGameOver(int[][] heights, List<Posn> allWorkers, List<Posn> myWorkers) {
    if (isWorkerAtWinHeight(heights, allWorkers)) {
      return true;
    }

    if (!canAnyWorkersMove(heights, myWorkers, allWorkers)) {
      return true;
    }

    return false;
  }

  @Override
  public boolean isGameOver(IBoard board, List<Worker> myWorkers) {
    List<Posn> workerPosns = new ArrayList<>();

    for (Worker w : myWorkers) {
      workerPosns.add(w.getPosn());
    }

    return isGameOver(board.getHeights(), board.getWorkers(), workerPosns);
  }

  /**
   * Can any of my workers move?
   *
   * @param heights The heights of the current board
   * @param workersToCheck my workers
   * @param workersOnBoard all the workers on the board
   * @return True if my workers have a valid move, false otherwise
   */
  private boolean canAnyWorkersMove(int[][] heights, List<Posn> workersToCheck, List<Posn> workersOnBoard) {
    for (Posn p : workersToCheck) {
      if (canWorkerMove(heights, workersOnBoard, p)) {
        return true;
      }
    }

    return false;
  }

  /**
   *
   * Is the given worker at the specified height to win the game?
   *
   * @param heights The heights of the buildings on the board
   * @param workers The list of all the workers on the board
   * @return True if any worker is the set winning height
   */
  private boolean isWorkerAtWinHeight(int[][] heights, List<Posn> workers) {
    for (Posn p : workers) {
      if (heights[p.getRow()][p.getCol()] == Board.maxHeight - 1) {
        return true;
      }
    }
    return false;
  }


  @Override
  public boolean didIWin(int[][] heights, List<Posn> allWorkers, List<Posn> myWorkers) {
    // If the game is not over, player did not win
    if (!isGameOver(heights, allWorkers, myWorkers)) {
      return false;
    }

    // Check if any of the player's workers are at height 3
    if (isWorkerAtWinHeight(heights, myWorkers)) {
      return true;
    }

    // We know the game is over and none of the player's workers are at height 3, so player must have lost
    return false;
  }

  @Override
  public boolean didIWin(IBoard board, List<Worker> myWorkers) {
    List<Posn> workerPosns = new ArrayList<>();

    for (Worker w : myWorkers) {
      workerPosns.add(w.getPosn());
    }

    return didIWin(board.getHeights(), board.getWorkers(), workerPosns);
  }

  /**
   * Can the given worker make a valid move?
   *
   * @param heights heights of the buildings on the board
   * @param allWorkers all the workers on the board
   * @param workerPosn the posn of the worker to check
   * @return true if worker can move, false otherwise
   */
  private boolean canWorkerMove(int[][] heights, List<Posn> allWorkers, Posn workerPosn) {
    for (int dRow = -1; dRow <= 1; dRow += 1) {
      for (int dCol = -1; dCol <= 1; dCol += 1) {
        if (isValidWorkerMove(heights, allWorkers, workerPosn, dRow, dCol)) {
          return true;
        }
      }
    }

    return false;
  }
}
