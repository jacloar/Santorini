package admin.observer;

import common.board.IReadonlyBoard;
import common.data.Action;
import java.util.List;
import player.IPlayer;

public class StdOutObserver implements IObserver {

  
  @Override
  public void update(IReadonlyBoard board) {

  }

  @Override
  public void update(IReadonlyBoard board, List<Action> turn) {

  }

  @Override
  public void updateGiveUp(IPlayer player) {

  }

  @Override
  public void updateWin(IPlayer player) {

  }

  @Override
  public void updateError(String error) {

  }
}
