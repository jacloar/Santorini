package Common;

/**
 * Represents a Worker on the board of a Santorini game.
 */
public class Worker {

  // Represents the position of this worker
  private Posn posn;

  /**
   * Creates a new worker with the specified position.
   *
   * @param row row coordinate of the new worker
   * @param col col coordinate of the new worker
   */
  public Worker(int row, int col) {
    this.posn = new Posn(row, col);
  }

  public Worker(Posn posn) {
    this.posn = posn;
  }

  /**
   * Returns the row coordinate of this worker
   *
   * If worker at posn (3, 2), will return 3.
   *
   * @return row of this worker
   */
  public int getRow() {
    return posn.getRow();
  }

  /**
   * Returns the col coordinate of this worker
   *
   * If worker at posn (3, 2), will return 2.
   *
   * @return col of this worker
   */
  public int getCol() {
    return posn.getCol();
  }

  /**
   * Returns the Posn representing the position of this worker
   *
   * @return position of this worker
   */
  public Posn getPosn() {
    return posn;
  }

  /**
   * Does this worker have the given position?
   *
   * If worker at posn (3, 2) and given (3, 2) will return true.
   * If worker at posn (3, 2) and given (1, 2) will return false.
   *
   * @param row row to check for
   * @param col col to check for
   * @return true if the positions match
   */
  public boolean hasPosn(int row, int col) {
    return getRow() == row && getCol() == col;
  }

  /**
   * Moves this worker by the given change in row and change in col.
   *
   * Example: If this worker is at (1, 1)
   * moveBy(-1, 1) will make the new position (0, 2)
   *
   * @param dRow change in row
   * @param dCol change in col
   */
  void moveBy(int dRow, int dCol) {
    int x = this.posn.getRow() + dRow;
    int y = this.posn.getCol() + dCol;

    this.posn = new Posn(x, y);
  }

}
