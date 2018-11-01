package admin.tournament;

import common.interfaces.IPlayer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import admin.result.GameResult;
import player.AIPlayer;
import strategy.CartesianDistancePlacementStrategy;
import strategy.DiagonalPlacementStrategy;
import strategy.IPlacementStrategy;
import strategy.ITurnStrategy;
import strategy.StayAliveStrategy;
import strategy.Strategy;

import static org.assertj.core.api.Assertions.assertThat;

public class tournamentTest {

  private TournamentManager manager;

  @Before
  public void init(){
    manager = new TournamentManager();
  }



  @Test
  public void testGenerateUniqueNames() {
    IPlayer p1 = new AIPlayer("one", null);
    IPlayer p2 = new AIPlayer("two", null);
    IPlayer p3 = new AIPlayer("three", null);

    List<IPlayer> playerList = new ArrayList<>();
    playerList.add(p1);
    playerList.add(p2);
    playerList.add(p3);

    String unique = manager.generateUniqueName(playerList);

    assertThat(unique).isNotEqualTo("one")
            .isNotEqualTo("two").isNotEqualTo("three");

  }


  @Test
  public void testUpdatePlayerName() {
    IPlayer p1 = new AIPlayer("one", null);
    IPlayer p2 = new AIPlayer("two", null);
    IPlayer p3 = new AIPlayer("three", null);

    List<IPlayer> playerList = new ArrayList<>();
    playerList.add(p1);
    playerList.add(p2);
    playerList.add(p3);

    String newName = manager.updatePlayerName(p1, playerList);


    assertThat(newName).isNotEqualTo("one")
            .isNotEqualTo("two").isNotEqualTo("three");

    assertThat(p1.getPlayerName()).isEqualTo(newName);
  }


  @Test
  public void testGetResults() {
    String p1Name = "one";
    String p2Name = "two";
    IPlacementStrategy placementP1 = new DiagonalPlacementStrategy();
    IPlacementStrategy placementP2 = new DiagonalPlacementStrategy();
    ITurnStrategy moveP1 = new StayAliveStrategy(p1Name, p2Name);
    ITurnStrategy moveP2 = new StayAliveStrategy(p2Name, p1Name);

    Strategy p1Strategy = new Strategy(placementP1, moveP1, 1);
    Strategy p2Strategy = new Strategy(placementP2, moveP2, 1);

    IPlayer p1 = new AIPlayer(p1Name, p1Strategy);
    IPlayer p2 = new AIPlayer(p2Name, p2Strategy);

    List<IPlayer> playerList = new ArrayList<>();
    playerList.add(p1);
    playerList.add(p2);


    manager.runTournament(playerList);

    List<GameResult> results = manager.getResults();

    assertThat(results).isEqualTo("");
    }

}
