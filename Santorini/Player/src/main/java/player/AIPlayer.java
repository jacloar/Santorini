package player;

import common.board.IReadonlyBoard;
import common.data.Action;
import common.data.PlaceWorkerAction;
import common.interfaces.IPlayer;
import java.util.List;
import java.util.UUID;
import strategy.Strategy;

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
        int nextWorkerNumber = b.getNumWorkers(playerName) + 1;
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
    public String toString() {
        return playerName;
    }
}
