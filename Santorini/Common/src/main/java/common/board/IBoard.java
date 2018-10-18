package common.board;

import common.data.Direction;

/**
 * common.board.IBoard
 * Serves as the interface for any implementation of a common.board
 * in the game Santorini
 */
public interface IBoard extends IReadonlyBoard {
    // create a common.board.BuildingWorker at the given coordinates and return it
    BuildingWorker createWorker(String workerId, int row, int column);

    // move the worker designated by the string in the given direction
    void move(String worker, Direction direction);

    // adds floor to building in the direction relative to the given worker
    int build(String worker, Direction direction);

    // returns a ViewModel from this common.board
    IReadonlyBoard toViewModel();
}
