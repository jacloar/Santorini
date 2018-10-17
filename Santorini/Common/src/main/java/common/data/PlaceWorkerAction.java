package common.data;

/**
 * A PlaceWorkerAction contains all information necessary for placing a worker on the common.board
 */
public class PlaceWorkerAction {
    // the worker id of the worker being placed
    String workerId;
    // coordinates of where the worker is being placed
    int row;
    int column;

    public PlaceWorkerAction(String workerId, int row, int column) {
        this.workerId = workerId;
        this.row = row;
        this.column = column;
    }

    // get the worker id
    public String getWorkerId() {
        return this.workerId;
    }

    // get the row of placement
    public int getRow() {
        return this.row;
    }

    // get the column of placement
    public int getColumn() {
        return this.column;
    }
}
