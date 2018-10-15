package Player;

import Common.IRules;
import Common.Posn;
import Common.Rules;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StayAliveMovementStrategy implements IMovementStrategy {

  private int numMoves;
  private IRules rules = new Rules();

  StayAliveMovementStrategy(int numMoves) {
    this.numMoves = numMoves;
  }


  @Override
  public Move makeMove(int[][] heights, List<Posn> allWorkers, List<Posn> myWorkers) {
    IGameState currentState = new InProgress(heights, getOpponentWorkers(allWorkers, myWorkers), myWorkers);
    List<IGameState> goodStates = findGoodStates(currentState, numMoves);
    if(!goodStates.isEmpty()) {
      return goodStates.get(0).getMove();
    } else {
      List<IGameState> badStates = findGoodStates(currentState, 1);
      return badStates.get(0).getMove();
    }
  }

  private List<Posn> getOpponentWorkers(List<Posn> allWorkers, List<Posn> myWorkers) {
    List<Posn> opponentWorkers = new ArrayList<>();

    for (Posn p : allWorkers) {
      if (!containsPosn(myWorkers, p)) {
        opponentWorkers.add(p);
      }
    }

    return opponentWorkers;
  }

  private boolean containsPosn(List<Posn> list, Posn posn) {
    for (Posn p : list) {
      if (p.samePosn(posn)) {
        return true;
      }
    }

    return false;
  }

  /**
   *
   * This method returns a list of gamestates that lead to the player not being defeated at a depth
   * of 'depth'
   *
   * If the returned list is empty it means that there is no combination of moves that lead to
   * the player automatically being alive after 'depth' turns.
   *
   * @param state The current state of the game
   * @param depth The desired number of turns to survive
   * @return A list of states that lead to the player surviving
   */
  private List<IGameState> findGoodStates(IGameState state, int depth) {

    List<IGameState> states = branch(state);

    if(depth <= 0) {
      return states;
    }

    List<IGameState> goodStates = new ArrayList<>();

    for(IGameState gs: states) {
      if(isEveryStateGood(gs, depth - 1)) {
        goodStates.add(gs);
      }
    }

    return goodStates;
  }

  public boolean isEveryStateGood(IGameState state, int depth) {
    if (state.isGameOver()) {
      return state.didWin();
    }

    if (depth <= 0) {
      return true;
    }

    List<IGameState> expanded = branchOpponentMove(state);

    for (IGameState gs : expanded) {
      if (!isAnyStateGood(gs, depth - 1)) {
        return false;
      }
    }

    return true;
  }

  private boolean isAnyStateGood(IGameState state, int depth) {
    if (state.isGameOver()) {
      return state.didWin();
    }

    if (depth <= 0) {
      return true;
    }

    List<IGameState> expanded = branch(state);

    for (IGameState gs : expanded) {
      if (isEveryStateGood(gs, depth - 1)) {
        return true;
      }
    }

    return false;
  }

  private List<IGameState> branchOpponentMove(IGameState state) {

    IGameState flipped = state.flipState();
    List<IGameState> flippedStates = branch(flipped);

    List<IGameState> unflippedStates = new ArrayList<>();
    for (IGameState gs : flippedStates) {
      unflippedStates.add(gs.flipState());
    }

    return unflippedStates;
  }


  /**
   * Takes in a game state and returns all the possible game states resulting from
   * one move of the current player in the game.
   *
   * **Public for testing purposes**
   *
   * @param state Current state of the game
   * @return List of all possible states after one round
   */
  List<IGameState> branch(IGameState state) {
    // If the game is over, the state is not going to change.
    if (state.isGameOver()) {
      return Collections.singletonList(state);
    }

    // We know the game is not over, so we have an InProgress state
    InProgress progress = (InProgress) state;
    List<IGameState> possibleStates = new ArrayList<>();

    for (Posn p : progress.getMyWorkers()) {
      for (int dMoveRow = -1; dMoveRow <= 1; dMoveRow += 1) {
        for (int dMoveCol = -1; dMoveCol <= 1; dMoveCol += 1) {
          if (rules.isValidWorkerMove(progress.getHeights(), progress.getAllWorkers(), p, dMoveRow, dMoveCol)) {

            Posn movedWorker = new Posn(p.getRow() + dMoveRow, p.getCol() + dMoveCol);
            List<Posn> updatedWorkers = updatePosn(progress.getMyWorkers(), p, movedWorker);
            InProgress movedState = new InProgress(progress.getHeights(), progress.getOpponentWorkers(), updatedWorkers);

            possibleStates.addAll(getAllBuilds(movedState, movedWorker, dMoveRow, dMoveCol));
          }
        }
      }
    }

    return possibleStates;
  }

  private List<IGameState> getAllBuilds(InProgress state, Posn workerPosn, int dMoveRow, int dMoveCol) {
    List<IGameState> possibleStates = new ArrayList<>();

    for (int dBuildRow = -1; dBuildRow <= 1; dBuildRow += 1) {
      for (int dBuildCol = -1; dBuildCol <= 1; dBuildCol += 1) {
        if (rules.isValidWorkerBuild(state.getHeights(), state.getAllWorkers(), workerPosn, dBuildRow, dBuildCol)) {
          int[][] heights = duplicate(state.getHeights());
          heights[workerPosn.getRow() + dBuildRow][workerPosn.getCol() + dBuildCol] += 1;

          IGameState newState;
          if (rules.isGameOver(heights, state.getAllWorkers(), state.getMyWorkers())) {
            newState = new GameOver(rules.didIWin(heights, state.getAllWorkers(), state.getMyWorkers()));
          } else {
            newState = new InProgress(heights, state.getOpponentWorkers(), state.getMyWorkers());
          }

          Move movement = new Move(
              new Posn(workerPosn.getRow() - dMoveRow, workerPosn.getCol() - dMoveCol),
              dMoveRow,
              dMoveCol,
              dBuildRow,
              dBuildCol
          );

          newState.setMove(movement);
          possibleStates.add(newState);
        }
      }
    }

    return possibleStates;
  }

  /**
   * Creates a new updated list of posns where the old posn is replaced with the new posn
   *
   * @param posns List of posns
   * @param oldPosn Posn to replace
   * @param newPosn Posn to add
   * @return Updated list of posns
   */
  private List<Posn> updatePosn(List<Posn> posns, Posn oldPosn, Posn newPosn) {
    List<Posn> updated = new ArrayList<>(posns);

    for (Posn p : posns) {
      if (p.samePosn(oldPosn)) {
        updated.remove(p);
        updated.add(newPosn);
      }
    }

    return updated;
  }

  /**
   * Duplicates the given 2d array of ints. Assumes array is rectangular.
   *
   * @param arr 2d array to copy
   * @return duplicated 2d array
   */
  private int[][] duplicate(int[][] arr) {
    if (arr.length < 1) {
      return new int[0][0];
    }

    int[][] ret = new int[arr.length][arr[0].length];

    for (int i = 0; i < arr.length; i += 1) {
      for (int j = 0; j < arr[i].length; j += 1) {
        ret[i][j] = arr[i][j];
      }
    }

    return ret;
  }
}

interface IGameState {

  boolean isGameOver();

  boolean didWin();

  Move getMove();

  void setMove(Move move);

  IGameState flipState();
}

class GameOver implements IGameState {
  private boolean didWin;
  private Move prevMove;

  GameOver(boolean didWin) {
    this.didWin = didWin;
  }

  public boolean isGameOver() {
    return true;
  }

  public boolean didWin() {
    return didWin;
  }

  public void setMove(Move move) {
    this.prevMove = move;
  }

  public Move getMove() {
    return this.prevMove;
  }

  public IGameState flipState() {
    return new GameOver(!didWin);
  }
}

class InProgress implements IGameState {
  private Move prevMove;
  private int[][] heights;
  private List<Posn> opponentWorkers;
  private List<Posn> myWorkers;

  public InProgress(int[][] heights, List<Posn> opponentWorkers, List<Posn> myWorkers) {
    this.heights = heights;
    this.opponentWorkers = opponentWorkers;
    this.myWorkers = myWorkers;
  }

  public void setMove(Move move) {
    this.prevMove = move;
  }

  public Move getMove() {
    return this.prevMove;
  }


  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public boolean didWin() {
    return false;
  }

  public List<Posn> getMyWorkers() {
    return myWorkers;
  }

  public List<Posn> getOpponentWorkers() {
    return opponentWorkers;
  }

  public List<Posn> getAllWorkers() {
    List<Posn> allWorkers = new ArrayList<>(this.getMyWorkers());
    allWorkers.addAll(this.getOpponentWorkers());
    return allWorkers;
  }

  public int[][] getHeights() {
    return heights;
  }

  public IGameState flipState() {
    return new InProgress(heights, myWorkers, opponentWorkers);
  }
}


