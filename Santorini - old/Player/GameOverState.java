package Player;

/**
 * If the game is over it is a GameOverState state
 */
public class GameOverState implements IGameState {
  private boolean didWin; // Did I win?
  private Move prevMove; // The move that got the board to this state.

  public GameOverState(boolean didWin) {
    this.didWin = didWin;
  }

  public boolean isGameOver() {
    return true;
  }

  public boolean didWin() {
    return didWin;
  }

  public void setMove(Move move) {
    this.prevMove = move;
  }

  public Move getMove() {
    return this.prevMove;
  }

  public IGameState flipState() {
    return new GameOverState(!didWin);
  }
}
