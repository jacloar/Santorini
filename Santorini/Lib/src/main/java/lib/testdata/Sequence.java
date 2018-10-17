package lib.testdata;

import common.board.Board;
import common.board.IBoard;
import common.board.ICell;
import common.data.Action;

import java.util.List;

public class Sequence {
    private final IBoard board;
    private final List<Action> actions;

    public Sequence(ICell[][] cells, List<Action> actions) {
        this.board = new Board(cells);
        this.actions = actions;
    }

    public IBoard getBoard() {
        return board;
    }

    public List<Action> getActions() {
        return actions;
    }
}
