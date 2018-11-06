package admin.tournament;

import admin.result.GameResult;
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
   * @param players The players participating in the tournament. Must have at least 2 players
   * @return a list of winning players (in the case of a tie) or an empty list if a tournament was not run
   */
  List<IPlayer> runTournament(List<IPlayer> players);

  /**
   * Reads the input configuration for this tournament manager and returns the maybe player who won
   *
   * @return the winning players of the tournament, or an empty list of a tournament was not run
   */
  List<IPlayer> readInput();


  /**
   * Returns the results of the tournament if one has been run.
   *
   * @return a list of GameResults representing the results of the tournament,
   *         or empty if a tournament has not been run.
   */
  Optional<List<GameResult>> getResults();

  /**
   * Returns the list of cheating players if a tournament has been run.
   *
   * @return a list of the names of cheating players,
   *         or empty of a tournament has not been run.
   */
  Optional<List<String>> getCheatersNames();

}

