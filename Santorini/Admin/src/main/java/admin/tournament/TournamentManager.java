package admin.tournament;

import admin.referee.IReferee;
import admin.referee.Referee;
import admin.result.GameResult;
import common.interfaces.IPlayer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import utils.Utils;

/**
 * Facilitates a tournament for a list of players. Each player will play every other
 * player in a round-robin style way.
 */
public class TournamentManager implements ITournamentManager {

  private static final int MEET_UP_GAMES = 3;
  private static final int TIMEOUT = 5;

  private Map<IPlayer, String> playerNames;

  private List<GameResult> results;
  private List<IPlayer> cheaters;

  private IReferee ref;

  /**
   * Constructor for a tournamentManager. Takes no parameters and initializes
   * playerNames, results, cheaters and makes a new referee for this tournament.
   */
  public TournamentManager() {
    playerNames = new HashMap<>();
    results = new ArrayList<>();
    cheaters = new ArrayList<>();
    ref = new Referee();
  }

  @Override
  public Optional<IPlayer> runTournament(List<IPlayer> players) {
    ensureUniqueNames(players);

    for (int i = 0; i < players.size(); i += 1) {
      for (int j = i + 1; j < players.size(); j += 1) {
        runMeetUp(players.get(i), players.get(j));
      }
    }

    fixCheaters();
    return determineWinner();
  }

  /**
   *
   * This method determines the winner of a tournament. It will look through the list
   * of GameResults and find the player that has the most victories. If multiple
   * players have the same amount of (max) wins then no winner can be determined.
   *
   * @return the player who won the tournament, empty if there is a tie.
   */
  private Optional<IPlayer> determineWinner() {
    Map<IPlayer, Integer> wins = new HashMap<>();

    for (GameResult result : results) {
      IPlayer gameWinner = result.getWinner();
      Integer winnerWins = wins.get(gameWinner);
      int newWins = winnerWins == null ? 1 : winnerWins + 1;
      wins.put(gameWinner, newWins);
    }

    Optional<IPlayer> tournamentWinner = Optional.empty();
    int maxWins = 0;
    for (IPlayer player : wins.keySet()) {
      int playerWins = wins.get(player);
      if (playerWins > maxWins) {
        tournamentWinner = Optional.of(player);
        maxWins = playerWins;
      } else if (playerWins == maxWins) {
        tournamentWinner = Optional.empty();
      }
    }

    return tournamentWinner;
  }

  /**
   * This method goes through the GameResults of a tournament and disqualifies cheating players
   * this will call a helper method that will make sure that the results of all games the cheater
   * played before cheating result in a loss for the cheater and a win for the other player.
   */
  private void fixCheaters() {
    for (GameResult result : results) {
      if (cheaters.contains(result.getWinner())) {
        result.disqualifyWinner();
      }
    }
  }

  /**
   *
   * In order to play a tournament we need to make sure that all of our players have unique names
   * if we find players with names that are not unique then we will assign them a new unique
   * name and notify them of the change.
   *
   * A player who has no name at all is marked as a cheating player, then a new unique name is
   * generated to allow the tournament to run.
   *
   * @param players All players participating in the tourney
   */
  private void ensureUniqueNames(List<IPlayer> players) {
    List<String> names = new ArrayList<>(players.size());

    for (IPlayer player : players) {
      Optional<String> name = Utils.timedCall(player, IPlayer::getPlayerName, TIMEOUT);

      if (name.isPresent()) {
        if (names.contains(name.get())) {
          String newName = updatePlayerName(player, players);
          storeName(names, player, newName);
        } else {
          storeName(names, player, name.get());
        }
      } else {
        cheaters.add(player);
        String cheatName = generateUniqueName(players);
        storeName(names, player, cheatName);
      }
    }
  }

  /**
   *
   * Stores the given name to the tournament managers list of names and the map from
   * player name -> player
   *
   * @param names The list of all known names of players
   * @param player the player who's name is being stored
   * @param name the new name we are storing
   */
  private void storeName(List<String> names, IPlayer player, String name) {
    names.add(name);
    playerNames.put(player, name);
  }


  /**
   *
   * This method facilitates a match of Santorini between two players. It will determine what
   * the results of that match are and add them to the list of GameResults.
   *
   * @param player1 The first player
   * @param player2 the second player
   */
  private void runMeetUp(IPlayer player1, IPlayer player2) {
    // Do not run meet up if either player is cheater
    if (cheaters.contains(player1) || cheaters.contains(player2)) {
      return;
    }

    List<GameResult> meetUpResults = ref.bestOfN(player1, player2, MEET_UP_GAMES);

    for (GameResult result : meetUpResults) {
      if (result.didLoserCheat()) {
        // if a player cheated, add it to the list of cheaters
        cheaters.add(result.getLoser());
      }

      // Add this result to the list of results
      this.results.add(result);
    }
  }

  /**
   * This method updates the name of a player and makes sure that it is unique relative to the
   * current list of players.
   *
   * @param player the player whose name is getting changed
   * @param allPlayers the list of all of our players
   * @return the new name for the player
   */
  String updatePlayerName(IPlayer player, List<IPlayer> allPlayers) {
    String name = generateUniqueName(allPlayers);

    Function<IPlayer, Boolean> updateName = p -> {
      p.setPlayerName(name);
      return true;
    };

    Optional<Boolean> result = Utils.timedCall(player, updateName, TIMEOUT);
    if (!result.isPresent()) {
      cheaters.add(player);
    }

    return name;
  }

  /**
   * Generates a name that is not currently used as a name in the given list of players.
   *
   * @param allPlayers all IPlayers playing the game
   * @return unique name
   */
  String generateUniqueName(List<IPlayer> allPlayers) {
    String name = UUID.randomUUID().toString();

    for (IPlayer player : allPlayers) {
      Optional<String> playerName = Utils.timedCall(player, IPlayer::getPlayerName, TIMEOUT);

      if (playerName.isPresent()) {
        if (name.equals(playerName.get())) {
          return generateUniqueName(allPlayers);
        }
      } else {
        cheaters.add(player);
      }
    }

    return name;
  }

  /**
   * Returns the results of the tournament.
   *
   * @return a list of GameResults representing the results of the tournament
   */
  public List<GameResult> getResults() {
    return results;
  }


  /**
   * Converts the list of cheating players to a list of cheating player names
   *
   * @return the list of cheater names
   */
  public List<String> getCheatersNames() {
    return cheaters.stream().map(p -> playerNames.get(p)).collect(Collectors.toList());
  }

  /**
   * Gets the name of the given player object
   *
   * @param player A player object
   * @return the name of that player as a string. If the player or name doesn't exist return empty
   */
  Optional<String> getPlayerName(IPlayer player) {
    if(playerNames.containsKey(player)) {
      return Optional.of(playerNames.get(player));
    }
    return Optional.empty();
  }

}
