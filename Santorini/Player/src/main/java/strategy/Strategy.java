package strategy;

import common.board.IReadonlyBoard;
import common.data.Action;
import common.data.PlaceWorkerAction;
import java.util.List;

public class Strategy {

    private final IPlacementStrategy placementStrategy;
    private final ITurnStrategy turnStrategy;

    public Strategy(IPlacementStrategy placementStrategy, ITurnStrategy turnStrategy) {
        this.placementStrategy = placementStrategy;
        this.turnStrategy = turnStrategy;
    }

    /**
     * Gets the placement of the intended worker
     * @param b the given board to work off of
     * @return the formulated placeworker action
     */
    public PlaceWorkerAction getPlaceWorker(IReadonlyBoard b) {
        return placementStrategy.getPlaceWorker(b);
    }

    public List<Action> getTurn(IReadonlyBoard b) {
        return turnStrategy.getTurn(b);
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.turnStrategy.setPlayer(currentPlayer);
    }

    public void setOpponent(String opponent) {
        this.turnStrategy.setOpponent(opponent);
    }
}
