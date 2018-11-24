package server;

import common.interfaces.IPlayer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
      System.out.println(System.currentTimeMillis());
      Socket connection = serverSocket.accept();
      connections.add(connection);
    }

    System.out.println("creating remote players");
    List<IPlayer> players = new ArrayList<>();
    for (Socket s : connections) {
      players.add(new RemotePlayer(s));
    }

    System.out.println("printing names");
    System.out.println(players.stream().map(IPlayer::getPlayerName).collect(Collectors.toList()));

    for (Socket s : connections) {
      s.close();
    }
  }
}
