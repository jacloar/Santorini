package admin.tournament;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import observer.StdOutObserver;
import admin.result.GameResult;
import common.interfaces.IObserver;
import common.interfaces.IPlayer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import player.AIPlayer;
import player.BreakerPlayer;
import player.InfinitePlayer;
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
    IPlayer p2 = new BreakerPlayer(p2Name);
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
    IPlayer player = manager.makeGoodPlayer(playerName, "file://Santorini/Player/src/main/java/player/AIPlayer.java");

    assertThat(player).isInstanceOf(AIPlayer.class)
                      .isNotInstanceOf(BreakerPlayer.class)
                      .isNotInstanceOf(InfinitePlayer.class);
    assertThat(player.getPlayerName()).isEqualTo(playerName);
  }

  @Test
  public void testMakeBreakerPlayer() {
    String playerName = "player";
    IPlayer player = manager.makeBreakerPlayer(playerName, "file://Santorini/Player/src/main/java/player/BreakerPlayer.java");

    assertThat(player).isInstanceOf(BreakerPlayer.class)
                      .isNotInstanceOf(AIPlayer.class)
                      .isNotInstanceOf(InfinitePlayer.class);
  }

  @Test
  public void testMakeInfinitePlayer() {
    String playerName = "player";
    IPlayer player = manager.makeInfinitePlayer(playerName, "file://Santorini/Player/src/main/java/player/InfinitePlayer.java");

    assertThat(player).isInstanceOf(InfinitePlayer.class)
                      .isNotInstanceOf(AIPlayer.class)
                      .isNotInstanceOf(BreakerPlayer.class);
  }

  @Test
  public void testMakeObserver() {
    String observerName = "observer";
    IObserver observer = manager.makeObserver(observerName, "file://Santorini/Observer/src/main/java/observer/StdOutObserver.java");

    assertThat(observer).isInstanceOf(StdOutObserver.class);
  }

  @Test
  public void testReadInputNoObserver() {
    StringReader input = new StringReader("{"
        + "\"players\" : [[\"good\", \"one\", \"file://Santorini/Player/src/main/java/player/AIPlayer.java\"],"
        + "[\"breaker\", \"two\", \"file://Santorini/Player/src/main/java/player/BreakerPlayer.java\"]],"
        + "\"observers\" : []}");
    manager = new TournamentManager(input);
    Optional<IPlayer> maybeWinner = manager.readInput();

    assertThat(manager.getCheatersNames()).hasSize(1)
                                          .containsExactly("two");

    assertThat(maybeWinner).isPresent();
    assertThat(maybeWinner.get().getPlayerName()).isEqualTo("one");
  }

  @Test
  public void testTournamentTooSmall() {
    IPlayer player = new AIPlayer("player", mockStrategy());
    List<IPlayer> players = new ArrayList<>();
    players.add(player);

    Optional<IPlayer> maybeWinner = manager.runTournament(players);

    assertThat(maybeWinner).isEmpty();
  }

  @Test
  public void testReadInputTournamentTooSmall() {
    StringReader input = new StringReader("{"
        + "\"players\" : [[\"good\", \"one\", \"file://Santorini/Player/src/main/java/player/AIPlayer.java\"]],"
        + "\"observers\" : []}");
    manager = new TournamentManager(input);
    Optional<IPlayer> maybeWinner = manager.readInput();

    assertThat(maybeWinner).isEmpty();
  }

  @Test
  public void testReadInputWithObserver() {
    StringReader input = new StringReader("{"
        + "\"players\" : [[\"good\", \"one\", \"file://Santorini/Player/src/main/java/player/AIPlayer.java\"],"
        + "[\"breaker\", \"two\", \"file://Santorini/Player/src/main/java/player/BreakerPlayer.java\"]],"
        + "\"observers\" : [[\"observer\", \"file://Santorini/Observer/src/main/java/observer/StdOutObserver.java\"]]}");
    manager = new TournamentManager(input);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));

    Optional<IPlayer> maybeWinner = manager.readInput();

    String output = out.toString();

    assertThat(manager.getCheatersNames()).hasSize(1)
                                          .containsExactly("two");

    assertThat(maybeWinner).isPresent();
    assertThat(maybeWinner.get().getPlayerName()).isEqualTo("one");

    assertThat(output).isEqualToIgnoringWhitespace("[[\"0one1\",0,0,0,0,0],"
        + "[0,0,0,0,0,0],"
        + "[0,0,0,0,0,0],"
        + "[0,0,0,0,0,0],"
        + "[0,0,0,0,0,0],"
        + "[0,0,0,0,0,0]]"
        + "\"two cheated\""
        + "\"one won the game!\"");
  }

}
