package server;

import admin.result.GameResult;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import common.board.IReadonlyBoard;
import common.data.Action;
import common.data.ActionType;
import common.data.Direction;
import common.data.PlaceWorkerAction;
import common.data.Worker;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Message {

  private static ObjectMapper mapper = new ObjectMapper();

  /**
   * Sends a message to the remote player. It is either a playing as or other message
   * @param out the print stream for sending the message
   * @param name the name of a player

   */
  public static void playingAs(PrintStream out, String name) {
    ArrayNode message = mapper.createArrayNode();
    message.add("playing-as");
    message.add(name);

    out.println(message);
  }

  /**
   * Sends a message to the remote player. It is either a playing as or other message
   * @param out the print stream for sending the message
   * @param name the name of a player
   * @JsonProcessingException if there is a problem with the message

   */
  public static void other(PrintStream out, String name) {
    String message;
    try {
      message = mapper.writeValueAsString(name);
    } catch (JsonProcessingException e) {
      // If there is an error processing the Json, do it manually.
      message = "\"" + name + "\"";
    }
    out.println(message);
  }

  /**
   *
   * Sends a message to a player indicating it is their turn to place a worker. It contains
   * the names and positions of the workers on the board already.
   *
   * @param out a print stream for sending the message
   * @param board the current board
   * @param p1Name the name of player 1
   * @param p2Name the name of player 2
   */
  public static PlaceWorkerAction workerPlacement(Reader in, PrintStream out, IReadonlyBoard board, String p1Name, String p2Name) throws IOException {
    ArrayNode message = mapper.createArrayNode();
    List<Worker> workers = new ArrayList<Worker>();
    workers.addAll(board.getPlayerWorkers(p1Name));
    workers.addAll(board.getPlayerWorkers(p2Name));

    for(Worker w : workers) {
      ArrayNode workerPlace = mapper.createArrayNode();
      workerPlace.add(w.getWorkerId());
      workerPlace.add(w.getRow());
      workerPlace.add(w.getColumn());
      message.add(workerPlace);
    }
    out.println(message.toString());

    return place(in);
  }


  /**
   *
   * Waits for a client to send a placement request an then translates the request into
   * a method call
   *
   * @param reader the reader to get the message from the client
   * @return a PlaceWorkerAction representing the desired location for the worker to be placed
   * @throws IOException throw an exception if the input unexpectedly closes
   */
  private static PlaceWorkerAction place(Reader reader) throws IOException {
    JsonParser parser = new JsonFactory().createParser(reader);


    ArrayNode node = mapper.readTree(parser);
    int row = node.get(0).asInt();
    int col = node.get(1).asInt();

    PlaceWorkerAction placement = new PlaceWorkerAction(row, col);
    return placement;
  }


  /**
   *
   * Tells a player that it is their turn to take a move. This method sends a representation of
   * the board to the client.
   *
   * @param in input stream for a response
   * @param out a print stream to send the message to the client
   * @param board the current board
   * @return a list of actions that the client player has requested
   * @throws IOException if the stream unexpectedly closes
   */
  public static List<Action> takeTurn(Reader in, PrintStream out, IReadonlyBoard board) throws IOException {
    ArrayNode message = mapper.createArrayNode();
    for(int row = 0; row < board.getMaxRows(); row++) {
      ArrayNode singleColumn = mapper.createArrayNode();
      for(int col = 0; col < board.getMaxColumns(); col++) {
        singleColumn.add(board.getCell(row, col).toJson());
      }
      message.add(singleColumn);
    }
    out.println(message.toString());
    return getTurn(in);
  }


  /**
   *
   * Waits for a response from the player and then translates it into a move, optional build
   * request or give up message
   *
   * @param reader the input stream from the client
   * @return a list of actions representing a move request, can also be a give up request
   * @throws IOException if the stream unexpectedly closes
   */
  private static List<Action> getTurn(Reader reader) throws IOException {
    JsonParser parser = new JsonFactory().createParser(reader);
    List<Action> turn = new ArrayList<>();
    JsonNode node = mapper.readTree(parser);
    if(!node.isArray()) {
      // TODO: need to figure out what to do for a give up action
    }
    String workerName = node.get(0).toString();
    Direction d1 = new Direction(node.get(1).asText(), node.get(2).asText());
    Action move = new Action(ActionType.MOVE, workerName, d1);
    turn.add(move);
    if(node.size() == 3) {
     return turn;
    } else if(node.size() == 5) {
      Direction d2 = new Direction(node.get(3).asText(), node.get(4).asText());
      Action build = new Action(ActionType.BUILD, workerName, d2);
      turn.add(build);
    } else {
      throw new IllegalArgumentException("not a valid turn");
    }
    return turn;
  }

  /**
   * Informs the players of the tournament of the result.
   *
   * @param out the print stream to send a message to clients
   * @param tournamentResults the list of game results of the tournament
   */
  public static void inform(PrintStream out, List<GameResult> tournamentResults) {
    ArrayNode message = mapper.createArrayNode();
    for(GameResult result : tournamentResults) {
      ArrayNode message2 = mapper.createArrayNode();
      message2.add(result.getWinner().getPlayerName());
      message2.add(result.getLoser().getPlayerName());
      if(result.didLoserCheat()) {
        message2.add("irregular");
      }
      message.add(message2);
    }
    out.println(message.toString());
  }








}
