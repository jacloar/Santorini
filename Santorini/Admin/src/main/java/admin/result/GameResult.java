package admin.result;

import common.interfaces.IPlayer;

/**
 * Represents the result of a game.
 */
public class GameResult {

  private IPlayer winner;
  private boolean didOtherPlayerCheat;

  public GameResult(IPlayer winner, boolean didOtherPlayerCheat) {
    this.winner = winner;
    this.didOtherPlayerCheat = didOtherPlayerCheat;
  }

  public IPlayer getWinner() {
    return winner;
  }

  public boolean didOtherPlayerCheat() {
    return didOtherPlayerCheat;
  }
}
