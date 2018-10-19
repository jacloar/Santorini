package admin.referee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import common.data.Action;
import common.data.PlaceWorkerAction;
import common.rules.IRulesEngine;
import java.util.List;
import org.junit.Test;
import player.IPlayer;

public class RefereeTest {

  public IPlayer makePlayer(String name) {
    IPlayer player = mock(IPlayer.class);
    when(player.getPlayerName()).thenReturn(name);

    List<Action> actionList = (List<Action>) mock(List.class);
    Action action = makeAction();
    when(actionList.get(anyInt())).thenReturn(action);

    when(player.getTurn(any())).thenReturn(actionList);
    return player;
  }

  public IRulesEngine makeRules() {
    IRulesEngine rules = mock(IRulesEngine.class);
    return rules;
  }

  public Action makeAction() {
    Action action = mock(Action.class);
    return action;
  }

  /**
   * Tests that playGame returns the winner of a game
   */
  @Test
  public void testPlayGameReturnsWinner() {
    IPlayer player1 = makePlayer("one");
    IPlayer player2 = makePlayer("two");
    IRulesEngine rules = makeRules();

    when(rules.didPlayerWin(any(), eq("one"))).thenReturn(true);

    IReferee referee = new Referee(rules);
    IPlayer winner = referee.playGame(player1, player2);

    assertThat(winner).isEqualTo(player1);
    assertThat(winner).isNotEqualTo(player2);
  }

  /**
   * Tests that if a player1 cheats during setup, playGame will choose player2 to win
   */
  @Test
  public void testPlayGamePicksTwoNotCheaterSetup() {
    IPlayer player1 = makePlayer("one");
    IPlayer player2 = makePlayer("two");
    IRulesEngine rules = makeRules();

    PlaceWorkerAction placeWorkerAction1 = mock(PlaceWorkerAction.class);
    when(player1.getPlaceWorker(any())).thenReturn(placeWorkerAction1);

    PlaceWorkerAction placeWorkerAction2 = mock(PlaceWorkerAction.class);
    when(player2.getPlaceWorker(any())).thenReturn(placeWorkerAction2);

    when(rules.isPlaceWorkerLegal(any(), eq(placeWorkerAction1))).thenReturn(false);
    when(rules.isPlaceWorkerLegal(any(), eq(placeWorkerAction2))).thenReturn(true);

    IReferee referee = new Referee();
    IPlayer winner = referee.playGame(player1, player2);

    assertThat(winner).isEqualTo(player2);
    assertThat(winner).isNotEqualTo(player1);
  }

  /**
   * Tests that if player1 cheats in run game, playGame will choose player2 to win
   */
  @Test
  public void testPlayGamePicksTwoNotCheaterRunGame() {
    IPlayer player1 = makePlayer("one");
    IPlayer player2 = makePlayer("two");
    IRulesEngine rules = makeRules();

    when(rules.isTurnLegal(any(), any(), eq("two"))).thenReturn(true);
    when(rules.isTurnLegal(any(), any(), eq("one"))).thenReturn(false);

    IReferee referee = new Referee(rules);
    IPlayer winner = referee.playGame(player1, player2);

    assertThat(winner).isEqualTo(player2);
    assertThat(winner).isNotEqualTo(player1);
  }
}
