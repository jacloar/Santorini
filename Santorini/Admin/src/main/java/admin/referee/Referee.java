package admin.referee;

import admin.observer.IObserver;
import admin.result.GameResult;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import common.board.Board;
import common.board.IBoard;
import common.data.Action;
import common.data.PlaceWorkerAction;
import common.rules.IRulesEngine;
import common.rules.StandardSantoriniRulesEngine;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;
import common.interfaces.IPlayer;

/**
 * Facilitates running game(s) of Santorini between two players.
 */
public class Referee implements IReferee {

  // Length of time to wait for action in seconds
  private static final int TIMEOUT = 5;

  private final IRulesEngine rules;
  private final List<IObserver> observers;

  private final ExecutorService executor = Executors.newSingleThreadExecutor();

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
    Optional<String> p1Name = timedCall(IPlayer::getPlayerName, player1);
    Optional<String> p2Name = timedCall(IPlayer::getPlayerName, player2);

    if (hasName(player1, player2, p1Name)) {
      return new GameResult(player2, true);
    }

    if (hasName(player2, player1, p2Name)) {
      return new GameResult(player1, true);
    }

    // We know the player names must both be present because got passed the two hasName method calls
    if (p1Name.get().equals(p2Name.get())) {
      throw new IllegalArgumentException("Player names cannot be equal");
    }

    // Sets up the game. Returns the winner if the other player tries to cheat.
    Optional<IPlayer> maybeWinner = setupGame(board, player1, player2);
    if (maybeWinner.isPresent()) {
      IPlayer winner = maybeWinner.get();

      // If player 1 is the winner, player 2 cheated.
      IPlayer cheater = player1 == winner ? player2 : player1;

      return activeCheated(cheater, winner);
    }
    return runGame(board, player1, player2);
  }

  private boolean hasName(IPlayer player, IPlayer opponent, Optional<String> name) {
    if (!name.isPresent()) {
      updateObservers(observer -> observer.updateError(player + " took too long to give name"));
      updateObservers(observer -> observer.updateWin(opponent));
      return true;
    }
    return false;
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
    Optional<PlaceWorkerAction> optionalPlacement = timedCall(p -> p.getPlaceWorker(board), player);

    // If the placement is not present, the player took too long and we consider that cheating
    if (!optionalPlacement.isPresent()) {
      return false;
    }

    PlaceWorkerAction placement = optionalPlacement.get();

    if (rules.isPlaceWorkerLegal(board, placement)) {
      board.createWorker(placement.getWorkerId(), placement.getRow(), placement.getColumn());
      updateObservers(observer -> observer.update(board));
      return true;
    }

    // If the placement was not legal, return false as the player cheated
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
      updateObservers(observer -> observer.update(board));
      updateObservers(observer -> observer.updateWin(active));
      return new GameResult(active, false);
    }

    // Return result if active player lost
    if (rules.didPlayerLose(board, active.getPlayerName())) {
      updateObservers(observer -> observer.updateGiveUp(active));
      updateObservers(observer -> observer.updateWin(waiting));
      return new GameResult(waiting, false);
    }

    Optional<List<Action>> optionalTurn = timedCall(p -> p.getTurn(board), active);
    if (!optionalTurn.isPresent()) {
      return activeCheated(active, waiting);
    }

    List<Action> turn = optionalTurn.get();
    // Return result if active player cheats
    if (!rules.isTurnLegal(board, turn, active.getPlayerName())) {
      return activeCheated(active, waiting);
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

  /**
   * Returns a game result where the active player cheated. Updates observers accordingly.
   *
   * @param active active player
   * @param waiting waiting player
   * @return GameResult where waiting won and didOtherPlayerCheat true
   */
  private GameResult activeCheated(IPlayer active, IPlayer waiting) {
    updateObservers(observer -> observer.updateError(active + " cheated"));
    updateObservers(observer -> observer.updateWin(waiting));
    return new GameResult(waiting, true);
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

  /**
   * Calls the given function on the given player, but makes sure that the call does not last
   * longer than 5 seconds.
   *
   * @param func function to call on player
   * @param player player to call function on
   * @param <T> type the function returns
   * @return the result of the function, or empty if timeout
   */
  private <T> Optional<T> timedCall(Function<IPlayer, T> func, IPlayer player) {
    TimeLimiter limiter = SimpleTimeLimiter.create(executor);
    T result;
    try {
      result = limiter.callWithTimeout(() -> func.apply(player), TIMEOUT, TimeUnit.SECONDS);
    } catch (TimeoutException | InterruptedException | ExecutionException e) {
      return Optional.empty();
    }

    return Optional.of(result);
  }
}
