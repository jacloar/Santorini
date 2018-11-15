package server.player;

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

  private static ObjectMapper mapper = new ObjectMapper();

  public RemotePlayer(Socket client) throws IOException {
    this.clientIn = new InputStreamReader(client.getInputStream());
    this.clientOut = new PrintStream(client.getOutputStream());
  }

  private String findName(InputStreamReader in) throws IOException {
    JsonParser parser = new JsonFactory().createParser(in);
    JsonNode nameNode = mapper.readTree(parser);

    return nameNode.asText();
  }

  @Override
  public PlaceWorkerAction getPlaceWorker(IReadonlyBoard b) {
    return null;
  }

  @Override
  public List<Action> getTurn(IReadonlyBoard b) {
    return null;
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

  }

  @Override
  public void setOpponentName(String opponentName) {

  }
}
