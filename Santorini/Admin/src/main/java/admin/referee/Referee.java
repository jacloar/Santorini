package admin.referee;

import admin.result.GameResult;
import common.board.Board;
import common.board.IBoard;
import common.data.Action;
import common.data.PlaceWorkerAction;
import common.rules.IRulesEngine;
import java.util.List;
import java.util.Optional;
import player.IPlayer;

/**
 * Facilitates running game(s) of Santorini between two players.
 */
public class Referee implements IReferee {

  private IRulesEngine rules;

  public Referee(IRulesEngine rules) {
    this.rules = rules;
  }

  @Override
  public IPlayer playGame(IPlayer player1, IPlayer player2) {
    // Interprets the outcome of playGameGetResult
    return playGameGetResult(player1, player2).getWinner();
  }

  /**
   * Facilitates running a game between two players
   *
   * @param player1 One player playing the game
   * @param player2 The other player playing the game
   * @return a GameResult representing the outcome of the game
   */
  private GameResult playGameGetResult(IPlayer player1, IPlayer player2) {
    // Construct the board
    IBoard board = new Board();

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
      return new GameResult(maybeWinner.get(), true);
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
    // Return result if active player won
    if (rules.didPlayerWin(board, active.getPlayerName())) {
      return new GameResult(active, false);
    }

    // Return result if active player lost
    if (rules.didPlayerLose(board, active.getPlayerName())) {
      return new GameResult(waiting, false);
    }

    List<Action> turn = active.getTurn(board);
    // Return result if active player cheats
    if (!rules.isTurnLegal(board, turn, active.getPlayerName())) {
      return new GameResult(waiting, true);
    }

    // Complete move component of turn
    Action move = turn.get(0);
    board.move(move.getWorkerId(), move.getDirection());
    // If the player won after moving, return the game's result
    if (rules.didPlayerWin(board, active.getPlayerName())) {
      return new GameResult(active, false);
    }

    // Complete build component of turn
    Action build = turn.get(1);
    board.build(build.getWorkerId(), build.getDirection());

    // Run a turn with waiting and active players swapped
    return runGame(board, waiting, active);
  }

  @Override
  public Optional<IPlayer> bestOfN(IPlayer player1, IPlayer player2, int games) {
    int countOne = 0;
    int countTwo = 0;

    for (int i = 0; i < games; i += 1) {
      GameResult result = playGameGetResult(player1, player2);
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
}
