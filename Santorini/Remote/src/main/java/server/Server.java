package server;

import admin.result.GameResult;
import admin.tournament.ITournamentManager;
import admin.tournament.TournamentManager;
import com.fasterxml.jackson.core.JsonFactory;
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

  public static void startServer(int minPlayers, int portNumber, int waitingFor, int repeat) {
    ServerSocket serverSocket;
    try {
      serverSocket = new ServerSocket(portNumber);
    } catch (IOException e) {
      // IO error occurred when opening socket. Want to shut down and throw exception
      throw new RuntimeException(e);
    }

    do {

      List<Socket> connections = acceptConnections(waitingFor, serverSocket);

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
    } while (repeat == 1);

    try {
      serverSocket.close();
    } catch (IOException e) {
      throw new RuntimeException("Unable to close server socket", e);
    }

    System.exit(0);
  }

  /**
   * Closes the connection to all the sockets given.
   *
   * @param connections Socket connections to close
   */
  private static void closeConnections(List<Socket> connections) {
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
   * @param players List of RemotePlayers to inform
   * @param results List of GameResults of a tournament
   */
  private static void informResults(List<RemotePlayer> players, List<GameResult> results) {
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

  public void readConfig() {
    readConfig(reader);
  }

  public static void readConfig(Reader r) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode config;
    try {
      config = mapper.readTree(r);
    } catch (IOException e) {
      throw new RuntimeException("Error reading config", e);
    }

    int minPlayers = config.get("min players").asInt();
    int port = config.get("port").asInt();
    int waitingFor = config.get("waiting for").asInt();
    int repeat = config.get("repeat").size();

    startServer(minPlayers, port, waitingFor, repeat);
  }
}
