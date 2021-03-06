package server;

import admin.result.GameResult;
import admin.tournament.ITournamentManager;
import admin.tournament.TournamentManager;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.interfaces.IPlayer;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the server for the tournament manager.
 *
 * If not enough players connected before the time is up, the connected
 * players will be booted and no tournament will take place.
 *
 * Players who do not respond with a name will be booted from the tournament.
 * Players who respond with an invalid name will be renamed via "playing-as".
 */
public class Server {

  Reader reader;

  public Server() {
    this(new InputStreamReader(System.in));
  }

  public Server(Reader reader) {
    this.reader = reader;
  }

  public static void main(String[] args) {
    readConfig(new InputStreamReader(System.in));
  }

  /**
   * Starts the server with the specified configuration. Validates the given config
   *
   * @param minPlayers minimum number of players
   * @param portNumber port number
   * @param waitingFor number of seconds to wait for
   * @param repeat 0 for no repeat or 1 to repeat all
   */
  public static void startServer(int minPlayers, int portNumber, int waitingFor, int repeat) {
    if (minPlayers >= 0
        && portNumber >= 0
        && waitingFor > 0
        && (repeat == 0 || repeat == 1)) {
      serverHelper(minPlayers, portNumber, waitingFor, repeat);
    } else {
      throw new IllegalArgumentException("\"min players\" and \"port\" must be greater than or equal to 0, "
          + "\"waiting for\" must be greater than 0, and \"repeat\" must be equal to 0 or 1");
    }

    System.exit(0);
  }

  /**
   * Helper method for the server. Starts the server with the specified configuration after validation
   *
   * @param minPlayers minimum players
   * @param portNumber port number
   * @param waitingFor wait time in seconds
   * @param repeat 0 or 1 for repeat
   */
  private static void serverHelper(int minPlayers, int portNumber, int waitingFor, int repeat) {
    ServerSocket serverSocket;
    try {
      serverSocket = new ServerSocket(portNumber);
    } catch (IOException e) {
      // IO error occurred when opening socket. Want to shut down and throw exception
      throw new RuntimeException(e);
    }

    do {
      List<Socket> connections = acceptConnections(waitingFor, serverSocket);
      runServer(minPlayers, connections);
    } while (repeat == 1);

    try {
      serverSocket.close();
    } catch (IOException e) {
      throw new RuntimeException("Unable to close server socket", e);
    }
  }

  /**
   * Runs the server with the given connections and minimum number of players.
   *
   * **package-private for testing**
   *
   * @param minPlayers minimum number of connections
   * @param connections list of connections
   */
  static void runServer(int minPlayers, List<Socket> connections) {
    // Run the tournament if enough players connected.
    if (connections.size() >= minPlayers) {
      List<RemotePlayer> players = constructRemotePlayers(connections);
      ITournamentManager tm = runTournament(players);

      // A tournament has been run, so results is not empty
      List<GameResult> results = tm.getResults().get();
      informResults(players, results);
      Message.inform(new PrintStream(System.out), results);
    }

    closeConnections(connections);
  }

  /**
   * Closes the connection to all the sockets given.
   *
   * **package-private for testing**
   *
   * @param connections Socket connections to close
   */
  static void closeConnections(List<Socket> connections) {
    for (Socket s : connections) {
      try {
        if (!s.isClosed()) {
          s.close();
        }
      } catch (IOException e) {
        // do nothing
      }
    }
  }

  /**
   * Informs the given remote players of the given results of a tournament.
   *
   * **package-private for testing**
   *
   * @param players List of RemotePlayers to inform
   * @param results List of GameResults of a tournament
   */
  static void informResults(List<RemotePlayer> players, List<GameResult> results) {
    for (RemotePlayer player : players) {
      player.inform(results);
    }
  }

  /**
   * Runs a tournament with the given IPlayers.
   *
   * @param players Players to run the tournament with
   * @return the ITournamentManager that ran the tournament
   */
  private static ITournamentManager runTournament(List<? extends IPlayer> players) {
    ITournamentManager tm = new TournamentManager();
    tm.runTournament(players);
    return tm;
  }

  /**
   * Creates new RemotePlayers for the given socket connections
   *
   * @param connections Connections to make remote players for
   * @return List of remote players
   */
  private static List<RemotePlayer> constructRemotePlayers(List<Socket> connections) {
    List<RemotePlayer> players = new ArrayList<>();
    for (Socket s : connections) {
      try {
        players.add(new RemotePlayer(s));
      } catch (IOException e) {
        try {
          s.close();
        } catch (IOException e1) {
          // do nothing
        }
      }
    }
    return players;
  }

  /**
   * Accepts connections to this server and adds them to a list.
   *
   * @param waitingFor time to wait for (in seconds)
   * @param serverSocket ServerSockets for this server
   * @return List of Sockets that contain the connections
   */
  private static List<Socket> acceptConnections(int waitingFor, ServerSocket serverSocket) {
    List<Socket> connections = new ArrayList<>();
    long endTime = System.currentTimeMillis() + (waitingFor * 1000);
    long currentTime;
    while ((currentTime = System.currentTimeMillis()) < endTime) {
      try {
        serverSocket.setSoTimeout((int) (endTime - currentTime));
      } catch (SocketException e) {
        // Throws if TCP error. Want to throw exception
        throw new RuntimeException(e);
      }
      try {
        Socket connection = serverSocket.accept();
        connections.add(connection);
      } catch (IOException e) {
        // do nothing, want to continue
      }
    }
    return connections;
  }

  /**
   * Read the config from this server's reader
   */
  public void readConfig() {
    readConfig(reader);
  }

  /**
   * Read the configuration from the given reader
   *
   * @param r Reader to read config from
   */
  private static void readConfig(Reader r) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode config;
    try {
      config = mapper.readTree(r);
    } catch (JsonParseException e) {
      throw new IllegalArgumentException("Must be given valid Json");
    } catch (IOException e) {
      throw new RuntimeException("Error reading config", e);
    }

    int minPlayers;
    int port;
    int waitingFor;
    int repeat;

    String minPlayersString = "min players";
    String portString = "port";
    String waitingForString = "waiting for";
    String repeatString = "repeat";

    if (config.isObject()
        && config.has(minPlayersString)
        && config.has(portString)
        && config.has(waitingForString)
        && config.has(repeatString)
        && config.get(minPlayersString).isInt()
        && config.get(portString).isInt()
        && config.get(waitingForString).isInt()
        && config.get(repeatString).isInt()) {

      minPlayers = config.get(minPlayersString).asInt();
      port = config.get(portString).asInt();
      waitingFor = config.get(waitingForString).asInt();
      repeat = config.get(repeatString).asInt();
    } else {
      throw new IllegalArgumentException("Must be given Json object with \"min players\", \"port\", "
          + "\"waiting for\", and \"repeat\". All values must be integers.");
    }

    startServer(minPlayers, port, waitingFor, repeat);
  }
}
