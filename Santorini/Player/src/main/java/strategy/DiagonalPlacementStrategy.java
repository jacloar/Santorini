package strategy;

import common.board.IReadonlyBoard;
import common.data.PlaceWorkerAction;

public class DiagonalPlacementStrategy implements IPlacementStrategy {
    @Override
    public PlaceWorkerAction getPlaceWorker(IReadonlyBoard b) {
        for (int i = 0; i < b.getMaxRows(); i += 1) {

            if (!b.isOccupied(i, i)) {
                return new PlaceWorkerAction(i, i);
            }
        }

        // Per the use of this strategy, this should NEVER occur.
        // To use this implementation with more Players, you must add proper error handling
        // or just use a better strategy
        throw new RuntimeException("Something has gone very wrong, all diagonal spots have been taken!");
    }
}
