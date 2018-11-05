package player;

import static org.assertj.core.api.Assertions.assertThat;

import common.data.Action;
import common.data.ActionType;
import common.data.Direction;
import common.data.PlaceWorkerAction;
import common.interfaces.IPlayer;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TestBreakerPlayer {

  private IPlayer breaker;

  @Before
  public void init() {
    breaker = new BreakerPlayer("player");
  }

  @Test
  public void testGetPlaceWorker() {
    PlaceWorkerAction placement = breaker.getPlaceWorker(null);

    assertThat(placement.getRow()).isEqualTo(-1);
    assertThat(placement.getColumn()).isEqualTo(3);
  }

  @Test
  public void testGetTurn() {
    List<Action> turn = breaker.getTurn(null);

    assertThat(turn).hasSize(1);

    Action build = turn.get(0);

    assertThat(build.getType()).isEqualTo(ActionType.BUILD);
    assertThat(build.getWorkerId()).isEqualTo("id");

    Direction direction = build.getDirection();

    assertThat(direction.getRowModifier()).isEqualTo(1);
    assertThat(direction.getColumnModifier()).isEqualTo(1);
  }

  @Test
  public void testGetName() {
    assertThat(breaker.getPlayerName()).isNull();
  }
}
