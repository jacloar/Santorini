package Common;

/**
 * Interface to for internal representation of a player.
 *
 * IPlayer will receive input from an administrative component
 * telling the player information about the state of the game.
 */
public interface IPlayer {

  /**
   * During the startup phase of the game the admin will tell the player
   * it is their turn to place a worker on the board.
   */
  void placeWorker();


  /**
   * Gives the player the initial copy of the board so they can see the state
   * of the game. Through object reference this board copy will reflect the
   * current state of the game.
   *
   * @param board the copy of the board to allow players to make decisions
   */
  void receiveBoard(IBoard board);


  /**
   * Tells this player that it is their turn to move. During the turn
   * the player will choose a worker, move that worker one space in any
   * direction, and then build on to an adjacent building. By the end
   * of this method the player's turn will be complete.
   */
  void completeTurn();

  /**
   *
   * Tells the player who won the game after the admin has declared
   * that the game is over.
   *
   * @param didWin true if the player won, false if they lost.
   */
  void gameOver(boolean didWin);
  

}

