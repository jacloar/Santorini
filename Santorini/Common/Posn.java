package Common;

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
   *
   * @return row coordinate of this position
   */
  public int getRow() {
    return row;
  }

  /**
   * Returns the col coordinate of this position
   *
   * @return col coordinate of this position
   */
  public int getCol() {
    return col;
  }

  /**
   * Is this posn the same as the given posn?
   *
   * @param p posn to check for equality
   * @return true if refer to same position, false otherwise
   */
  public boolean samePosn(Posn p) {
    return getRow() == p.getRow() && getCol() == p.getCol();
  }

  /**
   * Is this the same posn as the given posn?
   *
   * @param row row to check
   * @param col col to check
   * @return true if they refer to the same position, false otherwise
   */
  public boolean samePosn(int row, int col) {
    return this.samePosn(new Posn(row, col));
  }

  /**
   * Returns a representation of this object as a string
   *
   * @return this object represented as a string
   */
  public String toString() {
    return String.format("(%d, %d)", this.getRow(), this.getCol());
  }
}
