package server;

import admin.result.GameResult;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.board.IReadonlyBoard;
import common.data.Action;
import common.data.PlaceWorkerAction;
import common.interfaces.IPlayer;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

public class RemotePlayer implements IPlayer {

  private Optional<String> name = Optional.empty();
  private InputStreamReader clientIn;
  private PrintStream clientOut;

  private String opponentName;

  private static ObjectMapper mapper = new ObjectMapper();

  public RemotePlayer(Socket client) throws IOException {
    this.clientIn = new InputStreamReader(client.getInputStream());
    this.clientOut = new PrintStream(client.getOutputStream());
  }

  /**
   *
   * Gets the name of this player
   *
   * @param in input stream to receive name
   * @return the name of this player
   * @throws IOException if something goes wrong
   */
  private String findName(InputStreamReader in) throws IOException {
    JsonParser parser = new JsonFactory().createParser(in);
    JsonNode nameNode = mapper.readTree(parser);

    return nameNode.asText();
  }

  @Override
  public PlaceWorkerAction getPlaceWorker(IReadonlyBoard b) {
    try {
      return Message.workerPlacement(clientIn, clientOut, b, name.get(), opponentName);
    } catch (IOException e) {
      // loop infinitely because worker placement failed.
      while (true) {

      }
    }
  }

  @Override
  public List<Action> getTurn(IReadonlyBoard b) {
    try {
      return Message.takeTurn(clientIn, clientOut, b);
    } catch (IOException e) {
      while (true) {

      }
    }
  }

  @Override
  public String getPlayerName() {
    if (name.isPresent()) {
      return name.get();
    }

    String playerName = null;
    try {
      playerName = findName(clientIn);
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.name = Optional.of(playerName);
    return playerName;
  }

  @Override
  public void setPlayerName(String newName) {
    this.name = Optional.of(newName);

    Message.playingAs(clientOut, newName);
  }

  @Override
  public void setOpponentName(String opponentName) {
    this.opponentName = opponentName;

    Message.other(clientOut, opponentName);
  }

  /**
   * Informs the player of the results of the tournament.
   *
   * @param results List of game results
   */
  public void inform(List<GameResult> results) {
    Message.inform(clientOut, results);
  }
}
