import admin.referee.RefereeTest;
import common.board.BoardTest;
import common.board.HeightTest;
import common.board.ViewModelBoardTest;
import common.rules.TestStandardSantoriniRulesEngine;
import java.util.ArrayList;
import java.util.List;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import strategy.StayAliveStrategyTests;
import strategy.TestDiagonalPlacementStrategy;


/**
 * This class runs all of our Santorini unit tests.
 */
public class TestRunner {
  public static void main(String[] args) {
    List<Class> classesToTest = new ArrayList<>();
    classesToTest.add(BoardTest.class);
    classesToTest.add(HeightTest.class);
    classesToTest.add(ViewModelBoardTest.class);
    classesToTest.add(RefereeTest.class);
    classesToTest.add(StayAliveStrategyTests.class);
    classesToTest.add(TestDiagonalPlacementStrategy.class);
    classesToTest.add(TestStandardSantoriniRulesEngine.class);

    int ran = 0;
    int succeeded = 0;

    for(Class c: classesToTest) {
      Result result = JUnitCore.runClasses(c);
      for (Failure failure : result.getFailures()) {
        System.out.println("failure:");
        System.out.println(failure.toString());
      }
      ran += result.getRunCount();
      succeeded = ran - result.getFailureCount();
    }

    System.out.println("ran " + ran + " Tests");
    System.out.println(succeeded + " Tests passed");

  }
}