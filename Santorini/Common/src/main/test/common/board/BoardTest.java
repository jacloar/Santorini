package common.board;

import static org.assertj.core.api.Assertions.assertThat;

import common.data.Direction;
import org.junit.Test;

public class BoardTest {

  /**
   * Tests that a new empty board is empty.
   */
  @Test
  public void testNewBoardEmpty() {
    Board board = new Board();

    // Check that the height of every cell is 0
    for (int i = 0; i < 6; i += 1) {
      for (int j = 0; j < 6; j += 1) {
        assertThat(board.height(0, 0)).isEqualTo(0);
      }
    }

    // Check that there are no workers
    assertThat(board.getPlayerWorkerMap()).isEmpty();
  }

  /**
   * Tests that creating a worker adds it to the board
   */
  @Test
  public void testCreateWorker() {
    IBoard board = new Board();

    BuildingWorker worker = board.createWorker("one1", 0, 0);

    assertThat(board.getPlayerWorkerMap()).isNotEmpty()
                                          .hasSize(1)
                                          .containsKey("one");

    assertThat(board.getPlayerWorkerMap().get("one")).isNotEmpty()
                                                     .hasSize(1);

    assertThat(board.getPlayerWorkerMap().get("one").get(0).getPlayerName()).isEqualTo("one");
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getWorkerId()).isEqualTo("one1");
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getWorkerNumber()).isEqualTo(1);
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getColumn()).isEqualTo(0);
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getRow()).isEqualTo(0);

    assertThat(worker.getPlayerName()).isEqualTo("one");
    assertThat(worker.getWorkerNumber()).isEqualTo(1);
  }

  /**
   * Tests that creating a different worker adds it to the board
   */
  @Test
  public void testCreateWorker2() {
    IBoard board = new Board();

    BuildingWorker worker = board.createWorker("two2", 3, 2);

    assertThat(board.getPlayerWorkerMap()).isNotEmpty()
                                          .hasSize(1)
                                          .containsKey("two");

    assertThat(board.getPlayerWorkerMap().get("two")).isNotEmpty()
                                                     .hasSize(1);

    assertThat(board.getPlayerWorkerMap().get("two").get(0).getPlayerName()).isEqualTo("two");
    assertThat(board.getPlayerWorkerMap().get("two").get(0).getWorkerId()).isEqualTo("two2");
    assertThat(board.getPlayerWorkerMap().get("two").get(0).getWorkerNumber()).isEqualTo(2);
    assertThat(board.getPlayerWorkerMap().get("two").get(0).getColumn()).isEqualTo(2);
    assertThat(board.getPlayerWorkerMap().get("two").get(0).getRow()).isEqualTo(3);

    assertThat(worker.getPlayerName()).isEqualTo("two");
    assertThat(worker.getWorkerNumber()).isEqualTo(2);
  }

  /**
   * Tests that creating multiple workers for different players makes distinct workers
   */
  @Test
  public void testCreateMultipleWorkersDifferentPlayers() {
    IBoard board = new Board();

    BuildingWorker worker1 = board.createWorker("one1", 0, 0);
    BuildingWorker worker2 = board.createWorker("two2", 3, 2);

    assertThat(board.getPlayerWorkerMap()).isNotEmpty()
                                          .hasSize(2)
                                          .containsKey("one")
                                          .containsKey("two");

    assertThat(board.getPlayerWorkerMap().get("one")).isNotEmpty()
                                                     .hasSize(1);
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getPlayerName()).isEqualTo("one");
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getWorkerId()).isEqualTo("one1");
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getWorkerNumber()).isEqualTo(1);
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getColumn()).isEqualTo(0);
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getRow()).isEqualTo(0);

    assertThat(board.getPlayerWorkerMap().get("two")).isNotEmpty()
                                                     .hasSize(1);
    assertThat(board.getPlayerWorkerMap().get("two").get(0).getPlayerName()).isEqualTo("two");
    assertThat(board.getPlayerWorkerMap().get("two").get(0).getWorkerId()).isEqualTo("two2");
    assertThat(board.getPlayerWorkerMap().get("two").get(0).getWorkerNumber()).isEqualTo(2);
    assertThat(board.getPlayerWorkerMap().get("two").get(0).getColumn()).isEqualTo(2);
    assertThat(board.getPlayerWorkerMap().get("two").get(0).getRow()).isEqualTo(3);
    
    assertThat(worker1).isNotEqualTo(worker2);

    assertThat(worker1.getPlayerName()).isEqualTo("one");
    assertThat(worker1.getWorkerNumber()).isEqualTo(1);

    assertThat(worker2.getPlayerName()).isEqualTo("two");
    assertThat(worker2.getWorkerNumber()).isEqualTo(2);
  }

  /**
   * Tests that creating multiple workers for the same player makes distinct workers
   */
  @Test
  public void testCreateMultipleWorkersSamePlayer() {
    IBoard board = new Board();

    BuildingWorker worker1 = board.createWorker("one1", 0, 0);
    BuildingWorker worker2 = board.createWorker("one2", 3, 2);

    assertThat(board.getPlayerWorkerMap()).isNotEmpty()
                                          .hasSize(1)
                                          .containsKey("one");

    assertThat(board.getPlayerWorkerMap().get("one")).isNotEmpty()
                                                     .hasSize(2);
    
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getPlayerName()).isEqualTo("one");
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getWorkerId()).isEqualTo("one1");
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getWorkerNumber()).isEqualTo(1);
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getColumn()).isEqualTo(0);
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getRow()).isEqualTo(0);

    assertThat(board.getPlayerWorkerMap().get("one").get(1).getPlayerName()).isEqualTo("one");
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getWorkerId()).isEqualTo("one2");
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getWorkerNumber()).isEqualTo(2);
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getColumn()).isEqualTo(2);
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getRow()).isEqualTo(3);

    assertThat(worker1).isNotEqualTo(worker2);

    assertThat(worker1.getPlayerName()).isEqualTo("one");
    assertThat(worker1.getWorkerNumber()).isEqualTo(1);

    assertThat(worker2.getPlayerName()).isEqualTo("one");
    assertThat(worker2.getWorkerNumber()).isEqualTo(2);
  }

  /**
   * Tests that move worker moves the worker in the specified direction
   */
  @Test
  public void testMoveWorker() {
    IBoard board = new Board();

    BuildingWorker worker1 = board.createWorker("one1", 0, 0);
    BuildingWorker worker2 = board.createWorker("one2", 3, 2);

    assertThat(board.getPlayerWorkerMap()).isNotEmpty()
                                          .hasSize(1)
                                          .containsKey("one");

    assertThat(board.getPlayerWorkerMap().get("one")).isNotEmpty()
                                                     .hasSize(2);

    assertThat(board.getPlayerWorkerMap().get("one").get(0).getPlayerName()).isEqualTo("one");
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getWorkerId()).isEqualTo("one1");
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getWorkerNumber()).isEqualTo(1);
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getColumn()).isEqualTo(0);
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getRow()).isEqualTo(0);

    assertThat(board.getPlayerWorkerMap().get("one").get(1).getPlayerName()).isEqualTo("one");
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getWorkerId()).isEqualTo("one2");
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getWorkerNumber()).isEqualTo(2);
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getColumn()).isEqualTo(2);
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getRow()).isEqualTo(3);

    assertThat(worker1).isNotEqualTo(worker2);

    assertThat(worker1.getPlayerName()).isEqualTo("one");
    assertThat(worker1.getWorkerNumber()).isEqualTo(1);

    assertThat(worker2.getPlayerName()).isEqualTo("one");
    assertThat(worker2.getWorkerNumber()).isEqualTo(2);

    // Moves the worker!
    board.move("one1", new Direction("EAST", "SOUTH"));

    assertThat(board.getPlayerWorkerMap()).isNotEmpty()
                                          .hasSize(1)
                                          .containsKey("one");

    assertThat(board.getPlayerWorkerMap().get("one")).isNotEmpty()
                                                     .hasSize(2);

    assertThat(board.getPlayerWorkerMap().get("one").get(0).getPlayerName()).isEqualTo("one");
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getWorkerId()).isEqualTo("one1");
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getWorkerNumber()).isEqualTo(1);

    // This is what changed
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getColumn()).isEqualTo(1);
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getRow()).isEqualTo(1);

    assertThat(board.getPlayerWorkerMap().get("one").get(1).getPlayerName()).isEqualTo("one");
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getWorkerId()).isEqualTo("one2");
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getWorkerNumber()).isEqualTo(2);
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getColumn()).isEqualTo(2);
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getRow()).isEqualTo(3);

    assertThat(worker1).isNotEqualTo(worker2);

    assertThat(worker1.getPlayerName()).isEqualTo("one");
    assertThat(worker1.getWorkerNumber()).isEqualTo(1);

    assertThat(worker2.getPlayerName()).isEqualTo("one");
    assertThat(worker2.getWorkerNumber()).isEqualTo(2);
  }

  @Test
  public void testBuild() {
    IBoard board = new Board();

    BuildingWorker worker1 = board.createWorker("one1", 0, 0);
    BuildingWorker worker2 = board.createWorker("one2", 3, 2);

    assertThat(board.getPlayerWorkerMap()).isNotEmpty()
                                          .hasSize(1)
                                          .containsKey("one");

    assertThat(board.getPlayerWorkerMap().get("one")).isNotEmpty()
                                                     .hasSize(2);

    assertThat(board.getPlayerWorkerMap().get("one").get(0).getPlayerName()).isEqualTo("one");
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getWorkerId()).isEqualTo("one1");
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getWorkerNumber()).isEqualTo(1);
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getColumn()).isEqualTo(0);
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getRow()).isEqualTo(0);

    assertThat(board.getPlayerWorkerMap().get("one").get(1).getPlayerName()).isEqualTo("one");
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getWorkerId()).isEqualTo("one2");
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getWorkerNumber()).isEqualTo(2);
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getColumn()).isEqualTo(2);
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getRow()).isEqualTo(3);

    assertThat(worker1).isNotEqualTo(worker2);

    assertThat(worker1.getPlayerName()).isEqualTo("one");
    assertThat(worker1.getWorkerNumber()).isEqualTo(1);

    assertThat(worker2.getPlayerName()).isEqualTo("one");
    assertThat(worker2.getWorkerNumber()).isEqualTo(2);

    assertThat(board.height(1, 1)).isEqualTo(0);
    assertThat(board.height("one1", new Direction("EAST", "SOUTH"))).isEqualTo(0);

    // Build statement
    board.build("one1", new Direction("EAST", "SOUTH"));

    // Test that height increased
    assertThat(board.height(1, 1)).isEqualTo(1);
    assertThat(board.height("one1", new Direction("EAST", "SOUTH"))).isEqualTo(1);

    // Test that nothing else changed
    assertThat(board.getPlayerWorkerMap()).isNotEmpty()
                                          .hasSize(1)
                                          .containsKey("one");

    assertThat(board.getPlayerWorkerMap().get("one")).isNotEmpty()
                                                     .hasSize(2);

    assertThat(board.getPlayerWorkerMap().get("one").get(0).getPlayerName()).isEqualTo("one");
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getWorkerId()).isEqualTo("one1");
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getWorkerNumber()).isEqualTo(1);
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getColumn()).isEqualTo(0);
    assertThat(board.getPlayerWorkerMap().get("one").get(0).getRow()).isEqualTo(0);

    assertThat(board.getPlayerWorkerMap().get("one").get(1).getPlayerName()).isEqualTo("one");
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getWorkerId()).isEqualTo("one2");
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getWorkerNumber()).isEqualTo(2);
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getColumn()).isEqualTo(2);
    assertThat(board.getPlayerWorkerMap().get("one").get(1).getRow()).isEqualTo(3);

    assertThat(worker1).isNotEqualTo(worker2);

    assertThat(worker1.getPlayerName()).isEqualTo("one");
    assertThat(worker1.getWorkerNumber()).isEqualTo(1);

    assertThat(worker2.getPlayerName()).isEqualTo("one");
    assertThat(worker2.getWorkerNumber()).isEqualTo(2);
  }

  @Test
  public void testIsOccupiedEmpty() {
    IBoard board = new Board();

    boolean isOccupied = board.isOccupied(0, 0);

    assertThat(isOccupied).isFalse();
  }
}
