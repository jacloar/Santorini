import admin.observer.IObserver;
import admin.observer.StdOutObserver;
import admin.referee.IReferee;
import admin.referee.Referee;
import common.interfaces.IPlayer;
import player.AIPlayer;
import strategy.DiagonalPlacementStrategy;
import strategy.IPlacementStrategy;
import strategy.ITurnStrategy;
import strategy.StayAliveStrategy;
import strategy.Strategy;

/**
 * Test harness that runs the referee
 */
public class xobserve {

  public static void main(String[] args) {
    IObserver observer = new StdOutObserver();

    IReferee ref = new Referee();
    ref.addObserver(observer);

    String p1Name = "one";
    String p2Name = "two";

    IPlacementStrategy p1PlaceStrat = new DiagonalPlacementStrategy();
    IPlacementStrategy p2PlaceStrat = new DiagonalPlacementStrategy();

    ITurnStrategy p1TurnStrat = new StayAliveStrategy(p1Name, p2Name);
    ITurnStrategy p2TurnStrat = new StayAliveStrategy(p2Name, p1Name);

    IPlayer player1 = new AIPlayer(p1Name, new Strategy(p1PlaceStrat, p1TurnStrat, 1));
    IPlayer player2 = new AIPlayer(p2Name, new Strategy(p2PlaceStrat, p2TurnStrat, 1));

    ref.playGame(player1, player2);

  }
}
