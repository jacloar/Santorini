package admin.referee;

import admin.result.GameResult;
import java.util.Optional;
import player.IPlayer;

/**
 * An IReferee interface represents the knowledge of how to coordinate and organize IPlayers in a
 * game of Santorini.
 */
public interface IReferee {

  /**
   * Facilitates a game between two given IPlayers
   *
   * @param player1 one IPlayer
   * @param player2 the other IPlayer
   * @return the player who won the game
   */
  IPlayer playGame(IPlayer player1, IPlayer player2);

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
}