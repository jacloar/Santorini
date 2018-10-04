
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
    this(0);
  }

  /**
   * Creates a new Building with the specified height.
   *
   * @param height height of new Building.
   */
  public Building(int height) {
    if (height > Board.maxHeight) {
      throw new IllegalArgumentException(String.format("Building cannot have height greater than %d", Board.maxHeight));
    }

    if (height < 0) {
      throw new IllegalArgumentException("Building cannot have height less than 0");
    }

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
   * If this height is 2, new height will be 3.
   * If this height is 4, will throw error.
   *
   * @throws IllegalStateException if increasing the height would make the building taller than 4
   */
  void increaseHeight() throws IllegalStateException {
    if (height == Board.maxHeight) {
      throw new IllegalStateException(
          String.format("Cannot increase height of Building that has height %d", Board.maxHeight));
    }

    height += 1;
  }

}
