package observer;

import static org.assertj.core.api.Assertions.assertThat;

import common.board.Board;
import common.board.Height;
import common.board.IBoard;
import common.board.ICell;
import common.data.Action;
import common.data.ActionType;
import common.data.Direction;
import common.interfaces.IObserver;
import common.interfaces.IPlayer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import player.AIPlayer;

/**
 * Tests the STDOUT observer
 */
public class StdOutObserverTest {

  private StringBuilder builder;
  private IObserver observer;

  @Before
  public void init() {
    this.builder = new StringBuilder();
    this.observer = new StdOutObserver(builder);
  }

  /**
   * Tests that the StdOutObserver prints an empty board when given an empty board.
   */
  @Test
  public void testUpdateBoardEmpty() {
    IBoard board = new Board();

    assertThat(builder).isEmpty();

    observer.update(board);

    assertThat(builder).isEqualToIgnoringWhitespace("[[0,0,0,0,0,0],"
        + "[0,0,0,0,0,0],"
        + "[0,0,0,0,0,0],"
        + "[0,0,0,0,0,0],"
        + "[0,0,0,0,0,0],"
        + "[0,0,0,0,0,0]]");
  }

  /**
   * Tests that the StdOutObserver prints a board representing the supplied board.
   */
  @Test
  public void testUpdateBoardNotEmpty() {
    ICell[][] cells = new ICell[6][6];
    cells[0][0] = new Height(2);
    cells[1][1] = new Height(3);
    cells[2][0] = new Height(4);

    IBoard board = new Board(cells);

    board.createWorker("one1", 0, 0);
    board.createWorker("one2", 3, 3);
    board.createWorker("two1", 1, 2);
    board.createWorker("two2", 2, 1);

    assertThat(builder).isEmpty();

    observer.update(board);

    assertThat(builder).isEqualToIgnoringWhitespace("[[\"2one1\",0,0,0,0,0],"
        + "[0,3,\"0two1\",0,0,0],"
        + "[4,\"0two2\",0,0,0,0],"
        + "[0,0,0,\"0one2\",0,0],"
        + "[0,0,0,0,0,0],"
        + "[0,0,0,0,0,0]]");
  }

  /**
   * Tests that StdOutObserver prints the specified turn
   */
  @Test
  public void testNormalTurn() {

    Action move = new Action(ActionType.MOVE, "one1", new Direction("EAST", "SOUTH"));
    Action build = new Action(ActionType.BUILD, "one1", new Direction("WEST",  "NORTH"));

    List<Action> turn = new ArrayList<>();
    turn.add(move);
    turn.add(build);

    assertThat(builder).isEmpty();

    observer.update(turn);

    assertThat(builder).isEqualToIgnoringWhitespace("[\"one1\", \"EAST\", \"SOUTH\", \"WEST\", \"NORTH\"]");
  }

  /**
   * Tests that StdOutObserver prints the specified winning turn
   */
  @Test
  public void testWinTurn() {

    Action move = new Action(ActionType.MOVE, "one1", new Direction("EAST", "SOUTH"));

    List<Action> turn = new ArrayList<>();
    turn.add(move);

    assertThat(builder).isEmpty();

    observer.update(turn);

    assertThat(builder).isEqualToIgnoringWhitespace("[\"one1\", \"EAST\", \"SOUTH\"]");
  }

  /**
   * Tests that calling updateGiveUp with a player prints the given player
   */
  @Test
  public void testUpdateGiveUp() {
    IPlayer player = new AIPlayer("one", null);

    assertThat(builder).isEmpty();

    observer.updateGiveUp(player);

    assertThat(builder).isEqualToIgnoringWhitespace("\"one gave up.\"");
  }

  /**
   * Tests that calling updateWin with a player prints the given player
   */
  @Test
  public void testUpdateWinner() {
    IPlayer player = new AIPlayer("one", null);

    assertThat(builder).isEmpty();

    observer.updateWin(player);

    assertThat(builder).isEqualToIgnoringWhitespace("\"one won the game!\"");
  }

  /**
   * Tests that calling updateError with an error prints the error
   */
  @Test
  public void testUpdateError() {
    String error = "Error!!";

    assertThat(builder).isEmpty();

    observer.updateError(error);

    assertThat(builder).isEqualToIgnoringWhitespace("\"Error!!\"");
  }
}
