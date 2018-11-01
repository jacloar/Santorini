package common.data;

/**
 * A PlaceWorkerAction contains all information necessary for placing a worker on the common.board
 */
public class PlaceWorkerAction {
    // coordinates of where the worker is being placed
    private int row;
    private int column;

    public PlaceWorkerAction(int row, int column) {
        this.row = row;
        this.column = column;
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
