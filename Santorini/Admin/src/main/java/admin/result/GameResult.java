package admin.result;

import common.interfaces.IPlayer;

/**
 * Represents the result of a game.
 */
public class GameResult {

  private IPlayer winner;
  private IPlayer loser;
  private boolean didLoserCheat;

  public GameResult(IPlayer winner, IPlayer loser, boolean didLoserCheat) {
    this.winner = winner;
    this.loser = loser;
    this.didLoserCheat = didLoserCheat;
  }

  public IPlayer getWinner() {
    return winner;
  }

  public IPlayer getLoser() {
    return loser;
  }

  public boolean didLoserCheat() {
    return didLoserCheat;
  }
}
