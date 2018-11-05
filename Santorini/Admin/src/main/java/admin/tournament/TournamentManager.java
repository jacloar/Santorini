package admin.tournament;

import admin.referee.IReferee;
import admin.referee.Referee;
import admin.result.GameResult;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.interfaces.IObserver;
import common.interfaces.IPlayer;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import player.AIPlayer;
import strategy.DiagonalPlacementStrategy;
import strategy.StayAliveStrategy;
import strategy.Strategy;
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

  private Reader read;
  private ObjectMapper mapper = new ObjectMapper();

  /**
   * Constructor for a tournamentManager. Takes no parameters and initializes
   * playerNames, results, cheaters and makes a new referee for this tournament.
   */
  public TournamentManager() {
    this(new BufferedReader(new InputStreamReader(System.in)));
  }

  public TournamentManager(Reader read) {
    playerNames = new HashMap<>();
    results = new ArrayList<>();
    cheaters = new ArrayList<>();
    ref = new Referee();

    this.read = read;
  }

  @Override
  public Optional<IPlayer> runTournament(List<IPlayer> players) {
    // If tournament has fewer than 2 players, tournament cannot run and there is no winner
    if (players.size() < 2) {
      return Optional.empty();
    }

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
   *
   * If both players cheated at some point in the tournament. Remove their match from the results.
   */
  private void fixCheaters() {
    List<GameResult> finalResults = new ArrayList<>();
    for (GameResult result : results) {
      if (cheaters.contains(result.getWinner())) {
        result.disqualifyWinner();
      }
      if (!cheaters.contains(result.getWinner()) || !cheaters.contains(result.getLoser())) {
        finalResults.add(result);
      }
    }
    results = finalResults;
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

  /**
   * Reads in the config file that gives the information about the players and the observers
   * participating in the tournament.
   *
   * @param config a JsonNode containing player and observer info
   */
  private Optional<IPlayer> readConfig(JsonNode config) {
    JsonNode playersNode = config.get("players");
    JsonNode observersNode = config.get("observers");

    List<IPlayer> players = new ArrayList<>();
    List<IObserver> observers = new ArrayList<>();

    for(int i = 0; i < playersNode.size(); i++) {
      JsonNode playerNode = playersNode.get(i);
      String kind = playerNode.get(0).asText();
      String name = playerNode.get(1).asText();
      String path = playerNode.get(2).asText();

      IPlayer player = makeNewPlayer(kind, name, path);
      players.add(player);
    }

    for (int i = 0; i < observersNode.size(); i += 1) {
      JsonNode observerNode = observersNode.get(i);

      String name = observerNode.get(0).asText();
      String path = observerNode.get(0).asText();

      IObserver observer = makeObserver(name, path);
      observers.add(observer);

    }

    for (IObserver o : observers) {
      ref.addObserver(o);
    }

    return this.runTournament(players);

  }

  /**
   * Constructs a new observer based on the given name and path.
   *
   * @param name name of the new observer
   * @param path path to the class
   * @return IObservers specified by name and path
   */
  IObserver makeObserver(String name, String path) {
    ClassLoader loader = getClassLoader(path);

    try {
      return (IObserver) loader.loadClass("admin.observer.StdOutObserver")
                               .getConstructor()
                               .newInstance();
    } catch (ClassNotFoundException |
        IllegalAccessException |
        InstantiationException |
        NoSuchMethodException |
        InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates a new player to compete in the tournament based on the input from the config file.
   *
   *
   * @param kind the type of player
   * @param name the player name
   * @param path the path to the implementation of the player
   */
  private IPlayer makeNewPlayer(String kind, String name, String path) {
    switch (kind) {
      case "good":
        return makeGoodPlayer(name, path);
      case "breaker":
        return makeBreakerPlayer(name, path);
      case "infinite":
        return makeInfinitePlayer(name, path);
      default:
        throw new IllegalArgumentException("invalid kind");
    }
  }

  /**
   * Creates a new infinite player based on the given name and path.
   *
   * @param name name of the player
   * @param path path to the class
   * @return IPlayer specified
   */
  IPlayer makeInfinitePlayer(String name, String path) {
    ClassLoader loader = getClassLoader(path);
    String className = "player.InfinitePlayer";
    return makePlayer(loader, className, name);
  }

  /**
   * Creates a new breaker player based on the given name and path.
   *
   * @param name name of the player
   * @param path path to the class
   * @return IPlayer specified
   */
  IPlayer makeBreakerPlayer(String name, String path) {
    ClassLoader loader = getClassLoader(path);
    String className = "player.BreakerPlayer";
    return makePlayer(loader, className, name);
  }

  /**
   * Creates a new good player based on the given name and path.
   *
   * @param name name of the player
   * @param path path to the class
   * @return IPlayer specified
   */
  IPlayer makeGoodPlayer(String name, String path) {
    ClassLoader loader = getClassLoader(path);

    Strategy strategy = new Strategy(new DiagonalPlacementStrategy(), new StayAliveStrategy(), 1);

    AIPlayer player;
    try {
      player = (AIPlayer) loader
          .loadClass("player.AIPlayer")
          .getConstructor(String.class, Strategy.class)
          .newInstance(name, strategy);
    } catch (ClassNotFoundException |
        IllegalAccessException |
        InstantiationException |
        NoSuchMethodException |
        InvocationTargetException e) {
      throw new RuntimeException(e);
    }

    return player;
  }

  /**
   * Returns a new ClassLoader that loads the class at the given path
   *
   * @param path Path for ClassLoader
   * @return ClassLoader for specified path
   */
  private ClassLoader getClassLoader(String path) {
    File file = new File(path);
    URL url;
    try {
      url = file.toURI().toURL();
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Invalid path", e);
    }

    URL[] urls = new URL[]{url};

    return new URLClassLoader(urls);
  }

  /**
   * Makes a new player using the given loader, classname, and playerName
   *
   * @param loader ClassLoader to use
   * @param className name of the class to load
   * @param playerName name of the player
   * @return
   */
  private IPlayer makePlayer(ClassLoader loader, String className, String playerName) {
    IPlayer player;
    try {
      player = (IPlayer) loader
          .loadClass(className)
          .getConstructor(String.class)
          .newInstance(playerName);
    } catch (ClassNotFoundException |
        IllegalAccessException |
        InstantiationException |
        NoSuchMethodException |
        InvocationTargetException e) {
      throw new RuntimeException(e);
    }
    return player;
  }

  public Optional<IPlayer> readInput() {
    JsonParser parser;
    JsonNode config;
    try {
      parser = new JsonFactory().createParser(read);
      config = mapper.readTree(parser);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return readConfig(config);
  }

}
