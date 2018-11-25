package client;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
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

  public Client() {
    this(new InputStreamReader(System.in));
  }

  public Client(Reader reader) {
    this.reader = reader;
  }

  public static void main(String[] args) throws IOException {
    readConfig(new InputStreamReader(System.in));
  }

  public static void startClient(
      List<IPlayer> players,
      List<IObserver> observers,
      String ip,
      int port
  ) throws IOException {
    for (IPlayer player : players) {
      Socket socket = new Socket(ip, port);
      new Thread(new Relay(socket, player, observers)).start();
    }
  }

  public void readConfig() throws IOException {
    readConfig(reader);
  }

  private static void readConfig(Reader r) throws IOException {
    JsonParser parser = new JsonFactory().createParser(r);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode config = mapper.readTree(parser);

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
