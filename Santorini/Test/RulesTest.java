import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for the Rules class.
 */
public class RulesTest {

  // MOVE WORKER TESTS:

  /**
   * Tests to see if a worker is allowed to move to a neighboring square.
   */
  @Test
  public void testIsValidWorkerMoveTrue() {
    IBoard board = new Board();
    Posn p = new Posn(2,1);
    Worker worker = new Worker(p);
    IRules rules = new Rules();
    boolean isValidMove = rules.isValidWorkerMove(board, worker, 1,1);
    assertEquals(isValidMove, true);
  }

  /**
   * Tests to see if a worker is allowed to move off the board
   */
  @Test
  public void testIsValidWorkerMoveOffBoard() {
    IBoard board = new Board();
    Posn p = new Posn(0,0);
    Worker worker = new Worker(p);
    IRules rules = new Rules();
    boolean isValidMove = rules.isValidWorkerMove(board, worker, -1,0);
    assertEquals(isValidMove, false);
  }

  /**
   * Tests to see if a worker is allowed to move to the position it is already at.
   */
  @Test
  public void testIsValidWorkerMoveSamePosition() {
    IBoard board = new Board();
    Posn p = new Posn(0,0);
    Worker worker = new Worker(p);
    IRules rules = new Rules();
    boolean isValidMove = rules.isValidWorkerMove(board, worker, 0,0);
    assertEquals(isValidMove, false);
  }

  /**
   * Tests to see if a worker is allowed to move more than 1 square.
   */
  @Test
  public void testIsValidWorkerMoveTooFar() {
    IBoard board = new Board();
    Posn p = new Posn(0,0);
    Worker worker = new Worker(p);
    IRules rules = new Rules();
    boolean isValidMove = rules.isValidWorkerMove(board, worker, 0,2);
    assertEquals(isValidMove, false);
  }

  /**
   * Tests to see if a worker is allowed to move diagonally.
   */
  @Test
  public void testIsValidWorkerMoveDiagonally() {
    IBoard board = new Board();
    Posn p = new Posn(0,0);
    Worker worker = new Worker(p);
    IRules rules = new Rules();
    boolean isValidMove = rules.isValidWorkerMove(board, worker, 1,1);
    assertTrue(isValidMove);
  }

  /**
   * Tests to see if a worker is allowed to move to a square that is occupied.
   */
  @Test
  public void testIsValidWorkerMoveOnToOccupiedSquare() {
    IBoard board = new Board();
    Posn p = new Posn(0,0);
    Worker worker = new Worker(p);
    board.placeWorker(1,0);
    IRules rules = new Rules();
    boolean isValidMove = rules.isValidWorkerMove(board, worker, 1,0);
    assertFalse(isValidMove);
  }

  /**
   * Tests to see if a worker is allowed to move to a square that is 2 units of height higher than
   * its current height.
   */
  @Test
  public void testIsValidWorkerMoveUpToHighBuilding() {
    Building height2 = new Building(2);
    Building[][] heights = new Building[6][6];
    heights[1][0] = height2;
    List<Worker> workers = new ArrayList<Worker>();
    IBoard board = new Board(heights, workers);
    Posn p = new Posn(0,0);
    Worker worker = new Worker(p);
    board.placeWorker(0,0);
    IRules rules = new Rules();
    boolean isValidMove = rules.isValidWorkerMove(board, worker, 1,0);
    assertFalse(isValidMove);
  }

  /**
   * Tests to see if a worker is allowed to move to a square that is 1 unit of height higher than
   * its current height.
   */
  @Test
  public void testIsValidWorkerMoveUpToValidBuilding() {
    Building height1 = new Building(1);
    Building[][] heights = new Building[6][6];
    heights[1][0] = height1;
    List<Worker> workers = new ArrayList<Worker>();
    IBoard board = new Board(heights, workers);
    Posn p = new Posn(0,0);
    Worker worker = new Worker(p);
    board.placeWorker(0,0);
    IRules rules = new Rules();
    boolean isValidMove = rules.isValidWorkerMove(board, worker, 1,0);
    assertTrue(isValidMove);
  }

  // BUILD WORKER TESTS

  /**
   * Tests to see if a worker is allowed to move to a neighboring square.
   */
  @Test
  public void testIsValidWorkerBuildTrue() {
    IBoard board = new Board();
    Posn p = new Posn(2,1);
    Worker worker = new Worker(p);
    IRules rules = new Rules();
    boolean isValidBuild = rules.isValidWorkerBuild(board, worker, 1,1);
    assertTrue(isValidBuild);
  }

  /**
   * Tests to see if a worker is allowed to build off the board
   */
  @Test
  public void testIsValidWorkerBuildOffBoard() {
    IBoard board = new Board();
    Posn p = new Posn(0,0);
    Worker worker = new Worker(p);
    IRules rules = new Rules();
    boolean isValidBuild = rules.isValidWorkerBuild(board, worker, -1,0);
    assertFalse(isValidBuild);
  }

  /**
   * Tests to see if a worker is allowed to build to the position it is already at.
   */
  @Test
  public void testIsValidWorkerBuildSamePosition() {
    IBoard board = new Board();
    Posn p = new Posn(0,0);
    Worker worker = new Worker(p);
    IRules rules = new Rules();
    boolean isValidMove = rules.isValidWorkerBuild(board, worker, 0,0);
    assertEquals(isValidMove, false);
  }

  /**
   * Tests to see if a worker is allowed to build more than 1 square.
   */
  @Test
  public void testIsValidWorkerBuildTooFar() {
    IBoard board = new Board();
    Posn p = new Posn(0,0);
    Worker worker = new Worker(p);
    IRules rules = new Rules();
    boolean isValidBuild = rules.isValidWorkerBuild(board, worker, 0,2);
    assertFalse(isValidBuild);
  }

  /**
   * Tests to see if a worker is allowed to build diagonally.
   */
  @Test
  public void testIsValidWorkerBuildDiagonally() {
    IBoard board = new Board();
    Posn p = new Posn(0,0);
    Worker worker = new Worker(p);
    IRules rules = new Rules();
    boolean isValidBuild = rules.isValidWorkerBuild(board, worker, 1,1);
    assertTrue(isValidBuild);
  }

  /**
   * Tests to see if a worker is allowed to build to a square that is occupied.
   */
  @Test
  public void testIsValidWorkerBuildOnToOccupiedSquare() {
    IBoard board = new Board();
    Posn p = new Posn(0,0);
    Worker worker = new Worker(p);
    board.placeWorker(1,0);
    IRules rules = new Rules();
    boolean isValidBuild = rules.isValidWorkerBuild(board, worker, 1,0);
    assertFalse(isValidBuild);
  }

  /**
   * Tests to see if a worker is allowed to build to a square that is 2 units of height higher than
   * its current height.
   */
  @Test
  public void testIsValidWorkerBuildUpToHighBuilding() {
    Building height2 = new Building(2);
    Building[][] heights = new Building[6][6];
    heights[1][0] = height2;
    List<Worker> workers = new ArrayList<Worker>();
    IBoard board = new Board(heights, workers);
    Posn p = new Posn(0,0);
    Worker worker = new Worker(p);
    board.placeWorker(0,0);
    IRules rules = new Rules();
    boolean isValidBuild = rules.isValidWorkerBuild(board, worker, 1,0);
    assertTrue(isValidBuild);
  }

  /**
   * Tests to see if a worker is allowed to build to a square that is 1 unit of height higher than
   * its current height.
   */
  @Test
  public void testIsValidWorkerBuildUpToValidBuilding() {
    Building height1 = new Building(1);
    Building[][] heights = new Building[6][6];
    heights[1][0] = height1;
    List<Worker> workers = new ArrayList<Worker>();
    IBoard board = new Board(heights, workers);
    Posn p = new Posn(0,0);
    Worker worker = new Worker(p);
    board.placeWorker(0,0);
    IRules rules = new Rules();
    boolean isValidBuild = rules.isValidWorkerBuild(board, worker, 1,0);
    assertTrue(isValidBuild);
  }

  // WORKER PLACEMENT TESTS:


  /**
   * Test to see if you can make a valid worker placement on the board.
   */
  @Test
  public void testValidWorkerPlacementTrue() {
    IBoard board = new Board();
    IRules rules = new Rules();
    boolean isValidPlacement = rules.isValidPlaceWorker(board,0,0);
    assertTrue(isValidPlacement);
  }


  /**
   * Test to see if you can make a worker placement off the board row.
   */
  @Test
  public void testValidWorkerPlacementOffBoardRow() {
    IBoard board = new Board();
    IRules rules = new Rules();
    boolean isValidPlacement = rules.isValidPlaceWorker(board,7,0);
    assertFalse(isValidPlacement);
  }


  /**
   * Test to see if you can make a worker placement off the board col.
   */
  @Test
  public void testValidWorkerPlacementOffBoardCol() {
    IBoard board = new Board();
    IRules rules = new Rules();
    boolean isValidPlacement = rules.isValidPlaceWorker(board,0,7);
    assertFalse(isValidPlacement);
  }

  /**
   * Test to see if you can make a worker placement where a worker already is.
   */
  @Test
  public void testValidWorkerPlacementOccupied() {
    IRules rules = new Rules();
    IBoard board = new Board();
    Worker worker = board.placeWorker(1,1);
    boolean isValidPlacement = rules.isValidPlaceWorker(board,1,1);
    assertFalse(isValidPlacement);
  }

}
