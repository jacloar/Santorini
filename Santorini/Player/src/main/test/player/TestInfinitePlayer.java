package player;

import static org.assertj.core.api.Assertions.assertThat;

import common.data.Action;
import common.data.PlaceWorkerAction;
import common.interfaces.IPlayer;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import utils.Utils;

public class TestInfinitePlayer {
  private IPlayer inf;
  private int timeout = 5;

  @Before
  public void init() {
    inf = new InfinitePlayer();
  }

  @Test
  public void testGetPlayerNameLoops() {
    Optional<String> name = Utils.timedCall(inf, IPlayer::getPlayerName, timeout);

    assertThat(name).isEmpty();
  }

  @Test
  public void testGetPlaceWorkerLoop() {
    Optional<PlaceWorkerAction> placement = Utils.timedCall(inf, p -> p.getPlaceWorker(null), timeout);

    assertThat(placement).isEmpty();
  }

  @Test
  public void testGetTurn() {
    Optional<List<Action>> turn = Utils.timedCall(inf, p -> p.getTurn(null), timeout);

    assertThat(turn).isEmpty();
  }

}
