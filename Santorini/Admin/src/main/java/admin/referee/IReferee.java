package admin.referee;

import admin.result.GameResult;
import common.board.IBoard;
import common.interfaces.IObserver;
import common.interfaces.IPlayer;
import java.util.List;

/**
 * An IReferee interface represents the knowledge of how to coordinate and organize IPlayers in a
 * game of Santorini.
 */
public interface IReferee {

  /**
   * Facilitates a game between two given IPlayers on a new IBoard
   *
   * @param player1 one IPlayer
   * @param player2 the other IPlayer
   * @return the player who won the game
   */
  GameResult playGame(IPlayer player1, IPlayer player2);

  /**
   * Facilitates a game between two given IPlayers on the given IBoard
   *
   * @param board IBoard to play the game on
   * @param player1 one IPlayer
   * @param player2 another IPlayer
   * @return the player who won the game
   */
  GameResult playGame(IBoard board, IPlayer player1, IPlayer player2);

  /**
   * Facilitates playing {@code games} games between the given players. Returns a list of
   * GameResults with one for each game played.
   *
   * @param player1 one IPlayer
   * @param player2 the other IPlayer
   * @param games number of games to play
   * @return A list of GameResults with each result corresponding to a played game
   */
  List<GameResult> bestOfN(IPlayer player1, IPlayer player2, int games);

  /**
   * Adds the given observer to this referee's list of observers.
   *
   * @param observer IObserver to add
   */
  void addObserver(IObserver observer);
}