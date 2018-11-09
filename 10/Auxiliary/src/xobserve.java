import common.interfaces.IObserver;
import observer.StdOutObserver;
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

    ITurnStrategy p1TurnStrat = new StayAliveStrategy(1);
    p1TurnStrat.setPlayer(p1Name);
    p1TurnStrat.setOpponent(p2Name);
    ITurnStrategy p2TurnStrat = new StayAliveStrategy(1);

    IPlayer player1 = new AIPlayer(p1Name, new Strategy(p1PlaceStrat, p1TurnStrat));
    IPlayer player2 = new AIPlayer(p2Name, new Strategy(p2PlaceStrat, p2TurnStrat));

    ref.playGame(player1, player2);

    System.exit(0);

  }
}
