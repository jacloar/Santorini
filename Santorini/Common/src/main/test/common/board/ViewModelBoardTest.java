package common.board;

import common.data.Direction;
import common.data.Worker;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ViewModelBoardTest {

  /**
   * Test to see if creating a new viewModelBoard has the fields.
   */
  @Test
  public void testNewViewModelBoard() {
    IBoard board = new Board();
    IReadonlyBoard readOnlyBoard = board.toViewModel();

    assertEquals(board.getPlayerWorkerMap() ,readOnlyBoard.getPlayerWorkerMap());
    assertThat(readOnlyBoard.getMaxColumns()).isEqualTo(6);
    assertThat(readOnlyBoard.getMaxRows()).isEqualTo(6);
    assertThat(board.getPlayerWorkerMap()).isEqualTo(readOnlyBoard.getPlayerWorkerMap());

  }

  /**
   * moves, building, and adding workers should mutate the board.
   * also tests moving and building
   */
  @Test
  public void testMoveChangesBoardCopy() {
    IBoard board = new Board();
    IReadonlyBoard readOnlyBoard = board.toViewModel();
    board.createWorker("Luke1", 1, 1);
    Direction ne = new Direction("EAST", "NORTH");
    Direction s = new Direction("PUT", "SOUTH");
    board.move("Luke1", ne);
    board.build("Luke1", s);

    assertThat(board.getPlayerWorkerMap().size()).isEqualTo(readOnlyBoard.getPlayerWorkerMap().size());
    assertThat(board.getPlayerWorkers("Luke").size()).isEqualTo(1);
    assertThat(board.getPlayerWorkers("Luke").size()).isEqualTo(readOnlyBoard.getPlayerWorkers("Luke").size());
    assertThat(readOnlyBoard.height(2,2)).isEqualTo(0);
    assertThat(readOnlyBoard.height(1,2)).isEqualTo(1);

    board.createWorker("Luke2", 1, 2);
    assertTrue(readOnlyBoard.isOccupied(1,2));
    assertTrue(readOnlyBoard.isOccupied(0,2));
    Direction e = new Direction("EAST", "PUT");
    assertTrue(readOnlyBoard.isNeighbor("Luke1", e));

    Worker l1 = readOnlyBoard.findWorker("Luke1");
    assertThat(l1.getRow()).isEqualTo(0);
    assertThat(l1.getColumn()).isEqualTo(2);
  }

}
