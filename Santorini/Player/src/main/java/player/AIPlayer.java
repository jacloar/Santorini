package player;

import common.board.IReadonlyBoard;
import common.data.Action;
import common.data.PlaceWorkerAction;
import common.data.Worker;
import strategy.Strategy;

import java.util.List;
import java.util.UUID;

public class AIPlayer implements IPlayer {

    private static final String WORKER_ID = "%s%d";

    private final String playerName;
    private final Strategy strategy;

    public AIPlayer(Strategy strategy) {
        this(UUID.randomUUID().toString(), strategy);
    }

    public AIPlayer(String name, Strategy strategy) {
        this.playerName = name;
        this.strategy = strategy;
    }

    @Override
    public PlaceWorkerAction getPlaceWorker(IReadonlyBoard b) {
        List<Worker> workersOnBoard = b.getPlayerWorkers(getPlayerName());
        int nextWorkerNumber = workersOnBoard.size() + 1;
        String workerToPlace = String.format(WORKER_ID, playerName, nextWorkerNumber);

        return strategy.getPlaceWorker(workerToPlace, b);
    }

    @Override
    public List<Action> getTurn(IReadonlyBoard b) {
        return strategy.getTurn(playerName, b);
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public int howFooAmI() {
        // to be implemented later when we learn what "foo" means
        return 0;
    }
}
