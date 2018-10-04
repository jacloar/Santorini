/**
 * Immutable class that represents a position on the 2d plane
 */
public class Posn {

  // row coordinate of the position
  private int row;
  // col coordinate of the position
  private int col;

  /**
   * Creates a new position at the specified coordinates
   *
   * @param row row coordinate of the position
   * @param col col coordinate of the position
   */
  public Posn(int row, int col) {
    this.row = row;
    this.col = col;
  }

  /**
   * Returns the row coordinate of this position
   * @return row coordinate of this position
   */
  public int getRow() {
    return row;
  }

  /**
   * Returns the col coordinate of this position
   * @return col coordinate of this position
   */
  public int getCol() {
    return col;
  }

  /**
   * Is this posn the same as the given posn?
   * @param p posn to check for equality
   * @return true if refer to same position, false otherwise
   */
  public boolean samePosn(Posn p) {
    return getRow() == p.getRow() && getCol() == p.getCol();
  }
}
