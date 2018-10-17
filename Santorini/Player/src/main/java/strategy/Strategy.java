package strategy;

import common.board.IReadonlyBoard;
import common.data.Action;
import common.data.PlaceWorkerAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Strategy {

    private final IPlacementStrategy placementStrategy;
    private final ITurnStrategy turnStrategy;
    private final int maxTurnDepth;

    public Strategy(IPlacementStrategy placementStrategy, ITurnStrategy turnStrategy, int maxTurnDepth) {
        this.placementStrategy = placementStrategy;
        this.turnStrategy = turnStrategy;
        this.maxTurnDepth = maxTurnDepth;
    }

    /**
     * Gets the placement of the intended worker
     * @param workerdId The designated worker to try and place
     * @param b the given board to work off of
     * @return the formulated placeworker action
     */
    public PlaceWorkerAction getPlaceWorker(String workerdId, IReadonlyBoard b) {
        return placementStrategy.getPlaceWorker(workerdId, b);
    }

    /**
     * Gets the turn based on the given board.
     * @param playerName the playername to work with
     * @param b the given board to work off of
     * @return the formulated list of actions
     */
    public List<Action> getTurn(String playerName, IReadonlyBoard b) {

        List<List<Action>> posTurns = turnStrategy.getLegalMoves(playerName, b);

        List<Action> bestMove = posTurns.get(0);
        int score = turnStrategy.score(bestMove, b, maxTurnDepth);

        posTurns = posTurns.subList(1, posTurns.size() - 1);
        for (List<Action> turn : posTurns) {

            if (score > turnStrategy.score(turn, b, maxTurnDepth)) {
                bestMove = turn;
            }
        }

        return bestMove;
    }
}
