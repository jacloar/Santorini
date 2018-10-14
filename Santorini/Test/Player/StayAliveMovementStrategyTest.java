package Player;

import static org.junit.Assert.assertEquals;

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
}
