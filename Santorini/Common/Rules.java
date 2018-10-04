import java.util.List;

/**
 * Implementation of the rule checker
 */
public class Rules implements IRules {

  // Stores the worker that most recently moved.
  private Worker lastMoved;

  @Override
  public boolean isValidWorkerMove(IBoard board, Worker worker, int dRow, int dCol) {

    if (!isValidModification(board, worker, dRow, dCol)) {
      return false;
    }

    int toRow = worker.getRow() + dRow;
    int toCol = worker.getCol() + dCol;

    int currentHeight = board.getHeightAt(worker.getRow(), worker.getCol());
    int toHeight = board.getHeightAt(toRow, toCol);

    // Verify worker not moving to building of max height
    if (toHeight == Board.maxHeight) {
      return false;
    }

    // Verify that the building moving to is not more than 1 greater than current building
    if (toHeight - currentHeight > 1) {
      return false;
    }

    // Marks this worker as the worker that just moved
    this.lastMoved = worker;

    return true;
  }

  @Override
  public boolean isValidWorkerBuild(IBoard board, Worker worker, int dRow, int dCol) {

    // Verifies that the worker building is the same worker that just moved.
    if (!worker.equals(lastMoved)) {
      return false;
    }

    return isValidModification(board, worker, dRow, dCol);

  }

  /**
   * Method to check similar requirements for isValidWorkerMove and isValidWorkerBuild
   *
   * @param board IBoard for game being played
   * @param worker worker making change
   * @param dRow change in row
   * @param dCol change in col
   * @return true if proposal passes checks, false otherwise
   */
  private boolean isValidModification(IBoard board, Worker worker, int dRow, int dCol) {

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

    int toRow = worker.getRow() + dRow;
    int toCol = worker.getCol() + dCol;

    return isEmptySpace(board, toRow, toCol);
  }

  private boolean isEmptySpace(IBoard board, int row, int col) {
    // Verify row modifying is on board
    if (row < 0 || row > Board.gridSize) {
      return false;
    }

    // Verify col modifying is on board
    if (col < 0 || col > Board.gridSize) {
      return false;
    }

    // Verify that the position modifying is not occupied
    Posn toPosn = new Posn(row, col);
    List<Posn> workerPosns = board.getWorkers();
    for (Posn p : workerPosns) {
      if (p.samePosn(toPosn)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean isValidPlaceWorker(IBoard board, int row, int col) {
    return isEmptySpace(board, row, col);
  }
}
