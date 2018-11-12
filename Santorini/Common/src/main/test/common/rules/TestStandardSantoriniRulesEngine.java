package common.rules;

import static org.assertj.core.api.Assertions.assertThat;

import common.board.Board;
import common.board.IBoard;
import common.data.Action;
import common.data.ActionType;
import common.data.Direction;
import common.data.PlaceWorkerAction;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class TestStandardSantoriniRulesEngine {

  private String p1Name;
  private String p2Name;
  private StandardSantoriniRulesEngine rules;
  private IBoard board;
  private List<String> players;

  @Before
  public void init() {
    p1Name = "one";
    p2Name = "two";


    rules = new StandardSantoriniRulesEngine();
    board = new Board();

    players = new ArrayList<>();
    players.add(p1Name);
    players.add(p2Name);
  }

  /**
   * Test to assert that placing a valid worker on an empty board properly adds a worker
   */
  @Test
  public void testPlaceWorkerEmptyBoard() {
    IRulesEngine rules = new StandardSantoriniRulesEngine();

    IBoard board = new Board();
    PlaceWorkerAction placeWorkerAction = new PlaceWorkerAction( 0, 0);

    boolean isValid = rules.isPlaceWorkerLegal(board, placeWorkerAction);

    assertThat(isValid).isTrue();
  }

  /**
   * Tests if the starting state of the game is a legal one.
   */
  @Test
  public void testIsStartingStateLegal() {

    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    assertThat(rules.isStartingStateLegal(board, players)).isTrue();
  }


  /**
   * Tests if the starting state of the game is an illegal one if there are 5 workers.
   */
  @Test
  public void testIsStartingStateNotLegal() {

    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);
    board.createWorker(p1Name, 5, 5);

    assertThat(rules.isStartingStateLegal(board, players)).isFalse();
  }

  /**
   * Tests if the starting state of the game is an illegal one if there are 3 players.
   */
  @Test
  public void testIsStartingStateNotLegalExtraPlayer() {
    String p3Name = "three";

    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);
    board.createWorker(p3Name, 4, 4);
    board.createWorker(p3Name, 5, 5);

    players.add(p3Name);


    assertThat(rules.isStartingStateLegal(board, players)).isFalse();
  }


  /**
   * Tests if the given turn is a valid one.
   */
  @Test
  public void testValidTurn() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    Action p1Move = new Action(ActionType.MOVE, "one1", new Direction("PUT", "SOUTH"));
    Action p1Build = new Action(ActionType.BUILD, "one1", new Direction("PUT", "SOUTH"));
    List<Action> p1Turn = new ArrayList<>();
    p1Turn.add(p1Move);
    p1Turn.add(p1Build);

    assertThat(rules.isTurnLegal(board, p1Turn, p1Name)).isTrue();
  }

  /**
   * Tests if the given turn is not a valid one if the player tries to move a worker that doesnt
   * belong to them.
   */
  @Test
  public void testNotValidTurnWrongWorker() {

    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    Action p1Move = new Action(ActionType.MOVE, "two1", new Direction("PUT", "SOUTH"));
    Action p1Build = new Action(ActionType.BUILD, "two1", new Direction("PUT", "SOUTH"));
    List<Action> p1Turn = new ArrayList<>();
    p1Turn.add(p1Move);
    p1Turn.add(p1Build);

    assertThat(rules.isTurnLegal(board, p1Turn, p1Name)).isFalse();
  }

  /**
   * Tests if the given turn is not a valid one if the move and build actions have different workers.
   */
  @Test
  public void testNotValidTurnDiffWorkerMoveAndBuild() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    Action p1Move = new Action(ActionType.MOVE, "one1", new Direction("PUT", "SOUTH"));
    Action p1Build = new Action(ActionType.BUILD, "one2", new Direction("PUT", "SOUTH"));
    List<Action> p1Turn = new ArrayList<>();
    p1Turn.add(p1Move);
    p1Turn.add(p1Build);

    assertThat(rules.isTurnLegal(board, p1Turn, p1Name)).isFalse();
  }

  /**
   * Tests if the given turn is not a valid one if "PUT", "PUT" is given for a move.
   */
  @Test
  public void testNotValidTurnNoMove() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    Action p1Move = new Action(ActionType.MOVE, "one1", new Direction("PUT", "PUT"));
    Action p1Build = new Action(ActionType.BUILD, "one1", new Direction("PUT", "SOUTH"));
    List<Action> p1Turn = new ArrayList<>();
    p1Turn.add(p1Move);
    p1Turn.add(p1Build);

    assertThat(rules.isTurnLegal(board, p1Turn, p1Name)).isFalse();
  }

  /**
   * Tests if the given turn is not a valid one if "PUT", "PUT" is given for a build.
   */
  @Test
  public void testNotValidTurnNoBuild() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    Action p1Move = new Action(ActionType.MOVE, "one1", new Direction("PUT", "SOUTH"));
    Action p1Build = new Action(ActionType.BUILD, "one1", new Direction("PUT", "PUT"));
    List<Action> p1Turn = new ArrayList<>();
    p1Turn.add(p1Move);
    p1Turn.add(p1Build);

    assertThat(rules.isTurnLegal(board, p1Turn, p1Name)).isFalse();
  }





  /**
   * Tests if the given move is a valid one.
   */
  @Test
  public void testValidMove() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    Action p1Move = new Action(ActionType.MOVE, "one1", new Direction("PUT", "SOUTH"));

    assertThat(rules.isMoveLegal(board, p1Move, p1Name)).isTrue();
  }

  /**
   * Tests if the given move is not a valid one if the player uses a worker that doesn't belong to them.
   */
  @Test
  public void testNotValidMoveWrongWorker() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    Action p1Move = new Action(ActionType.MOVE, "two1", new Direction("PUT", "SOUTH"));

    assertThat(rules.isMoveLegal(board, p1Move, p1Name)).isFalse();
  }


  /**
   * Tests if the given move is not a valid one if the player uses a worker that doesn't exist.
   */
  @Test
  public void testNotValidMoveWorkerDoesntExist() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    Action p1Move = new Action(ActionType.MOVE, "three1", new Direction("PUT", "SOUTH"));

    assertThat(rules.isMoveLegal(board, p1Move, p1Name)).isFalse();
  }

  /**
   * Tests if the given move is not a valid one if the player tries to stay in the same spot.
   */
  @Test
  public void testNotValidMoveStayPut() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    Action p1Move = new Action(ActionType.MOVE, "one1", new Direction("PUT", "PUT"));

    assertThat(rules.isMoveLegal(board, p1Move, p1Name)).isFalse();
  }

  /**
   * Tests if the given move is not a valid one if the player tries to move off the board.
   */
  @Test
  public void testNotValidMoveOffBoard() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    Action p1Move = new Action(ActionType.MOVE, "one1", new Direction("WEST", "PUT"));

    assertThat(rules.isMoveLegal(board, p1Move, p1Name)).isFalse();
  }

  /**
   * Tests if the given move is not a valid one if the player tries to move off onto occupied space.
   */
  @Test
  public void testNotValidMoveOccupied() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    Action p1Move = new Action(ActionType.MOVE, "one1", new Direction("EAST", "SOUTH"));
    assertThat(rules.isMoveLegal(board, p1Move, p1Name)).isFalse();
  }

  /**
   * Tests if the given move is not a valid one if the player tries to move to up 2 floors in 1 turn.
   */
  @Test
  public void testNotValidMoveTooHigh() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    board.build("one1", new Direction("PUT", "SOUTH"));
    board.build("one1", new Direction("PUT", "SOUTH"));

    Action p1Move = new Action(ActionType.MOVE, "one1", new Direction("PUT", "SOUTH"));
    assertThat(rules.isMoveLegal(board, p1Move, p1Name)).isFalse();
  }


  // ---------------- Build tests


  /**
   * Tests if the given move is a valid one.
   */
  @Test
  public void testValidBuild() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    Direction p1Build = new Direction("PUT", "SOUTH");

    assertThat(rules.isBuildLegal(board, 1, 0 , p1Build, 0,0)).isTrue();
  }

  /**
   * Tests if the given move is not a valid one if the player tries to stay in the same spot.
   */
  @Test
  public void testNotValidBuildStayPut() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    Direction p1Build = new Direction("PUT", "PUT");

    assertThat(rules.isBuildLegal(board, 1, 0 , p1Build, 0,0)).isFalse();
  }

  /**
   * Tests if the given move is not a valid one if the player tries to move off the board.
   */
  @Test
  public void testNotValidBuildOffBoard() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    Direction p1Build = new Direction("WEST", "PUT");

    assertThat(rules.isBuildLegal(board, 1, 0,
            p1Build, 0,0)).isFalse();
  }

  /**
   * Tests if the given move is not a valid one if the player tries to build onto occupied space.
   */
  @Test
  public void testNotValidBuildOccupied() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    Direction p1Build = new Direction("EAST", "PUT");

    assertThat(rules.isBuildLegal(board, 1, 0,
            p1Build, 0,0)).isFalse();
  }

  /**
   * Tests if the given build is a valid one if the player tries to build to a floor that is
   * two floors above it.
   */
  @Test
  public void testValidBuildHigh() {
    board.createWorker(p1Name, 0, 0);
    board.createWorker(p2Name, 1, 1);
    board.createWorker(p1Name, 2, 2);
    board.createWorker(p2Name, 3, 3);

    board.build("one1", new Direction("PUT", "SOUTH"));
    board.build("one1", new Direction("PUT", "SOUTH"));

    Direction p1Build = new Direction("PUT", "SOUTH");

    assertThat(rules.isBuildLegal(board, 0, 0 , p1Build,
            0,0)).isTrue();
  }



}
