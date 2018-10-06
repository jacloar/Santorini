import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests that Building behaves as desired
 */
public class BuildingTest {

  /**
   * Tests that getHeight returns the height of the building
   */
  @Test
  public void testGetHeight() {
    Building b = new Building(2);

    assertEquals(b.getHeight(),  2);
  }

  /**
   * Tests that the empty constructor makes a building of height 0
   */
  @Test
  public void testEmptyConstructor() {
    Building b = new Building();

    assertEquals(b.getHeight(), 0);
  }

  /**
   * Tests that increaseHeight increases a building's height by 1
   */
  @Test
  public void testIncreaseHeight() {
    Building b = new Building();

    assertEquals(b.getHeight(), 0);

    b.increaseHeight();

    assertEquals(b.getHeight(), 1);
  }

  /**
   * Tests that the Building constructor throws an IllegalArgumentException
   * if given a height larger than the max height.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructInvalidHeightBig() {
    int maxHeight = Board.maxHeight;
    new Building(maxHeight + 1);
  }

  /**
   * Tests that the Building constructor throws an IllegalArgumentException
   * if given a height less than 0.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorInvalidHeightSmall() {
    new Building(-1);
  }

  /**
   * Tests that increaseHeight throws an IllegalStateException if the height
   * is already at the maxHeight.
   */
  @Test(expected = IllegalStateException.class)
  public void testIncreasePastMaxHeight() {
    int maxHeight = Board.maxHeight;
    Building b = new Building(maxHeight);
    b.increaseHeight();
  }
}
