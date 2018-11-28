package admin.tournament;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import admin.result.GameResult;
import common.interfaces.IObserver;
import common.interfaces.IPlayer;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import observer.StdOutObserver;
import org.junit.Before;
import org.junit.Test;
import player.AIPlayer;
import player.BreakerPlayer;
import player.InfinitePlayer;
import player.InfiniteTurnPlayer;
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

  /**
   * Tests that the manager assigns a unique name to players if there are duplicates
   * but keeps the first player with each name.
   */
  @Test
  public void testEnsureUniqueNames() {
    IPlayer one = new AIPlayer("one", mockStrategy());
    IPlayer two = new AIPlayer("two", mockStrategy());
    IPlayer oneCopy = new AIPlayer("one", mockStrategy());
    IPlayer twoCopy = new AIPlayer("two", mockStrategy());
    IPlayer three = new AIPlayer("three", mockStrategy());

    List<IPlayer> players = new ArrayList<>();
    players.add(one);
    players.add(two);
    players.add(oneCopy);
    players.add(twoCopy);
    players.add(three);

    manager.ensureUniqueNames(players);

    assertThat(one.getPlayerName()).isEqualTo("one");
    assertThat(two.getPlayerName()).isEqualTo("two");
    assertThat(three.getPlayerName()).isEqualTo("three");

    assertThat(oneCopy.getPlayerName()).isNotEqualTo("one")
                                       .isNotEqualTo(twoCopy.getPlayerName());
    assertThat(twoCopy.getPlayerName()).isNotEqualTo("two");

    assertThat(players.stream().map(IPlayer::getPlayerName)).doesNotHaveDuplicates();
  }

  @Test
  public void testEnsureUniqueNamesInvalidName() {
    IPlayer one = new AIPlayer("one", mockStrategy());
    IPlayer two = new AIPlayer("two", mockStrategy());
    IPlayer invalid1 = new AIPlayer("1", mockStrategy());
    IPlayer invalid2 = new AIPlayer("@", mockStrategy());
    IPlayer invalid3 = new AIPlayer("", mockStrategy());

    List<IPlayer> players = new ArrayList<>();
    players.add(one);
    players.add(two);
    players.add(invalid1);
    players.add(invalid2);
    players.add(invalid3);

    manager.ensureUniqueNames(players);

    assertThat(one.getPlayerName()).isEqualTo("one");
    assertThat(two.getPlayerName()).isEqualTo("two");

    assertThat(invalid1.getPlayerName()).isNotEqualTo("1");
    assertThat(invalid2.getPlayerName()).isNotEqualTo("@");
    assertThat(invalid3.getPlayerName()).isNotEqualTo("");

    assertThat(players.stream().map(IPlayer::getPlayerName)).doesNotHaveDuplicates();
  }

  /**
   * Tests that generateUniqueName creates a name that is not in the player list
   */
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
                      .isNotEqualTo("two")
                      .isNotEqualTo("three");

  }

  /**
   * Tests that updatePlayerName correctly updates the player's name
   */
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

  /**
   * Tests that getResults returns the expected results after a tournament is run
   */
  @Test
  public void testGetResults() {
    String p1Name = "one";
    String p2Name = "two";
    IPlacementStrategy placementP1 = new DiagonalPlacementStrategy();
    IPlacementStrategy placementP2 = new DiagonalPlacementStrategy();
    ITurnStrategy moveP1 = new StayAliveStrategy(1);
    ITurnStrategy moveP2 = new StayAliveStrategy(1);

    Strategy p1Strategy = new Strategy(placementP1, moveP1);
    Strategy p2Strategy = new Strategy(placementP2, moveP2);

    IPlayer p1 = new AIPlayer(p1Name, p1Strategy);
    IPlayer p2 = new AIPlayer(p2Name, p2Strategy);

    List<IPlayer> playerList = new ArrayList<>();
    playerList.add(p1);
    playerList.add(p2);

    manager.runTournament(playerList);

    List<GameResult> results = manager.getResults().get();

    assertThat(results.size()).isEqualTo(3);
    for(GameResult r : results) {
      assertThat(manager.getPlayerName(r.getWinner()).get()).isEqualTo("two");
    }

    assertThat(manager.getCheatersNames().get()).isEmpty();

  }

  /**
   * Tests that if a player cheats, they are added to the cheaters list and do not win.
   */
  @Test
  public void testThereAreCheaters() {
    String p1Name = "one";
    String p2Name = "two";
    IPlacementStrategy placementP1 = new DiagonalPlacementStrategy();
    ITurnStrategy moveP1 = new StayAliveStrategy(1);

    Strategy p1Strategy = new Strategy(placementP1, moveP1);

    IPlayer p1 = new AIPlayer(p1Name, p1Strategy);
    IPlayer p2 = new InfiniteTurnPlayer(p2Name);
    p2.setPlayerName(p2Name);

    List<IPlayer> playerList = new ArrayList<>();
    playerList.add(p2);
    playerList.add(p1);

    List<IPlayer> winners = manager.runTournament(playerList);

    assertThat(manager.getCheatersNames().get().get(0)).isEqualTo(p2Name);

    assertThat(winners).hasSize(1);
    assertThat(winners.get(0).getPlayerName()).isEqualTo(p1Name);

  }

  /**
   * Tests that makeGoodPlayer makes a good player if given an appropriate path
   */
  @Test
  public void testMakeGoodPlayer() {
    String playerName = "player";
    IPlayer player = manager.makePlayer(playerName, "file://Santorini/Player/src/main/java/player/AIPlayer.java");

    assertThat(player).isInstanceOf(AIPlayer.class)
                      .isNotInstanceOf(BreakerPlayer.class)
                      .isNotInstanceOf(InfinitePlayer.class);
    assertThat(player.getPlayerName()).isEqualTo(playerName);
  }

  /**
   * Tests that makeBreakerPlayer makes a breaker player if given an appropriate path
   */
  @Test
  public void testMakeBreakerPlayer() {
    String playerName = "player";
    IPlayer player = manager.makePlayer(playerName, "file://Santorini/Player/src/main/java/player/BreakerPlayer.java");

    assertThat(player).isInstanceOf(BreakerPlayer.class)
                      .isNotInstanceOf(AIPlayer.class)
                      .isNotInstanceOf(InfinitePlayer.class);
  }

  /**
   * Tests that makeInfinitePlayer makes an infinite player if given an appropriate path
   */
  @Test
  public void testMakeInfinitePlayer() {
    String playerName = "player";
    IPlayer player = manager.makePlayer(playerName, "file://Santorini/Player/src/main/java/player/InfinitePlayer.java");

    assertThat(player).isInstanceOf(InfinitePlayer.class)
                      .isNotInstanceOf(AIPlayer.class)
                      .isNotInstanceOf(BreakerPlayer.class);
  }

  /**
   * Tests that makeObserver makes an observer if given an appropriate path
   */
  @Test
  public void testMakeObserver() {
    String observerName = "observer";
    IObserver observer = manager.makeObserver(observerName, "file://Santorini/Observer/src/main/java/observer/StdOutObserver.java");

    assertThat(observer).isInstanceOf(StdOutObserver.class);
  }

  /**
   * Tests that the tournament manager correctly reads given input and runs the specified tournament.
   */
  @Test
  public void testReadInputNoObserver() {
    StringReader input = new StringReader("{"
        + "\"players\" : [[\"good\", \"one\", \"file://Santorini/Player/src/main/java/player/AIPlayer.java\"],"
        + "[\"breaker\", \"two\", \"file://Santorini/Player/src/main/java/player/BreakerPlayer.java\"]],"
        + "\"observers\" : []}");
    manager = new TournamentManager(input);
    List<IPlayer> winners = manager.readInput();

    assertThat(manager.getCheatersNames().get()).hasSize(1)
                                          .containsExactly("two");

    assertThat(winners).hasSize(1);
    assertThat(winners.get(0).getPlayerName()).isEqualTo("one");
  }

  /**
   * Tests that if a tournament is too small it does not run
   */
  @Test
  public void testTournamentTooSmall() {
    IPlayer player = new AIPlayer("player", mockStrategy());
    List<IPlayer> players = new ArrayList<>();
    players.add(player);

    List<IPlayer> winners = manager.runTournament(players);

    assertThat(winners).isEmpty();

    assertThat(manager.getCheatersNames()).isEmpty();
    assertThat(manager.getResults()).isEmpty();
  }

  /**
   * Tests that if a tournament specified from config is too small it does not run
   */
  @Test
  public void testReadInputTournamentTooSmall() {
    StringReader input = new StringReader("{"
        + "\"players\" : [[\"good\", \"one\", \"file://Santorini/Player/src/main/java/player/AIPlayer.java\"]],"
        + "\"observers\" : []}");
    manager = new TournamentManager(input);
    List<IPlayer> winners = manager.readInput();

    assertThat(winners).isEmpty();

    assertThat(manager.getCheatersNames()).isEmpty();
    assertThat(manager.getResults()).isEmpty();
  }

  /**
   * Tests that if the config supplies an observer it is updated with the status of each game
   */
  @Test
  public void testReadInputWithObserver() {
    StringReader input = new StringReader("{"
        + "\"players\" : [[\"good\", \"one\", \"file://Santorini/Player/src/main/java/player/AIPlayer.java\"],"
        + "[\"breaker\", \"two\", \"file://Santorini/Player/src/main/java/player/BreakerPlayer.java\"]],"
        + "\"observers\" : [[\"observer\", \"file://Santorini/Observer/src/main/java/observer/StdOutObserver.java\"]]}");
    manager = new TournamentManager(input);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));

    List<IPlayer> winners = manager.readInput();

    String output = out.toString();

    assertThat(manager.getCheatersNames().get()).hasSize(1)
                                          .containsExactly("two");

    assertThat(winners).hasSize(1);
    assertThat(winners.get(0).getPlayerName()).isEqualTo("one");

    assertThat(output).isEqualToIgnoringWhitespace("[[\"0one1\",0,0,0,0,0],"
        + "[0,0,0,0,0,0],"
        + "[0,0,0,0,0,0],"
        + "[0,0,0,0,0,0],"
        + "[0,0,0,0,0,0],"
        + "[0,0,0,0,0,0]]"
        + "\"two cheated\""
        + "\"one won the game!\"");
  }

  /**
   * Tests that if a tournament is run with one good player and two bad, the good player wins and the others are cheaters.
   */
  @Test
  public void testTwoBadPlayersOneGoodPlayer() {
    StringReader input = new StringReader("{ \"players\" : [[\"good\", \"good\", \"file://Santorini/Player/src/main/java/player/AIPlayer.java\"],\n"
        + "[\"infinite\", \"infiniteplace\", \"file://Santorini/Player/src/main/java/player/InfinitePlayer.java\"],\n"
        + "[\"infinite\", \"infiniteturn\", \"file://Santorini/Player/src/main/java/player/InfiniteTurnPlayer.java\"]],\n"
        + "\"observers\" : []}");

    manager = new TournamentManager(input);

    List<IPlayer> winners = manager.readInput();

    assertThat(manager.getCheatersNames().get()).hasSize(2)
                                          .containsExactly("infiniteplace", "infiniteturn");

    assertThat(winners).hasSize(1);
    assertThat(winners.get(0).getPlayerName()).isEqualTo("good");
  }

}
