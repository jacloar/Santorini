package player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import common.board.Board;
import common.interfaces.IPlayer;
import org.junit.Test;
import strategy.Strategy;

/**
 * Tests for AI player implementation
 */
public class TestAIPlayer {

  private Strategy mockStrategy(Appendable app) {
    Strategy strategy = mock(Strategy.class);
    when(strategy.getTurn(any())).then(invocation -> {
      app.append("call to getTurn\n");
      return null;
    });
    when(strategy.getPlaceWorker(any())).then(invocation -> {
      app.append("call to getPlaceWorker\n");
      return null;
    });

    return strategy;
  }

  @Test
  public void testNoNameGivenHasName() {
    IPlayer player = new AIPlayer(null);

    assertThat(player.getPlayerName()).isNotEmpty();
  }

  @Test
  public void testGetPlaceWorkerCallsStrategy() {
    StringBuilder builder = new StringBuilder();
    IPlayer player = new AIPlayer(mockStrategy(builder));

    player.getPlaceWorker(new Board());

    assertThat(builder).containsIgnoringCase("call to getPlaceWorker");
  }

  @Test
  public void testGetTurnCallsStrategy() {
    StringBuilder builder = new StringBuilder();
    IPlayer player = new AIPlayer(mockStrategy(builder));

    player.getTurn(new Board());

    assertThat(builder).containsIgnoringCase("call to getTurn");
  }

}
