package Player;

/**
 * This interface represents a game state. A game may be over or in progress.
 */
public interface IGameState {
  /**
   * Is the game over?
   * @return True if the game is over, false otherwise.
   */
  boolean isGameOver();

  /**
   * Given that the game is over, did I win?
   * @return True if the player whose move it is did win, false otherwise, meaning they lost.
   */
  boolean didWin();

  /**
   * Gets the move that leads to this state
   * @return The move that gets the game to the 'current' hypothetical state
   */
  Move getMove();

  /**
   * Sets the move.
   * @param move The move that we want to set as the one that leads to this hypothetical state.
   */
  void setMove(Move move);

  /**
   * Flips the workers so we get permutations for the other players move.
   * @return The state after the opponents workers and my workers have swapped.
   */
  IGameState flipState();
}
