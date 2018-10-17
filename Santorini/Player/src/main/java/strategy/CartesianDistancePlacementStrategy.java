package strategy;

import common.board.IReadonlyBoard;
import common.data.PlaceWorkerAction;
import common.data.Worker;

import java.util.List;
import java.util.Map;

public class CartesianDistancePlacementStrategy implements IPlacementStrategy {

    private final String opponent;

    public CartesianDistancePlacementStrategy(String opponent) {
        this.opponent = opponent;
    }

    /**
     * Places a worker somewhere that is farthest away from the opponent using cartesian distance.
     * It is important to note that the base case where no workers have been placed results in the
     * worker to be placed at 0,0
     * @param workerId The workerId to come up with a placement with
     * @param b the given readonly board to find a spot on
     * @return the placeworker action that is farthest away from all opponents workers
     */
    @Override
    public PlaceWorkerAction getPlaceWorker(String workerId, IReadonlyBoard b) {
        Map<String, List<Worker>> playerWorkers = b.getPlayerWorkerMap();

        List<Worker> opponentWorkers = playerWorkers.get(opponent);

        // Return the base case
        if (opponentWorkers.isEmpty()) {
            return new PlaceWorkerAction(workerId, 0, 0);
        }

        PlaceWorkerAction result = null;
        double maxCombineDistance = 0;
        // Calculate a distance that is farthest from both workers
        for (int i = 0; i < b.getMaxRows(); i++) {
            for (int j = 0; j < b.getMaxColumns(); j++) {
                PlaceWorkerAction potentialPlaceAction = new PlaceWorkerAction(workerId, i, j);
                double totalDistance = 0;

                for (Worker worker : opponentWorkers) {
                    totalDistance += calculateDistance(i, j, worker.getRow(), worker.getColumn());
                }

                if (totalDistance > maxCombineDistance) {
                    maxCombineDistance = totalDistance;
                    result = potentialPlaceAction;
                }
            }
        }

        if (result == null) {
            throw new RuntimeException("Something went very wrong, no spots on board to place?");
        }

        return result;
    }

    /**
     * Calculates the distance between two sets of points (x0, y0) and (x1, y1)
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     * @return
     */
    private double calculateDistance(int x0, int y0, int x1, int y1) {
        double xDistance = Math.pow(x1 - x0, 2);
        double yDistance = Math.pow(y1 - y0, 2);
        return Math.sqrt(xDistance + yDistance);
    }
}
