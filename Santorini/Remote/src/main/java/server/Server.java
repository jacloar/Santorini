package server;

import admin.result.GameResult;
import admin.tournament.ITournamentManager;
import admin.tournament.TournamentManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Server {

  Reader reader;

  public Server() {
    this(new BufferedReader(new InputStreamReader(System.in)));
  }

  public Server(Reader reader) {
    this.reader = reader;
  }

  public static void main(String[] args) throws Exception {
    startServer();
  }

  public static void startServer() throws Exception {
    int minPlayers = 3;
    int portNumber = 8000;
    int waitingFor = 10;
    int repeat = 0;

    ServerSocket serverSocket = new ServerSocket(portNumber);
    List<Socket> connections = new ArrayList<>();
    long endTime = System.currentTimeMillis() + (waitingFor * 1000);

    System.out.println("accepting connections");
    while (System.currentTimeMillis() <= endTime) {
      serverSocket.setSoTimeout((int) (endTime - System.currentTimeMillis()));
      try {
        Socket connection = serverSocket.accept();
        connections.add(connection);
      } catch (SocketTimeoutException e) {
        // do nothing, want to continue
      }
    }

    // Run the tournament if enough players connected.
    if (connections.size() >= minPlayers) {
      System.out.println("creating remote players");
      List<RemotePlayer> players = new ArrayList<>();
      for (Socket s : connections) {
        players.add(new RemotePlayer(s));
      }

      ITournamentManager tm = new TournamentManager();
      tm.runTournament(players);

      // A tournament has been run, so results is not empty
      List<GameResult> results = tm.getResults().get();
      for (RemotePlayer player : players) {
        player.inform(results);
      }
    }

    for (Socket s : connections) {
      s.close();
    }
  }
}
