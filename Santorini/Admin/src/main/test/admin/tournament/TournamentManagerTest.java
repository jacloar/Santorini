package admin.tournament;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import admin.result.GameResult;
import common.interfaces.IPlayer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import player.AIPlayer;
import player.BreakerPlayer;
import strategy.DiagonalPlacementStrategy;
import strategy.IPlacementStrategy;
import strategy.ITurnStrategy;
import strategy.StayAliveStrategy;
import strategy.Strategy;

public class TournamentManagerTest {

  private TournamentManager manager;

  @Before
  public void init(){
    manager = new TournamentManager();
  }

  private Strategy mockStrategy() {
    return mock(Strategy.class);
  }


  @Test
  public void testGenerateUniqueNames() {
    IPlayer p1 = new AIPlayer("one", mockStrategy());
    IPlayer p2 = new AIPlayer("two", mockStrategy());
    IPlayer p3 = new AIPlayer("three", mockStrategy());

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
    IPlayer p1 = new AIPlayer("one", mockStrategy());
    IPlayer p2 = new AIPlayer("two", mockStrategy());
    IPlayer p3 = new AIPlayer("three", mockStrategy());

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
    ITurnStrategy moveP1 = new StayAliveStrategy();
    ITurnStrategy moveP2 = new StayAliveStrategy();

    Strategy p1Strategy = new Strategy(placementP1, moveP1, 1);
    Strategy p2Strategy = new Strategy(placementP2, moveP2, 1);

    IPlayer p1 = new AIPlayer(p1Name, p1Strategy);
    IPlayer p2 = new AIPlayer(p2Name, p2Strategy);

    List<IPlayer> playerList = new ArrayList<>();
    playerList.add(p1);
    playerList.add(p2);

    manager.runTournament(playerList);

    List<GameResult> results = manager.getResults();

    assertThat(results.size()).isEqualTo(3);
    for(GameResult r : results) {
      assertThat(manager.getPlayerName(r.getWinner()).get()).isEqualTo("two");
    }

    assertThat(manager.getCheatersNames()).isEmpty();

  }


  @Test
  public void testThereAreCheaters() {
    String p1Name = "one";
    String p2Name = "two";
    IPlacementStrategy placementP1 = new DiagonalPlacementStrategy();
    ITurnStrategy moveP1 = new StayAliveStrategy();

    Strategy p1Strategy = new Strategy(placementP1, moveP1, 1);

    IPlayer p1 = new AIPlayer(p1Name, p1Strategy);
    IPlayer p2 = new BreakerPlayer();
    p2.setPlayerName(p2Name);

    List<IPlayer> playerList = new ArrayList<>();
    playerList.add(p1);
    playerList.add(p2);

    manager.runTournament(playerList);

    assertThat(manager.getCheatersNames().get(0)).isEqualTo("two");

  }


  @Test
  public void testMakeGoodPlayer() {
    String playerName = "player";
    AIPlayer player = manager.makeGoodPlayer(playerName, "file://Santorini/Player/src/main/java/player/AIPlayer.java");

    assertThat(player.getPlayerName()).isEqualTo(playerName);
  }

}
