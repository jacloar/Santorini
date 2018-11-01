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
 * Facilitates a tournament between a list of players. Each player will play every other
 * player in a round-robin style way.
 */
public class TournamentManager implements ITournamentManager {

  private static final int MEET_UP_GAMES = 3;
  private static final int TIMEOUT = 5;

  private Map<IPlayer, String> playerNames;

  private List<GameResult> results;
  private List<IPlayer> cheaters;

  private IReferee ref;

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

  private Optional<IPlayer> determineWinner() {
    Map<IPlayer, Integer> wins = new HashMap<>();

    for (GameResult result : results) {
      IPlayer gameWinner = result.getWinner();
      int winnerWins = wins.get(gameWinner) + 1;
      wins.put(gameWinner, winnerWins);
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

  private void fixCheaters() {
    for (GameResult result : results) {
      if (cheaters.contains(result.getWinner())) {
        result.disqualifyWinner();
      }
    }
  }

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

  private void storeName(List<String> names, IPlayer player, String name) {
    names.add(name);
    playerNames.put(player, name);
  }

  @Override
  public Optional<IPlayer> runTournament(List<IPlayer> players, int bestOf) {
    return Optional.empty();
  }

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

  private String updatePlayerName(IPlayer player, List<IPlayer> allPlayers) {
    String name = generateUniqueName(allPlayers);

    Function<IPlayer, Void> updateName = p -> {
      p.setPlayerName(name);
      return null;
    };

    Optional<Void> result = Utils.timedCall(player, updateName, TIMEOUT);
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
  private String generateUniqueName(List<IPlayer> allPlayers) {
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

  public List<GameResult> getResults() {
    return results;
  }

  public List<String> getCheatersNames() {
    return cheaters.stream().map(IPlayer::getPlayerName).collect(Collectors.toList());
  }
}
