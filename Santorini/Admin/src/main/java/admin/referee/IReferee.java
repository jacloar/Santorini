package admin.referee;

import common.interfaces.IObserver;
import common.board.IBoard;
import java.util.Optional;
import common.interfaces.IPlayer;

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
  IPlayer playGame(IPlayer player1, IPlayer player2);

  /**
   * Facilitates a game between two given IPlayers on the given IBoard
   *
   * @param board IBoard to play the game on
   * @param player1 one IPlayer
   * @param player2 another IPlayer
   * @return the player who won the game
   */
  IPlayer playGame(IBoard board, IPlayer player1, IPlayer player2);

  /**
   * Facilitates playing {@code games} games between the given players. Returns the IPlayer that
   * won the most games, or and empty Optional if there was a tie.
   *
   * @param player1 one IPlayer
   * @param player2 the other IPlayer
   * @param games number of games to play
   * @return IPlayer that won the most games, or empty if tied
   */
  Optional<IPlayer> bestOfN(IPlayer player1, IPlayer player2, int games);

  /**
   * Adds the given observer to this referee's list of observers.
   *
   * @param observer IObserver to add
   */
  void addObserver(IObserver observer);
}