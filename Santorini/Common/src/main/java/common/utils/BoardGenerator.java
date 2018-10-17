package common.utils;

import common.board.IBoard;
import common.board.IReadonlyBoard;
import common.data.Action;

import java.util.List;

/**
 * Used as a static helper to apply an action to a given Viewmodel of a common.board
 * such that
 */
public class BoardGenerator {

    public static IReadonlyBoard generateSuccessor(IReadonlyBoard readonlyBoard, List<Action> actions) {
        final IBoard board = readonlyBoard.toBoard();
        actions.forEach(a -> apply(board, a));
        return board.toViewModel();
    }

    private static void apply(IBoard board, Action action) {
        switch (action.getType()) {
            case MOVE:
                board.move(action.getWorkerId(), action.getDirection());
                break;
            case BUILD:
                board.build(action.getWorkerId(), action.getDirection());
                break;
        }
    }
}
