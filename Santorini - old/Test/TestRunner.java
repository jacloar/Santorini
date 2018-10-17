
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.ArrayList;
import java.util.List;

import Common.BoardTest;
import Common.BuildingTest;
import Common.PosnTest;
import Common.RulesTest;
import Common.WorkerTest;
import Player.DiagonalPlacementStrategyTest;
import Player.MaxDistancePlacementStrategyTest;
import Player.StayAliveMovementStrategyTest;

/**
 * This class runs all of our Santorini unit tests.
 */
public class TestRunner {
  public static void main(String[] args) {
    List<Class> classesToTest = new ArrayList<>();
    classesToTest.add(BoardTest.class);
    classesToTest.add(BuildingTest.class);
    classesToTest.add(PosnTest.class);
    classesToTest.add(RulesTest.class);
    classesToTest.add(WorkerTest.class);
    classesToTest.add(DiagonalPlacementStrategyTest.class);
    classesToTest.add(MaxDistancePlacementStrategyTest.class);
    classesToTest.add(StayAliveMovementStrategyTest.class);

    int ran = 0;
    int succeeded = 0;

    for(Class c: classesToTest) {
      Result result = JUnitCore.runClasses(c);
      for (Failure failure : result.getFailures()) {
        System.out.println(failure.toString());
      }
      ran += result.getRunCount();
      succeeded = ran - result.getFailureCount();
    }

    System.out.println("ran " + ran + " Tests");
    System.out.println(succeeded + " Tests passed");

  }
}