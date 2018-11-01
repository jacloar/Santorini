package strategy;

import common.board.IReadonlyBoard;
import common.data.PlaceWorkerAction;

public interface IPlacementStrategy {
    /**
     * Get a "place worker" action that will place a worker on the common.board
     */
    PlaceWorkerAction getPlaceWorker(IReadonlyBoard b);
}
