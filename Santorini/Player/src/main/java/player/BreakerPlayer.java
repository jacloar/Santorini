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

  @Override
  public PlaceWorkerAction getPlaceWorker(IReadonlyBoard b) {
    return new PlaceWorkerAction("id", -1, 3);
  }

  @Override
  public List<Action> getTurn(IReadonlyBoard b) {
    List<Action> turn = new ArrayList<>();

    turn.add(new Action(ActionType.BUILD, "id", new Direction("EAST", "SOUTH")));

    return turn;
  }

  @Override
  public String getPlayerName() {
    return null;
  }

  @Override
  public void setPlayerName(String newName) {
    // do nothing
  }
}
