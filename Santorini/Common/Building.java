package Common;

/**
 * Represents a Building on the board of a Santorini game.
 */
public class Building {

  // integer in [0, maxHeight] representing the height of this building
  private int height;

  /**
   * Creates a new Building with height 0.
   */
  public Building() {
    this.height = 0;
  }

  /**
   * Creates a new Building with the specified height.
   * @param height height of new Building.
   */
  public Building(int height) {
    this.height = height;
  }

  /**
   * Returns the height of this building
   *
   * @return height of this building
   */
  public int getHeight() {
    return height;
  }

  /**
   * Increases the height of this building by 1
   *
   * @throws IllegalStateException if increasing the height would make the building taller than 4
   */
  void increaseHeight() throws IllegalStateException {
    if (height == Board.maxHeight) {
      throw new IllegalArgumentException("Cannot increase height of Building that has height 4");
    }

    height += 1;
  }

}