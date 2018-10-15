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

    InProgress state = new InProgress(heights, opponentWorkers, myWorkers);

    assertFalse(strategy.isEveryStateGood(state, 1));
  }
}
