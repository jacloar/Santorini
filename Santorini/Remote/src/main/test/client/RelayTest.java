package client;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import common.board.Board;
import common.board.IBoard;
import common.interfaces.IPlayer;
import org.junit.Before;
import org.junit.Test;
import player.AIPlayer;
import strategy.DiagonalPlacementStrategy;
import strategy.IPlacementStrategy;
import strategy.ITurnStrategy;
import strategy.StayAliveStrategy;
import strategy.Strategy;

public class RelayTest {

  private static String name = "one";
  private static ObjectMapper mapper = new ObjectMapper();

  private Relay relay;
  private IPlayer player;

  @Before
  public void init() {
    IPlacementStrategy placementStrategy = new DiagonalPlacementStrategy();
    ITurnStrategy turnStrategy = new StayAliveStrategy(1);

    Strategy strategy = new Strategy(placementStrategy, turnStrategy);

    player = new AIPlayer(name, strategy);
    relay = new Relay(player);
  }

  /**
   * Tests that a playing as message will update the name of a player
   */
  @Test
  public void testPlayingAs() {
    String playingAs = "playing-as";
    String newName = "new";

    ArrayNode playingAsMessage = mapper.createArrayNode();
    playingAsMessage.add(playingAs);
    playingAsMessage.add(newName);

    assertThat(player.getPlayerName()).isEqualTo(name);

    relay.respond(playingAsMessage);

    assertThat(player.getPlayerName()).isEqualTo(newName);
  }

  /**
   * Tests that an empty placement will get back a valid place
   */
  @Test
  public void testEmptyPlacement() {
    ArrayNode placementMessage = mapper.createArrayNode();

    JsonNode place = relay.respond(placementMessage).get();

    assertThat(place.isArray()).isTrue();
    assertThat(place).hasSize(2);

    assertThat(place.get(0).isInt()).isTrue();
    assertThat(place.get(1).isInt()).isTrue();

    assertThat(place.get(0).asInt()).isEqualTo(0);
    assertThat(place.get(1).asInt()).isEqualTo(0);
  }

  /**
   * Tests that a non empty placement will get back a valid place
   */
  @Test
  public void testNonEmptyPlacement() {
    String otherWorker = "other1";
    int otherX = 0;
    int otherY = 0;

    ArrayNode workerPlace = mapper.createArrayNode();
    workerPlace.add(otherWorker);
    workerPlace.add(otherX);
    workerPlace.add(otherY);

    ArrayNode placement = mapper.createArrayNode();
    placement.add(workerPlace);

    JsonNode place = relay.respond(placement).get();

    assertThat(place.isArray()).isTrue();
    assertThat(place).hasSize(2);

    assertThat(place.get(0).isInt()).isTrue();
    assertThat(place.get(1).isInt()).isTrue();

    assertThat(place.get(0).asInt()).isEqualTo(1);
    assertThat(place.get(1).asInt()).isEqualTo(1);
  }

  /**
   * Tests that a take turn message will result in an appropriate action
   */
  @Test
  public void testTakeTurn() {
    String player1 = name;
    String player2 = "two";

    IBoard board = new Board();

    board.createWorker(player1, 0, 0);
    board.createWorker(player2, 1, 1);
    board.createWorker(player1, 2, 2);
    board.createWorker(player2, 3, 3);

    player.setOpponentName(player2);

    JsonNode action = relay.respond(board.toJson()).get();

    assertThat(action.isArray()).isTrue();
    assertThat(action).hasSize(5);
    assertThat(action.get(0).asText()).isEqualTo("one1");
    assertThat(action.get(1).asText()).isEqualTo("EAST");
    assertThat(action.get(2).asText()).isEqualTo("PUT");
    assertThat(action.get(3).asText()).isEqualTo("EAST");
    assertThat(action.get(4).asText()).isEqualTo("PUT");
  }

}
