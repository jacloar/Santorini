package common.data;

/**
 * This class is a storage object used to communicate information about workers in the game common.board, without
 * actually being those workers, which are more conveniently stored as BuildingWorkers. This separation also serves to
 * allow us to bundle information about workers while still having a single source of truth.
 */
public class Worker {
    // a string representing the name of the IPlayer
    private String playerName;
    // the number of this worker, i.e. 1 or 2
    private int workerNumber;
    // the current coordinates of the worker
    private int row;
    private int column;

    // all fields are provided in the constructor and can't be mutated
    public Worker(String playerName, int workerNumber, int row, int column) {
        this.playerName = playerName;
        this.workerNumber = workerNumber;
        this.row = row;
        this.column = column;
    }

    /**
     * Below are getter methods for all fields
     */

    public String getPlayerName() {
        return this.playerName;
    }

    public int getWorkerNumber() {
        return this.workerNumber;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public String getWorkerId() { return String.format("%s%s", this.playerName, this.workerNumber); }
}
