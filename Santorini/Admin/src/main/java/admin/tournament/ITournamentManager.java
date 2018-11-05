package admin.tournament;

import common.interfaces.IPlayer;
import java.util.List;
import java.util.Optional;

/**
 *
 * The tournament manager allows a number of players to participate in an round-robin tournament.
 * To start a tournament a user needs to call runTournament with a list of players.
 *
 * If a player breaks, disconnects, or cheats they are booted from the tournament.
 *
 * The results will be returned as the player who won the most matches.
 *
 * If any amount of  players had the same amount of victories and a winner cannot be determined
 * empty will be returned.
 *
 * A tournament must have at least 2 players.
 *
 */
public interface ITournamentManager {
  /**
   *
   * This method runs a round-robin tournament of Santorini games and determines a winner.
   *
   * @param players The players participating in the tournament
   * @return the winning player of the tournament or empty if a winner could not be
   * determined
   */
  Optional<IPlayer> runTournament(List<IPlayer> players);


  Optional<IPlayer> readInput();

}

