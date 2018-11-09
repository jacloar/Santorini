package player;

import common.board.IReadonlyBoard;
import common.data.Action;
import common.data.PlaceWorkerAction;
import common.interfaces.IPlayer;
import java.util.List;
import java.util.UUID;
import strategy.Strategy;

public class AIPlayer implements IPlayer {

    private String playerName;
    private final Strategy strategy;

    public AIPlayer(Strategy strategy) {
        this(UUID.randomUUID().toString(), strategy);
    }

    public AIPlayer(String name, Strategy strategy) {
        this.playerName = name;
        this.strategy = strategy;
        this.strategy.setCurrentPlayer(name);
    }

    @Override
    public PlaceWorkerAction getPlaceWorker(IReadonlyBoard b) {
        return strategy.getPlaceWorker(b);
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
    public void setPlayerName(String newName) {
        this.playerName = newName;
        strategy.setCurrentPlayer(newName);
    }

    @Override
    public String toString() {
        return playerName;
    }

    public void setOpponentName(String opponentName) {
        strategy.setOpponent(opponentName);
    }
}
