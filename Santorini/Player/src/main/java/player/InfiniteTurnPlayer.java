package player;

import common.board.IReadonlyBoard;
import common.data.Action;
import java.util.List;
import strategy.DiagonalPlacementStrategy;
import strategy.StayAliveStrategy;
import strategy.Strategy;

/**
 * This player loops when asked for a turn
 */
public class InfiniteTurnPlayer extends AIPlayer {

  /**
   * Constructs an InfiniteTurnPlayer with a default diagonal placement strategy and stay alive strategy.
   * The stay alive strategy should never be used by this player, but it is required for a valid strategy.
   *
   * @param name name of this player
   */
  public InfiniteTurnPlayer(String name) {
    super(name, new Strategy(new DiagonalPlacementStrategy(), new StayAliveStrategy(1), 1));
  }

  @Override
  public List<Action> getTurn(IReadonlyBoard b) {
    while (true) {
      // loop
    }
  }
}
