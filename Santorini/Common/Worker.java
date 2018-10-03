
/**
 * Represents a Worker on the board of a Santorini game.
 */
public class Worker {

  // Represents the position of this worker
  private Posn posn;

  /**
   * Creates a new worker with the specified position.
   *
   * @param x x coordinate of the new worker
   * @param y y coordinate of the new worker
   */
  public Worker(int x, int y) {
    this.posn = new Posn(x, y);
  }

  public Worker(Posn posn) {
    this.posn = posn;
  }

  /**
   * Returns the x coordinate of this worker
   *
   * If worker at posn (3, 2), will return 3.
   *
   * @return x coordinate of this worker
   */
  public int getX() {
    return posn.getX();
  }

  /**
   * Returns the y coordinate of this worker
   *
   * If worker at posn (3, 2), will return 2.
   *
   * @return y coordinate of this worker
   */
  public int getY() {
    return posn.getY();
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
   * @param x x to check for
   * @param y y to check for
   * @return true if the positions match
   */
  public boolean hasPosn(int x, int y) {
    return getX() == x && getY() == y;
  }

  /**
   * Moves this worker by the given change in x and change in y.
   *
   * Example: If this worker is at (1, 1)
   * moveBy(-1, 1) will make the new position (0, 2)
   *
   * @param dx change in x
   * @param dy change in y
   */
  void moveBy(int dx, int dy) {
    int x = this.posn.getX() + dx;
    int y = this.posn.getY() + dy;

    this.posn = new Posn(x, y);
  }

}
