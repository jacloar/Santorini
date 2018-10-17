package strategy;

import common.board.IReadonlyBoard;
import common.data.PlaceWorkerAction;

public class DiagonalPlacementStrategy implements IPlacementStrategy {
    @Override
    public PlaceWorkerAction getPlaceWorker(String workerId, IReadonlyBoard b) {
        int x = 0;
        int y = 0;

        while (x < b.getMaxRows() && y < b.getMaxColumns()) {

            if (!b.isOccupied(x, y)) {
                return new PlaceWorkerAction(workerId, x, y);
            }

            x++;
            y++;

            x++;
            y++;
        }

        // Per the use of this strategy, this should NEVER occur.
        // To use this implementation with more Players, you must add proper error handling
        // or just use a better strategy
        throw new RuntimeException("Something has gone very wrong, all diagonal spots have been taken!");
    }
}
