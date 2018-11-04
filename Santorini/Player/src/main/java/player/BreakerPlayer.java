package player;

import common.board.IReadonlyBoard;
import common.data.Action;
import common.data.ActionType;
import common.data.Direction;
import common.data.PlaceWorkerAction;
import common.interfaces.IPlayer;
import java.util.ArrayList;
import java.util.List;

public class BreakerPlayer implements IPlayer {

  private String playerName;

  @Override
  public PlaceWorkerAction getPlaceWorker(IReadonlyBoard b) {
    return new PlaceWorkerAction( -1, 3);
  }

  @Override
  public List<Action> getTurn(IReadonlyBoard b) {
    List<Action> turn = new ArrayList<>();

    turn.add(new Action(ActionType.BUILD, "id", new Direction("EAST", "SOUTH")));

    return turn;
  }

  @Override
  public String getPlayerName() {
    return this.playerName;
  }

  @Override
  public void setPlayerName(String newName) {
    this.playerName = newName;
  }

  @Override
  public void setOpponentName(String opponentName) {
    // do nothing
  }
}
