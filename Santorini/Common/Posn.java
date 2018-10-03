/**
 * Immutable class that represents a position on the 2d plane
 */
public class Posn {

  // x coordinate of the position
  private int x;
  // y coordinate of the position
  private int y;

  /**
   * Creates a new position at the specified coordinates
   *
   * @param x x coordinate of the position
   * @param y y coordinate of the position
   */
  public Posn(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Returns the x coordinate of this position
   * @return x coordinate of this position
   */
  public int getX() {
    return x;
  }

  /**
   * Returns the y coordinate of this position
   * @return y coordinate of this position
   */
  public int getY() {
    return y;
  }

  /**
   * Is this posn the same as the given posn?
   * @param p posn to check for equality
   * @return true if refer to same position, false otherwise
   */
  public boolean samePosn(Posn p) {
    return getX() == p.getX() && getY() == p.getY();
  }
}
