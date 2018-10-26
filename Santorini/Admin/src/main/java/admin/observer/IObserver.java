package admin.observer;

import common.board.IReadonlyBoard;
import common.data.Action;
import java.util.List;
import common.interfaces.IPlayer;

/**
 * Interface for an observer that the referee will update with state changes.
 */
public interface IObserver {

  /**
   * Updates the observer with just a board
   *
   * @param board board to give to the observer
   */

  void update(IReadonlyBoard board);
  /**
   * Updates the observer with the last turn taken
   *
   * @param turn last turn taken
   */
  void update(List<Action> turn);

  /**
   * Updates the observer with a player that is giving up
   * (ie, this player is unable to move)
   *
   * @param player player that gives up
   */
  void updateGiveUp(IPlayer player);

  /**
   * Updates the observer with the player that won the game.
   *
   * @param player player that won
   */
  void updateWin(IPlayer player);

  /**
   * Tells the observer there was an error (e.g. a player cheated)
   *
   * @param error error message
   */
  void updateError(String error);
}
