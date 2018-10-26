package common.rules;

import static org.assertj.core.api.Assertions.assertThat;

import common.board.Board;
import common.board.IBoard;
import common.data.PlaceWorkerAction;
import org.junit.Test;

public class TestStandardSantoriniRulesEngine {

  @Test
  public void testPlaceWorkerEmptyBoard() {
    IRulesEngine rules = new StandardSantoriniRulesEngine();

    IBoard board = new Board();
    PlaceWorkerAction placeWorkerAction = new PlaceWorkerAction("one1", 0, 0);

    boolean isValid = rules.isPlaceWorkerLegal(board, placeWorkerAction);

    assertThat(isValid).isTrue();
  }

}
