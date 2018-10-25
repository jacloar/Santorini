package admin.referee;

import admin.observer.IObserver;
import admin.result.GameResult;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;
import common.board.Board;
import common.board.IBoard;
import common.data.Action;
import common.data.PlaceWorkerAction;
import common.rules.IRulesEngine;
import common.rules.StandardSantoriniRulesEngine;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import player.IPlayer;

/**
 * Facilitates running game(s) of Santorini between two players.
 */
public class Referee implements IReferee {

  private final IRulesEngine rules;
  private final List<IObserver> observers;

  public Referee() {
    this(new StandardSantoriniRulesEngine(), new ArrayList<>());
  }

  public Referee(IRulesEngine rules) {
    this(rules, new ArrayList<>());
  }

  public Referee(List<IObserver> observers) {
    this(new StandardSantoriniRulesEngine(), observers);
  }

  public Referee(IRulesEngine rules, List<IObserver> observers) {
    this.rules = rules;
    this.observers = observers;
  }

  @Override
  public void addObserver(IObserver observer) {
    this.observers.add(observer);
  }

  @Override
  public IPlayer playGame(IPlayer player1, IPlayer player2) {
    // Interprets the outcome of playGameGetResult
    return playGameGetResult(new Board(), player1, player2).getWinner();
  }

  @Override
  public IPlayer playGame(IBoard board, IPlayer player1, IPlayer player2) {
    return playGameGetResult(board, player1, player2).getWinner();
  }

  /**
   * Facilitates running a game between two players
   *
   * @param board board to play the game on
   * @param player1 One player playing the game
   * @param player2 The other player playing the game
   * @return a GameResult representing the outcome of the game
   */
  private GameResult playGameGetResult(IBoard board, IPlayer player1, IPlayer player2) {
    if (player1.getPlayerName().equals(player2.getPlayerName())) {
      throw new IllegalArgumentException("Player names cannot be equal");
    }

    // Determine order of play
    int p1Foo = player1.howFooAmI();
    int p2Foo = player2.howFooAmI();

    IPlayer first;
    IPlayer second;
    if (p1Foo >= p2Foo) {
      first = player1;
      second = player2;
    } else {
      first = player2;
      second = player1;
    }

    // Sets up the game. Returns the winner if the other player tries to cheat.
    Optional<IPlayer> maybeWinner = setupGame(board, first, second);
    if (maybeWinner.isPresent()) {
      IPlayer winner = maybeWinner.get();

      // If player 1 is the winner, player 2 cheated.
      IPlayer cheater = player1 == winner ? player2 : player1;

      updateObservers(observer -> observer.updateError(cheater + " cheated"));
      updateObservers(observer -> observer.updateWin(winner));
      return new GameResult(winner, true);
    }
    return runGame(board, first, second);
  }

  /**
   * Runs the set up of the game. Places the worker from the given players onto the board.
   * If a player tries to give an invalid placement, other player wins.
   *
   * @param board IBoard to place workers on
   * @param player1 IPlayer going first
   * @param player2 IPlayer going second
   * @return Empty if players gave valid placements, or winner if other player tried to cheat
   */
  private Optional<IPlayer> setupGame(IBoard board, IPlayer player1, IPlayer player2) {
    for (int i = 0; i < rules.getNumberOfWorkers(); i += 1) {
      if (!placeWorker(board, player1)) {
        return Optional.of(player2);
      }

      if (!placeWorker(board, player2)) {
        return Optional.of(player1);
      }
    }

    return Optional.empty();
  }

  /**
   * Places a worker from the given player onto the board.
   *
   * @param board IBoard to place worker on   * @param player IPlayer to place the worker
   * @return true if valid placement, false otherwise
   */
  private boolean placeWorker(IBoard board, IPlayer player) {
    PlaceWorkerAction placement = player.getPlaceWorker(board);

    if (rules.isPlaceWorkerLegal(board, placement)) {
      board.createWorker(placement.getWorkerId(), placement.getRow(), placement.getColumn());
      updateObservers(observer -> observer.update(board));
      return true;
    }

    return false;
  }

  /**
   * Runs the game with the given players on the given board.
   *
   * @param board IBoard the game is played on
   * @param active IPlayer that is currently making a turn
   * @param waiting IPlayer that is waiting for the other player to complete their turn
   * @return GameResult representing the result of the game
   */
  private GameResult runGame(IBoard board, IPlayer active, IPlayer waiting) {
    updateObservers(observer -> observer.update(board));
    // Return result if active player won
    if (rules.didPlayerWin(board, active.getPlayerName())) {
      updateObservers(observer -> observer.updateWin(active));
      return new GameResult(active, false);
    }

    // Return result if active player lost
    if (rules.didPlayerLose(board, active.getPlayerName())) {
      updateObservers(observer -> observer.updateGiveUp(active));
      updateObservers(observer -> observer.updateWin(waiting));
      return new GameResult(waiting, false);
    }

    List<Action> turn = active.getTurn(board);
    // Return result if active player cheats
    if (!rules.isTurnLegal(board, turn, active.getPlayerName())) {
      updateObservers(observer -> observer.updateError(active + " cheated"));
      updateObservers(observer -> observer.updateWin(waiting));
      return new GameResult(waiting, true);
    }

    // Complete move component of turn
    Action move = turn.get(0);
    board.move(move.getWorkerId(), move.getDirection());
    // If the player won after moving, return the game's result
    if (rules.didPlayerWin(board, active.getPlayerName())) {
      updateObservers(observer -> observer.update(turn));
      updateObservers(observer -> observer.updateWin(active));
      return new GameResult(active, false);
    }

    // Complete build component of turn
    Action build = turn.get(1);
    board.build(build.getWorkerId(), build.getDirection());

    updateObservers(observer -> observer.update(turn));
    // Run a turn with waiting and active players swapped
    return runGame(board, waiting, active);
  }

  @Override
  public Optional<IPlayer> bestOfN(IPlayer player1, IPlayer player2, int games) {
    int countOne = 0;
    int countTwo = 0;

    for (int i = 0; i < games; i += 1) {
      GameResult result = playGameGetResult(new Board(), player1, player2);
      if (result.didOtherPlayerCheat()) {
        return Optional.of(result.getWinner());
      }

      if (result.getWinner() == player1) {
        countOne += 1;
      } else if (result.getWinner() == player2) {
        countTwo += 1;
      }
    }

    // If player1 won more games, return player1
    // If player2 won more games, return player2
    // If the players won equal numbers of games, return empty
    if (countOne > countTwo) {
      return Optional.of(player1);
    } else if (countTwo > countOne) {
      return Optional.of(player2);
    } else {
      return Optional.empty();
    }
  }

  /**
   * Runs the specified function on all the observers
   *
   * @param updateFunc function that takes observer and calls an update method on it
   */
  private void updateObservers(Consumer<IObserver> updateFunc) {
    for (IObserver o : observers) {
      updateFunc.accept(o);
    }
  }
}
