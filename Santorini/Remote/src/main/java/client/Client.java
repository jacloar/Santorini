package client;

import com.fasterxml.jackson.core.JsonParseException;
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

/**
 * Client for the players. Allows user to send config from STDIN and sets up
 * appropriate players to participate in the tournament.
 */
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
    List<Socket> sockets = new ArrayList<>();

    if (port >= 50000 && port <= 60000) {
      for (IPlayer player : players) {
        Socket socket = new Socket(ip, port);

        Thread thread = new Thread(new Relay(socket, player, observers));
        thread.start();

        sockets.add(socket);

        // pause for half a second between connections to ensure
        // they connect in specified order
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          // do nothing
        }
      }

      // If all the sockets are closed, exit
      if (sockets.stream().allMatch(Socket::isClosed)) {
        System.exit(0);
      }
    } else {
      throw new IllegalArgumentException("\"port\" must be between 50000 and 60000");
    }
  }

  /**
   * Calls read config with the clients reader. This is called by whoever wants to start a client
   *
   * @throws IOException if I/O error occurs
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

    JsonNode config;
    try {
      config = mapper.readTree(r);
    } catch (JsonParseException e) {
      throw new IllegalArgumentException("Must be given valid Json");
    }

    parseConfig(config);
  }

  /**
   * Reads in the config file that gives the information about the players and the observers
   * participating in the tournament.
   *
   * @param config a JsonNode containing player and observer info
   */
  private static void parseConfig(JsonNode config) throws IOException {
    JsonNode playersNode;
    JsonNode observersNode;
    String ip;
    int port;

    String playersString = "players";
    String observersString = "observers";
    String ipString = "ip";
    String portString = "port";

    if (config.isObject()
        && config.has(playersString)
        && config.has(observersString)
        && config.has(ipString)
        && config.has(portString)) {
      playersNode = config.get(playersString);
      observersNode = config.get(observersString);

      if (config.get(ipString).isTextual()) {
        ip = config.get(ipString).asText();
      } else {
        throw new IllegalArgumentException("\"ip\" must be a string");
      }

      if (config.get(portString).isInt()) {
        port = config.get(portString).asInt();
      } else {
        throw new IllegalArgumentException("\"port\" must be an integer");
      }
    } else {
      throw new IllegalArgumentException("Invalid Json, must be an object with "
          + "\"players\", \"observers\", \"ip\", and \"port\"");
    }


    List<IPlayer> players = parsePlayers(playersNode);
    List<IObserver> observers = parseObservers(observersNode);

    startClient(players, observers, ip, port);
  }

  private static List<IObserver> parseObservers(JsonNode observersNode) {
    List<IObserver> observers = new ArrayList<>();
    if (observersNode.isArray()) {
      for (int i = 0; i < observersNode.size(); i += 1) {
        JsonNode observerNode = observersNode.get(i);

        if (observerNode.isArray()
            && observerNode.size() == 2
            && observerNode.get(0).isTextual()
            && observerNode.get(1).isTextual()) {
          String name = observerNode.get(0).asText();
          String path = observerNode.get(1).asText();

          IObserver observer;
          try {
            observer = makeObserver(name, path);
          } catch (Exception e) {
            throw new IllegalArgumentException("An observer has invalid class path");
          }
          observers.add(observer);
        } else {
          throw new IllegalArgumentException("Every element in \"observers\" must be an array of strings of size 2");
        }
      }
    } else {
      throw new IllegalArgumentException("\"observers\" field must be an array");
    }
    return observers;
  }

  private static List<IPlayer> parsePlayers(JsonNode playersNode) {
    List<IPlayer> players = new ArrayList<>();
    if (playersNode.isArray()) {
      for (int i = 0; i < playersNode.size(); i++) {
        JsonNode playerNode = playersNode.get(i);

        if (playerNode.isArray()
            && playerNode.size() == 3
            && playerNode.get(0).isTextual()
            && playerNode.get(1).isTextual()
            && playerNode.get(2).isTextual()) {
          String name = playerNode.get(1).asText();
          String path = playerNode.get(2).asText();

          IPlayer player;
          try {
            player = makePlayer(name, path);
          } catch (Exception e) {
            throw new IllegalArgumentException("A player has invalid class path");
          }
          players.add(player);
        } else {
          throw new IllegalArgumentException("Every element in \"players\" must be an array of strings of size 3");
        }
      }
    } else {
      throw new IllegalArgumentException("\"players\" field must be an array");
    }
    return players;
  }

  /**
   * Makes a new player using the given name and path.
   *
   * @param name name of the new player
   * @param path path to the class
   * @return player specified by name and path
   */
  private static IPlayer makePlayer(String name, String path) throws Exception {
    ClassLoader loader = Utils.getClassLoader(path);
    String className = Utils.classNameFromPath(path);

    return (IPlayer) loader
        .loadClass(className)
        .getConstructor(String.class)
        .newInstance(name);
  }

  /**
   * Constructs a new observer based on the given name and path.
   *
   * @param name name of the new observer
   * @param path path to the class
   * @return IObservers specified by name and path
   */
  private static IObserver makeObserver(String name, String path) throws Exception {
    ClassLoader loader = Utils.getClassLoader(path);

    return (IObserver) loader.loadClass(Utils.classNameFromPath(path))
                             .getConstructor()
                             .newInstance();
  }

}
