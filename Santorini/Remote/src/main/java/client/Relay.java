package client;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import common.board.Board;
import common.board.IBoard;
import common.data.Action;
import common.data.ActionType;
import common.data.Direction;
import common.data.PlaceWorkerAction;
import common.interfaces.IPlayer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

public class Relay implements Runnable {

  private static ObjectMapper mapper = new ObjectMapper();

  private Socket socket;
  private IPlayer player;

  public Relay(IPlayer player) {
    this.player = player;
  }

  public Relay(Socket socket, IPlayer player) {
    this.socket = socket;
    this.player = player;
  }

  public void run() {
    try {
      manageConnection();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void manageConnection() throws IOException {
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    try {
      out.println(mapper.writeValueAsString(player.getPlayerName()));
    } catch (JsonProcessingException e) {
      // Should not be thrown. getPlayerName returns String.
    }

    JsonParser parser = new JsonFactory().createParser(in);
    while (!socket.isClosed()) {
      JsonNode nextNode;
      if ((nextNode = mapper.readTree(parser)) != null) {
        Optional<JsonNode> response = this.respond(nextNode);
        response.ifPresent(out::println);
      }
    }
  }

  /**
   * Response Json message from the player, if it is required.
   *
   * @param prompt Json prompt
   * @return Json response
   */
  public Optional<JsonNode> respond(JsonNode prompt) {
    String playingAs = "playing-as";

    // other message
    if (prompt.isTextual()) {
      this.other(prompt.asText());
      return Optional.empty();
    }

    // playing as message
    if (prompt.isArray() && prompt.size() == 2 && prompt.get(0).asText().equals(playingAs)) {
      this.playingAs(prompt.get(1).asText());
      return Optional.empty();
    }

    // placement message
    if (this.isPlacement(prompt)) {
      return Optional.of(this.place(prompt));
    }

    // take turn message (only message left)
    return Optional.of(action(prompt));
  }

  /**
   * Returns the action message in response to the given board
   * ** boardNode must be a valid Json Board **
   *
   * @param boardNode Json node representing the current state of the board
   * @return Json Action representing next move
   */
  private JsonNode action(JsonNode boardNode) {
    IBoard board = new Board(boardNode);

    List<Action> actions = player.getTurn(board);

    return buildActionResponse(actions);
  }

  private JsonNode buildActionResponse(List<Action> actions) {
    ArrayNode action = mapper.createArrayNode();

    if (actions.get(0).getType() == ActionType.MOVE) {
      Action move = actions.get(0);
      action.add(move.getWorkerId());

      Direction moveDir = move.getDirection();
      action.add(moveDir.getEastWest());
      action.add(moveDir.getNorthSouth());
    }

    if (actions.size() > 1) {
      Direction buildDir = actions.get(1).getDirection();
      action.add(buildDir.getEastWest());
      action.add(buildDir.getNorthSouth());
    }

    return action;
  }

  /**
   * Returns the place message in response to the given placement message
   * ** Given JsonNode must be a Placement **
   *
   * @param node Placement to determine place from
   * @return Place message representing placement
   */
  private JsonNode place(JsonNode node) {
    // Create a new board with the given placements
    IBoard placeBoard = buildPlaceBoard(node);

    PlaceWorkerAction place = player.getPlaceWorker(placeBoard);

    return buildPlaceResponse(place);
  }

  /**
   * Build the Json Place response from the given PlaceWorkerAction.
   *
   * @param action PlaceWorkerAction to convert to Json
   * @return Json Place response
   */
  private JsonNode buildPlaceResponse(PlaceWorkerAction action) {
    ArrayNode place = mapper.createArrayNode();

    // add the x (column)
    place.add(action.getColumn());

    // add the y (row)
    place.add(action.getRow());

    return place;
  }

  /**
   * Creates a new board with the placements given in the JsonNode.
   * ** Given JsonNode must be a Placement **
   *
   * @param node Placement to build board from
   * @return Equivalent IBoard
   */
  private IBoard buildPlaceBoard(JsonNode node) {
    IBoard placeBoard = new Board();

    for (int i = 0; i < node.size(); i += 1) {
      JsonNode workerPlace = node.get(i);
      String worker = workerPlace.get(0).asText();
      int x = workerPlace.get(1).asInt();
      int y = workerPlace.get(2).asInt();

      String playerName = worker.substring(0, worker.length() - 1);
      // y = row, x = col
      placeBoard.createWorker(playerName, y, x);
    }
    return placeBoard;
  }


  /**
   * Is this JsonNode a Placement?
   *
   * @param node JsonNode to check
   * @return true if placement, false otherwise
   */
  private boolean isPlacement(JsonNode node) {
    if (!node.isArray()) {
      return false;
    }

    int size = node.size();
    if (size > 3) {
      return false;
    }

    for (int i = 0; i < size; i += 1) {
      if (!this.isWorkerPlace(node.get(i))) {
        return false;
      }
    }

    return true;
  }

  /**
   * Is the given Json node a WorkerPlace?
   * @param node Node to check
   * @return true if WorkerPlace, false otherwise
   */
  private boolean isWorkerPlace(JsonNode node) {
    if (!node.isArray() || node.size() != 3) {
      return false;
    }

    JsonNode workerNode = node.get(0);
    JsonNode xNode = node.get(1);
    JsonNode yNode = node.get(2);

    if (!workerNode.isTextual() || !xNode.isInt() || !yNode.isInt()) {
      return false;
    }

    String worker = workerNode.asText();
    int x = xNode.asInt();
    int y = yNode.asInt();

    return this.isWorker(worker) && this.isCoordinate(x) && this.isCoordinate(y);
  }

  /**
   * Is the given String a valid worker (lowercase letters a-z followed by 1 or 2)
   *
   * @param str String to check
   * @return true if valid worker, false otherwise
   */
  private boolean isWorker(String str) {
    int length = str.length();
    String name = str.substring(0, length - 1);
    String num = str.substring(length - 1);

    return name.matches("[a-z]*") && (num.equals("1") || num.equals("2"));
  }

  /**
   * Is the given integer a valid coordinate (in [0,5])
   *
   * @param coord integer to check
   * @return true if coordinate, false otherwise
   */
  private boolean isCoordinate(int coord) {
    return coord >= 0 && coord <= 5;
  }

  /**
   * Responds to an other prompt
   *
   * @param name name of the opponent player
   */
  private void other(String name) {
    player.setOpponentName(name);
  }

  /**
   * Respond to a PA prompt
   *
   * @param name new name of the player
   */
  private void playingAs(String name) {
    player.setPlayerName(name);
  }

}
