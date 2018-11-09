package strategy;

import static org.junit.Assert.assertEquals;

import common.board.Board;
import common.board.BuildingWorker;
import common.board.Height;
import common.board.IBoard;
import common.board.ICell;
import common.data.Action;
import common.data.ActionType;
import common.data.Direction;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class StayAliveStrategyTests {

    @Test
    public void itTestsSimpleLookahead() {
        String playerToTest = "one";
        String opponent = "two";

        IBoard board = new Board(formulateSimpleBoard());

        StayAliveStrategy turnStrategy = new StayAliveStrategy(2);
        turnStrategy.setPlayer(playerToTest);
        turnStrategy.setOpponent(opponent);
        List<Action> turnToTake = new ArrayList<>();

        Action move = new Action(ActionType.MOVE, "one2", new Direction("WEST", "SOUTH"));
        Action build = new Action(ActionType.BUILD, "one2", new Direction("PUT", "SOUTH"));

        turnToTake.add(move);
        turnToTake.add(build);

        int value = turnStrategy.score(turnToTake, board.toViewModel(), 2);

        assertEquals(0, value);
    }

    private ICell[][] formulateSimpleBoard() {
        ICell[][] results = new ICell[6][6];

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                results[i][j] = new Height(0);
            }
        }

        results[0][0] = new BuildingWorker("one", 1, 0);
        results[0][1] = new BuildingWorker("one", 2, 1);
        results[0][2] = new Height(3);
        results[0][3] = new BuildingWorker("two", 1, 2);
        results[1][1] = new BuildingWorker("two", 2, 2);
        results[1][2] = new Height(3);

        return results;
    }

    @Test
    public void itTestsAdvancedLookahead() {
        String playerToTest = "one";
        String opponent = "two";

        IBoard board = new Board(formulateAdvancedBoard());

        StayAliveStrategy turnStrategy = new StayAliveStrategy(3);
        turnStrategy.setPlayer(playerToTest);
        turnStrategy.setOpponent(opponent);
        List<Action> turnToTake = new ArrayList<>();

        Action move = new Action(ActionType.MOVE, "one1", new Direction("EAST", "SOUTH"));
        Action build = new Action(ActionType.BUILD, "one1", new Direction("WEST", "PUT"));

        turnToTake.add(move);
        turnToTake.add(build);

        int value = turnStrategy.score(turnToTake, board.toViewModel(), 3);

        assertEquals(1, value);
    }

    private ICell[][] formulateAdvancedBoard() {
        ICell[][] results = new ICell[6][6];

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                results[i][j] = new Height(0);
            }
        }

        results[0][0] = new BuildingWorker("one", 1, 0);
        results[0][1] = new BuildingWorker("one", 2, 0);

        results[1][0] = new Height(3);

        results[2][0] = new BuildingWorker("two", 1, 0);
        results[2][1] = new BuildingWorker("two", 2, 0);

        return results;
    }

    @Test
    public void itTestsImmediateWin() {
        String playerToTest = "one";
        String opponent = "two";

        IBoard board = new Board(formulateAdvancedBoard());

        StayAliveStrategy turnStrategy = new StayAliveStrategy(0);
        turnStrategy.setPlayer(playerToTest);
        turnStrategy.setOpponent(opponent);
        List<Action> turnToTake = new ArrayList<>();

        Action move = new Action(ActionType.MOVE, "one1", new Direction("PUT", "SOUTH"));

        turnToTake.add(move);

        int value = turnStrategy.score(turnToTake, board.toViewModel(), 0);

        assertEquals(1, value);
    }

}
