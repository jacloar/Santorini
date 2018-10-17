package common.board;

import common.data.Direction;
import common.data.Worker;

public class Board implements IBoard {

    private static final int DEFAULT_ROWS = 6;
    private static final int DEFAULT_COLUMNS = 6;

    private final ICell[][] cells;

    public Board() {
        this.cells = new ICell[DEFAULT_ROWS][DEFAULT_COLUMNS];

        for (int i = 0; i < DEFAULT_ROWS; i++) {
            for (int j = 0; j < DEFAULT_COLUMNS; j++) {
                this.cells[i][j] = new Height(0);
            }
        }
    }

    public Board(ICell[][] cells) {
        this.cells = cells;
    }

    // Creates a worker at the given row and column
    @Override
    public BuildingWorker createWorker(String workerId, int row, int column) {
        int len = workerId.length();
        int workerNumber = Integer.parseInt(workerId.substring(len - 1));
        String playerName = workerId.substring(0, len - 1);
        int buildingHeight = this.cells[row][column].getHeight();
        BuildingWorker newWorker = new BuildingWorker(playerName, workerNumber, buildingHeight);
        this.cells[row][column] = newWorker;
        return newWorker;
    }

    // move the worker designated by the string in the given direction
    @Override
    public void move(String worker, Direction direction) {
        Worker currentWorker = this.findWorker(worker);
        int currentRow = currentWorker.getRow();
        int currentColumn = currentWorker.getColumn();
        Height originHeight = new Height(this.cells[currentRow][currentColumn].getHeight());
        int newRow =  currentRow + direction.getRowModifier();
        int newColumn =  currentColumn + direction.getColumnModifier();
        int destinationHeight = this.cells[newRow][newColumn].getHeight();
        BuildingWorker newWorker = new BuildingWorker(currentWorker.getPlayerName(), currentWorker.getWorkerNumber(), destinationHeight);
        this.cells[newRow][newColumn] = newWorker;
        this.cells[currentWorker.getRow()][currentWorker.getColumn()] = originHeight;
    }

    // adds floor to building in the direction relative to the given worker
    @Override
    public int build(String worker, Direction direction) {
        Worker currentWorker = this.findWorker(worker);
        int newRow = currentWorker.getRow() + direction.getRowModifier();
        int newColumn = currentWorker.getColumn() + direction.getColumnModifier();
        ICell targetCell = this.cells[newRow][newColumn];
        int newHeight = targetCell.getHeight() + 1;
        Height newCell = new Height(newHeight);
        this.cells[newRow][newColumn] = newCell;
        return newHeight;
    }

    /**
     * Used to construct an immutable viewmodel of the common.board
     * @return IReadonlyBoard which is immutable and offers informational methods on the common.board
     * <b>NOTE: Everything returned is a <i>deep copy</i></b>
     */
    @Override
    public IReadonlyBoard toViewModel() {
        int rows = this.cells.length;
        ICell[][] copy = new ICell[rows][];
        for (int i = 0; i < rows; i++) {
            int columns = this.cells[i].length;
            copy[i] = new ICell[columns];

            for (int j = 0; j < columns; j++) {
                copy[i][j] = this.cells[i][j].copy();
            }
        }

        return new ViewModelBoard(copy);
    }

    // Convenience method to allow this common.board to get our 'Worker' class for doing operations with.
    private Worker findWorker(String workerId) {
        int len = workerId.length();
        int workerNumber = Integer.parseInt(workerId.substring(len - 1));
        String playerName = workerId.substring(0, len - 1);
        for (int row = 0; row < this.cells.length; row++) {
            for (int column = 0; column < this.cells[row].length; column++) {
                ICell currentCell = this.cells[row][column];
                if (currentCell.isWorker()) {
                    if (currentCell.getPlayerName().equals(playerName) && currentCell.getWorkerNumber() == workerNumber) {
                        return new Worker(currentCell.getPlayerName(), currentCell.getWorkerNumber(), row, column);
                    }
                }
            }
        }
        return null;
    }
}
