package client;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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

  private static String name = "name";
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

  @Test
  public void testNonEmptyWorkerPlace() {
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

}
