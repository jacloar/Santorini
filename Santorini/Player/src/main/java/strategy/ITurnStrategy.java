package strategy;

import common.board.IReadonlyBoard;
import common.data.Action;

import java.util.List;

public interface ITurnStrategy {

    List<Action> getTurn(IReadonlyBoard b);

    void setPlayer(String playerName);

    void setOpponent(String opponentName);
}
