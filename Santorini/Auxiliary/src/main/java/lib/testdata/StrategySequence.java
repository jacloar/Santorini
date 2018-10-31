package lib.testdata;

import common.board.Board;
import common.board.IBoard;
import common.board.ICell;
import common.data.Action;

import java.util.List;

public class StrategySequence {
    private final String activePlayer;
    private final IBoard board;
    private final int depth;
    private final List<Action> actions;

    public StrategySequence(String activePlayer, ICell[][] board, int depth, List<Action> actions) {
        this.activePlayer = activePlayer;
        this.board = new Board(board);
        this.depth = depth;
        this.actions = actions;
    }


    public String getActivePlayer() {
        return activePlayer;
    }

    public int getDepth() {
        return depth;
    }


    public IBoard getBoard() {
        return board;
    }

    public List<Action> getActions() {
        return actions;
    }

    public boolean hasActions() {
        return !this.actions.isEmpty();
    }
}
