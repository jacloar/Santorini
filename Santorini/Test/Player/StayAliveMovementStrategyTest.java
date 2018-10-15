package Player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import Common.Posn;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Tests the StayAliveMovementStrategy class
 */
public class StayAliveMovementStrategyTest {

  /**
   * Tests to see if a simple game tree contains the correct number of possible moves
   */
  @Test
  public void testBranchSimple() {
    StayAliveMovementStrategy strategy = new StayAliveMovementStrategy(2);

    int[][] heights = new int[6][6];
    List<Posn> myWorkers = new ArrayList<>();
    myWorkers.add(new Posn(0, 0));
    myWorkers.add(new Posn(5, 5));

    List<Posn> opponentWorkers = new ArrayList<>();
    opponentWorkers.add(new Posn(0, 5));
    opponentWorkers.add(new Posn(5, 0));

    List<IGameState> possibleStates = strategy.branch(new InProgress(heights, opponentWorkers, myWorkers));

    assertEquals(36, possibleStates.size());
  }

  /**
   * Tests to see if the player has a move that is safe after one turn
   */
  @Test
  public void testIsEveryStateGood1() {
    StayAliveMovementStrategy strategy = new StayAliveMovementStrategy(0);

    int[][] heights = new int[6][6];
    List<Posn> myWorkers = new ArrayList<>();
    myWorkers.add(new Posn(0, 0));
    myWorkers.add(new Posn(5, 5));

    List<Posn> opponentWorkers = new ArrayList<>();
    opponentWorkers.add(new Posn(0, 5));
    opponentWorkers.add(new Posn(5, 0));

    InProgress state = new InProgress(heights, opponentWorkers, myWorkers);

    assertTrue(strategy.isEveryStateGood(state, 1));
  }

  /**
   * Tests to see if a player has a move that is safe after two turns
   */
  @Test
  public void testIsEveryStateGood2() {
    StayAliveMovementStrategy strategy = new StayAliveMovementStrategy(0);

    int[][] heights = new int[6][6];
    List<Posn> myWorkers = new ArrayList<>();
    myWorkers.add(new Posn(0, 0));
    myWorkers.add(new Posn(5, 5));

    List<Posn> opponentWorkers = new ArrayList<>();
    opponentWorkers.add(new Posn(0, 5));
    opponentWorkers.add(new Posn(5, 0));

    InProgress state = new InProgress(heights, opponentWorkers, myWorkers);

    assertTrue(strategy.isEveryStateGood(state, 2));
  }


  /**
   * Tests to see if the player will lose after one turn
   */
  @Test
  public void testIsEveryStateGoodFail1() {
    StayAliveMovementStrategy strategy = new StayAliveMovementStrategy(0);

    int[][] heights = {
        {3, 2, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0}
    };

    List<Posn> myWorkers = new ArrayList<>();
    myWorkers.add(new Posn(5, 5));
    myWorkers.add(new Posn(4, 5));

    List<Posn> opponentWorkers = new ArrayList<>();
    opponentWorkers.add(new Posn(0, 1));
    opponentWorkers.add(new Posn(0, 2));

    IGameState state = new InProgress(heights, opponentWorkers, myWorkers);

    assertFalse(strategy.isEveryStateGood(state, 1));
  }

  /**
   * Tests to see that the player will loose within three turns no matter what
   */
  @Test
  public void testIsEveryStateGoodFail3() {
    StayAliveMovementStrategy strategy = new StayAliveMovementStrategy(0);

    int[][] heights = {
        {3, 2, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0}
    };

    List<Posn> myWorkers = new ArrayList<>();
    myWorkers.add(new Posn(5, 5));
    myWorkers.add(new Posn(4, 5));

    List<Posn> opponentWorkers = new ArrayList<>();
    opponentWorkers.add(new Posn(0, 2));
    opponentWorkers.add(new Posn(0, 3));

    IGameState state = new InProgress(heights, opponentWorkers, myWorkers);

    assertFalse(strategy.isEveryStateGood(state, 3));
  }

  /**
   * Tests to see that the player has a move that will keep them alive for 2 turns
   */
  @Test
  public void testIsEveryStateGoodNotFail2() {
    StayAliveMovementStrategy strategy = new StayAliveMovementStrategy(0);

    int[][] heights = {
        {3, 2, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0}
    };

    List<Posn> myWorkers = new ArrayList<>();
    myWorkers.add(new Posn(5, 5));
    myWorkers.add(new Posn(4, 5));

    List<Posn> opponentWorkers = new ArrayList<>();
    opponentWorkers.add(new Posn(0, 2));
    opponentWorkers.add(new Posn(0, 3));

    IGameState state = new InProgress(heights, opponentWorkers, myWorkers);

    assertTrue(strategy.isEveryStateGood(state, 2));
  }

  /**
   * Tests to see that the player can get trapped and no moves can prevent it
   */
  @Test
  public void testIsEveryStateGoodWorkersSurrounded() {
    StayAliveMovementStrategy strategy = new StayAliveMovementStrategy(0);

    int[][] heights = {
        {3, 2, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 4, 4},
        {0, 0, 0, 0, 4, 0},
        {0, 0, 0, 0, 1, 0}
    };

    List<Posn> myWorkers = new ArrayList<>();
    myWorkers.add(new Posn(5, 5));
    myWorkers.add(new Posn(4, 5));

    List<Posn> opponentWorkers = new ArrayList<>();
    opponentWorkers.add(new Posn(5, 2));
    opponentWorkers.add(new Posn(5, 3));

    List<Posn> allWorkers = new ArrayList<>();
    allWorkers.addAll(myWorkers);
    allWorkers.addAll(opponentWorkers);

    InProgress state = new InProgress(heights, opponentWorkers, myWorkers);

    assertFalse(strategy.isEveryStateGood(state, 2));
  }

  /**
   * The player should get a move back representing the start of a safe sequence of moves.
   */
  @Test
  public void testGetMove() {
    StayAliveMovementStrategy strategy = new StayAliveMovementStrategy(1);

    int[][] heights = {
            {0, 0, 0, 0, 0, 0},
            {4, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0}
    };

    List<Posn> myWorkers = new ArrayList<>();
    myWorkers.add(new Posn(0, 0));
    myWorkers.add(new Posn(0, 1));

    List<Posn> opponentWorkers = new ArrayList<>();
    opponentWorkers.add(new Posn(5, 2));
    opponentWorkers.add(new Posn(5, 3));

    List<Posn> allWorkers = new ArrayList<>();
    allWorkers.addAll(myWorkers);
    allWorkers.addAll(opponentWorkers);

    Move result = strategy.makeMove(heights, allWorkers, myWorkers);

    assertEquals(result.getWorkerPosn().getRow(), 0);
    assertEquals(result.getWorkerPosn().getCol(), 0);
    assertEquals(result.getMoveDx(), 1);
    assertEquals(result.getMoveDx(), 1);
    assertEquals(result.getBuildDx(), -1);
    assertEquals(result.getBuildDy(), -1);
  }

  /**
   * Since the game in un-winnable we should just return the first valid move.
   */
  @Test
  public void tesNoGoodMoves2() {
    StayAliveMovementStrategy strategy = new StayAliveMovementStrategy(2);

    int[][] heights = {
            {3, 2, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0}
    };


    List<Posn> myWorkers = new ArrayList<>();
    myWorkers.add(new Posn(5, 5));
    myWorkers.add(new Posn(4, 4));

    List<Posn> opponentWorkers = new ArrayList<>();
    opponentWorkers.add(new Posn(2, 0));
    opponentWorkers.add(new Posn(2, 1));

    List<Posn> allWorkers = new ArrayList<>();
    allWorkers.addAll(myWorkers);
    allWorkers.addAll(opponentWorkers);

    Move result = strategy.makeMove(heights, allWorkers, myWorkers);

    assertEquals(result.getWorkerPosn().getRow(), 5);
    assertEquals(result.getWorkerPosn().getCol(), 5);
    assertEquals(result.getMoveDx(), -1);
    assertEquals(result.getMoveDx(), -1);
    assertEquals(result.getBuildDx(), -1);
    assertEquals(result.getBuildDy(), -1);
  }

}
