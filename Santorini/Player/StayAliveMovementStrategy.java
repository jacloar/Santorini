package Player;

import Common.Posn;
import java.util.List;
import java.util.function.Function;

public class StayAliveMovementStrategy implements IMovementStrategy {

  private int numMoves;

  StayAliveMovementStrategy(int numMoves) {
    this.numMoves = numMoves;
  }


  @Override
  public Move makeMove(int[][] heights, List<Posn> workersOnBoard, List<Posn> myWorkers) {
    return null;
  }

  private Function<>
}

interface IGameState {

  boolean isGameOver();

  boolean didWin();

}

class GameOver implements IGameState {
  boolean didWin;

  public boolean isGameOver() {
    return true;
  }

  public boolean didWin() {
    return didWin;
  }
}

class InProgress implements IGameState {
  int[][] heights;
  List<Posn> opponentWorkers;
  List<Posn> myWorkers;

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public boolean didWin() {
    return false;
  }
}

interface IGameTree {

  IGameTree expand(IGameState state);
}

class GameTree implements IGameTree {

  private List<IGameState> states;
  private Function<IGameState, IGameTree> search;

  public GameTree(List<IGameState> states, Function<IGameState, IGameTree> search) {
    this.states = states;
    this.search = search;
  }


  @Override
  public IGameTree expand(IGameState state) {
    return search.apply(state);
  }
}


