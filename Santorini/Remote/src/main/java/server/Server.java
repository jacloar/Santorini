package server;

import admin.result.GameResult;
import admin.tournament.ITournamentManager;
import admin.tournament.TournamentManager;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
    this(new InputStreamReader(System.in));
  }

  public Server(Reader reader) {
    this.reader = reader;
  }

  public static void main(String[] args) throws IOException {
    readConfig(new InputStreamReader(System.in));
  }

  public static void startServer(int minPlayers, int portNumber, int waitingFor, int repeat) throws IOException {
    do {
      ServerSocket serverSocket = new ServerSocket(portNumber);
      List<Socket> connections = new ArrayList<>();
      long endTime = System.currentTimeMillis() + (waitingFor * 1000);
      System.out.println("end time: " + endTime);

      System.out.println("accepting connections");
      long currentTime;
      while ((currentTime = System.currentTimeMillis()) < endTime) {
        System.out.println(currentTime);
        serverSocket.setSoTimeout((int) (endTime - currentTime));
        try {
          Socket connection = serverSocket.accept();
          connections.add(connection);
        } catch (SocketTimeoutException e) {
          // do nothing, want to continue
        }
      }
      System.out.println("wait time up");

      // Run the tournament if enough players connected.
      if (connections.size() >= minPlayers) {
        System.out.println("creating remote players");
        List<RemotePlayer> players = new ArrayList<>();
        for (Socket s : connections) {
          players.add(new RemotePlayer(s));
        }

        System.out.println("running tournament");
        ITournamentManager tm = new TournamentManager();
        tm.runTournament(players);

        // A tournament has been run, so results is not empty
        List<GameResult> results = tm.getResults().get();
        for (RemotePlayer player : players) {
          player.inform(results);
        }
      }

      System.out.println("closing connections");
      for (Socket s : connections) {
        s.close();
      }
    } while (repeat == 1);
  }

  public void readConfig() throws IOException {
    readConfig(reader);
  }

  public static void readConfig(Reader r) throws IOException{
    JsonParser parser = new JsonFactory().createParser(r);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode config = mapper.readTree(parser);

    int minPlayers = config.get("min players").asInt();
    int port = config.get("port").asInt();
    int waitingFor = config.get("waiting for").asInt();
    int repeat = config.get("repeat").size();

    startServer(minPlayers, port, waitingFor, repeat);
  }
}
