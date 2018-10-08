package Player;

import Common.IBoard;
import Common.Posn;
import Common.Worker;

/**
 * Interface to for internal representation of a player.
 *
 * IPlayer will receive input from an administrative component
 * telling the player information about the state of the game.
 */
public interface IPlayer {

  /**
   * Gives the player a reference to a worker that belongs to the player.
   *
   * @param worker worker that belongs to this player
   */
  void addWorker(Worker worker);

  /**
   * During the startup phase of the game the admin will tell the player
   * it is their turn to place a worker on the board.
   *
   * @return posn where the strategy requested to place a worker
   */
  Posn placeWorker();


  /**
   * Tells this player that it is their turn to move. During the turn
   * the player will choose a worker, move that worker one space in any
   * direction, and then build on to an adjacent building. By the end
   * of this method the player's turn will be complete.
   *
   * @return a move request
   */
  Move completeTurn();

}

