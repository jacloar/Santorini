import admin.result.GameResult;

/**
 * Interface for an observer that gets updates from the tournament
 */
public interface ITournamentObserver {

  /**
   * Updates the observer with the names of the players.
   *
   * @param players names of the players
   */
  void updatePlayers(IList<String> players);

  /**
   * Updates the observer with information concerning the result of a game
   *
   * @param winner winner of the game
   * @param loser loser of the game
   * @param didLoserCheat did the loser cheat in this game?
   */
  void updateGame(String winner, String loser, boolean didLoserCheat);

  /**
   * Updates the observer with a list of the names of the players who cheated.
   *
   * @param cheaters list of strings representing the names of cheaters
   */
  void updateCheaters(List<String> cheaters);

  /**
   * Updates the observer with a list of the game results from the current tournament.
   *
   * @param results list of game results
   */
  void updateResults(List<GameResult> results);
}