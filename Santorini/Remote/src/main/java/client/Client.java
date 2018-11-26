package client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.interfaces.IObserver;
import common.interfaces.IPlayer;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import utils.Utils;

public class Client {

  Reader reader;

  /**
   * Constructor for a client with system.in
   */
  public Client() {
    this(new InputStreamReader(System.in));
  }

  /**
   *
   * Constructor for a client using a given reader
   *
   * @param reader
   */
  public Client(Reader reader) {
    this.reader = reader;
  }

  /**
   * Starts a new client with System.in as the reader.
   *
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    readConfig(new InputStreamReader(System.in));
  }

  /**
   *
   * Starts a client. Connects all players to correct socket given the IP and port number.
   *
   * @param players the players in the tournament
   * @param observers the observers in the tournament
   * @param ip the ip address they are connecting to
   * @param port the port number they are connecting to
   * @throws IOException if the connection fails
   */
  public static void startClient(
      List<IPlayer> players,
      List<IObserver> observers,
      String ip,
      int port
  ) throws IOException {
    List<Thread> threads = new ArrayList<>();
    for (IPlayer player : players) {
      Socket socket = new Socket(ip, port);

      Thread thread = new Thread(new Relay(socket, player, observers));
      thread.start();
      threads.add(thread);

      // pause for half a second between connections to ensure
      // they connect in specified order
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        // do nothing
      }
    }


    while (threads.stream().anyMatch(Thread::isAlive)) {
      // wait until all threads are dead
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // do nothing
      }
    }

    System.exit(0);
  }

  /**
   * Calls read config with the clients reader. This is called by whoever wants to start a client
   *
   * @throws IOException
   */
  public void readConfig() throws IOException {
    readConfig(reader);
  }

  /**
   * Reads from the config file as JsonNodes
   *
   * @param r the reader for the client
   * @throws IOException if the connection fails
   */
  private static void readConfig(Reader r) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode config = mapper.readTree(r);

    parseConfig(config);
  }

  /**
   * Reads in the config file that gives the information about the players and the observers
   * participating in the tournament.
   *
   * @param config a JsonNode containing player and observer info
   */
  private static void parseConfig(JsonNode config) throws IOException {
    JsonNode playersNode = config.get("players");
    JsonNode observersNode = config.get("observers");
    String ip = config.get("ip").asText();
    int port = config.get("port").asInt();

    List<IPlayer> players = new ArrayList<>();
    List<IObserver> observers = new ArrayList<>();

    for(int i = 0; i < playersNode.size(); i++) {
      JsonNode playerNode = playersNode.get(i);
      String name = playerNode.get(1).asText();
      String path = playerNode.get(2).asText();

      IPlayer player = makePlayer(name, path);
      players.add(player);
    }

    for (int i = 0; i < observersNode.size(); i += 1) {
      JsonNode observerNode = observersNode.get(i);

      String name = observerNode.get(0).asText();
      String path = observerNode.get(1).asText();

      IObserver observer = makeObserver(name, path);
      observers.add(observer);
    }

    startClient(players, observers, ip, port);
  }

  /**
   * Makes a new player using the given name and path.
   *
   * @param name name of the new player
   * @param path path to the class
   * @return player specified by name and path
   */
  private static IPlayer makePlayer(String name, String path) {
    ClassLoader loader = Utils.getClassLoader(path);
    String className = Utils.classNameFromPath(path);

    IPlayer player;
    try {
      player = (IPlayer) loader
          .loadClass(className)
          .getConstructor(String.class)
          .newInstance(name);
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
   * Constructs a new observer based on the given name and path.
   *
   * @param name name of the new observer
   * @param path path to the class
   * @return IObservers specified by name and path
   */
  private static IObserver makeObserver(String name, String path) {
    ClassLoader loader = Utils.getClassLoader(path);

    try {
      return (IObserver) loader.loadClass(Utils.classNameFromPath(path))
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

}
