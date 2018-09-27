package Common;

/**
 * Represents a Worker on the board of a Santorini game.
 */
public class Worker {

  // Represents the x coordinate of this worker
  private int x;
  // Represents the y coordinate of this worker
  private int y;

  /**
   * Creates a new worker with the specified position.
   *
   * @param x x coordinate of the new worker
   * @param y y coordinate of the new worker
   */
  Worker(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Returns the x coordinate of this worker
   *
   * @return x coordinate of this worker
   */
  public int getX() {
    return x;
  }

  /**
   * Returns the y coordinate of this worker
   *
   * @return y coordinate of this worker
   */
  public int getY() {
    return y;
  }

  /**
   * Does this worker have the given position?
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
    this.x += dx;
    this.y += dy;
  }

}
