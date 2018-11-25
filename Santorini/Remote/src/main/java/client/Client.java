package client;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import common.interfaces.IObserver;
import common.interfaces.IPlayer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {

  Reader reader;

  public Client() {
    this(new BufferedReader(new InputStreamReader(System.in)));
  }

  public Client(Reader reader) {
    this.reader = reader;
  }

  public static void main(String[] args) throws IOException {
    startClient();
  }

  public static void startClient() throws IOException {
    List<IPlayer> players = Arrays.asList(mockPlayer("one"), mockPlayer("two"), mockPlayer("three"));
    List<IObserver> observers = new ArrayList<>();
    String ip = "localhost";
    int portNumber = 8000;

    for (IPlayer player : players) {
      System.out.println(String.format("creating relay for player %s", player.getPlayerName()));
      Socket socket = new Socket(ip, portNumber);
      new Thread(new Relay(socket, player)).start();
    }
  }

  private static IPlayer mockPlayer(String name) {
    IPlayer player = mock(IPlayer.class);
    when(player.getPlayerName()).thenReturn(name);
    return player;
  }
}
