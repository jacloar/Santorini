package common.board;

/**
 * An common.board.ICell represents a cell in a game common.board of Santorini
 */
public interface ICell {
    // get the height at this common.board.ICell
    int getHeight();

    // get the workerId at this common.board.ICell
    String getPlayerName();

    // return whether there is a worker there
    boolean isWorker();

    // get the number worker this is, i.e. the first or second of a player's workers
    int getWorkerNumber();

    // generates a copy of the given cell
    ICell copy();
}
