/**
 *
 * The tournament manager allows a number of players to participate in an round-robin tournament.
 * To start a tournament a user needs to call runTournament with a list of players.
 *
 * If a player breaks, disconnects, or cheats they are booted from the tournament.
 *
 * The results will be returned as the player who won the most matches.
 *
 * If two players won the same amount of games the winner of the head to head matchup
 * between those two players is the tournament winner.
 *
 * If more than two players had the same amount of victories and a winner cannot be determined
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

  /**
   *
   * This method runs a round-robin tournament of Santorini games and determines a winner.
   *
   * @param players The players participating in the tournament
   * @param bestOf is the number of games that each player will play against eachother
   * @return the winning player of the tournament or empty if a winner could not be
   * determined
   */
  Optional<IPlayer> runTournament(List<IPlayer> players, int bestOf);




  // INFORMATION FOR IMPLEMENTATION:

  // Each time runTournament is called it will create a new referee.
  // the new referee will be used to obtain the results of each individual
  // matchup.

  // Tournaments will run one game at a time.

  // We will keep track of wins using a map of Player -> Wins

  // A crashing/cheating player, once discovered, will not play any more games in the
  // tournament and their win count will be set to 0.
  // Also each future matchup they have will result in an automatic loss and players that
  // they have already played, if they were defeated, will be awarded a victory.

  // Each tournament will have a list of GameResults (with added field for loser). Using
  // this list a tournament manager can manage retroactively dealing with cheaters.




}

