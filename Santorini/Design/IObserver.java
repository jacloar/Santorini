import common.board.IReadonlyBoard;
import player.IPlayer;

/**
 * IObserver is an interface that can be used to watch a game's progress as it occurs.
 */
public interface IObserver {

  /**
   * Returns a read only copy of the board. From this copy, the observer can know the height
   * of every cell, the locations of all the workers, which workers belong to which player,
   * the dimensions of the board.
   *
   * @return IReadonlyBoard representing the board
   */
  public IReadonlyBoard getBoard();

  /**
   * Returns the player who is currently making their turn.
   *
   * @return the active player
   */
  public IPlayer getCurrentTurn();

  /**
   * Returns a map of IPlayers to integers representing the number of games that
   * each player has won.
   *
   * @return map representing score board
   */
  public Map<IPlayer, Integer> getScores();

  /**
   * Returns a list of the players currently playing against each other.
   *
   * @return IPlayers in current game
   */
  public List<IPlayer> getPlayers();
}