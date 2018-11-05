package player;

import common.board.IReadonlyBoard;
import common.data.Action;
import common.data.PlaceWorkerAction;
import common.interfaces.IPlayer;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of player that will infinitely loop on any method call
 */
public class InfinitePlayer implements IPlayer {

  private String playerName;

  public InfinitePlayer(String name) {
    this.playerName = name;
  }

  @Override
  public PlaceWorkerAction getPlaceWorker(IReadonlyBoard b) {
    loop();
    return new PlaceWorkerAction(0, 0);
  }

  @Override
  public List<Action> getTurn(IReadonlyBoard b) {
    loop();
    return new ArrayList<>();
  }

  @Override
  public String getPlayerName() {
    loop();
    return playerName;
  }

  @Override
  public void setPlayerName(String newName) {
    loop();
  }

  @Override
  public void setOpponentName(String opponentName) {
    loop();
  }

  private void loop() {
    while (true) {

    }
  }

  @Override
  public String toString() {
    return playerName;
  }
}
