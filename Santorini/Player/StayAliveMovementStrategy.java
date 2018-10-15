package Player;

import Common.IRules;
import Common.Posn;
import Common.Rules;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class attempts to return a move that will guarantee to keep the player alive for at most
 * numMoves turns.
 *
 * If a player cannot stay alive for certain numMoves turns it will just give the first valid move
 * it can.
 */
public class StayAliveMovementStrategy implements IMovementStrategy {

  private int numMoves;
  private IRules rules = new Rules();

  public StayAliveMovementStrategy(int numMoves) {
    this.numMoves = numMoves;
  }


  @Override
  public Move makeMove(int[][] heights, List<Posn> allWorkers, List<Posn> myWorkers) {
    IGameState currentState = new InProgressState(heights, getOpponentWorkers(allWorkers, myWorkers), myWorkers);
    List<IGameState> goodStates = findGoodStates(currentState, numMoves);
    if(!goodStates.isEmpty()) {
      return goodStates.get(0).getMove();
    } else {
      List<IGameState> badStates = findGoodStates(currentState, 1);
      return badStates.get(0).getMove();
    }
  }

  /**
   * Returns the list of opponent workers (as posn) from all the workers and my workers
   *
   * @param allWorkers all the workers on the board
   * @param myWorkers the workers that belong to this player
   * @return list of opponent workers
   */
  private List<Posn> getOpponentWorkers(List<Posn> allWorkers, List<Posn> myWorkers) {
    List<Posn> opponentWorkers = new ArrayList<>();

    for (Posn p : allWorkers) {
      if (!containsPosn(myWorkers, p)) {
        opponentWorkers.add(p);
      }
    }

    return opponentWorkers;
  }

  /**
   * Does this list of posns contain one with the same value as the given posn?
   *
   * @param list
   * @param posn
   * @return
   */
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

  /**
   *
   * At the given depth are all of the players options ones that wont lead to a defeat.
   *
   * @param state The current state of the game
   * @param depth The number of turns we need to check
   * @return True if all states keep the player alive, false otherwise.
   */
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

  /**
   * Are any of the possible states leading to a defeat given a state of the game
   * @param state the current state of the game
   * @param depth The depth we are checking from the actual current state of the game
   * @return True if there is a move where every responding move doesn't defeat the player,
   * false if otherwise.
   */
  public boolean isAnyStateGood(IGameState state, int depth) {
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

  /**
   * Iterates through all the permutations for an opponents move given a state representing a move
   * we might make.
   * @param state The state of the board after one of the players possible moves
   * @return The list of states possible after an opponent moves
   */
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

    // We know the game is not over, so we have an InProgressState state
    InProgressState progress = (InProgressState) state;
    List<IGameState> possibleStates = new ArrayList<>();

    for (Posn p : progress.getMyWorkers()) {
      for (int dMoveRow = -1; dMoveRow <= 1; dMoveRow += 1) {
        for (int dMoveCol = -1; dMoveCol <= 1; dMoveCol += 1) {
          if (rules.isValidWorkerMove(progress.getHeights(), progress.getAllWorkers(), p, dMoveRow, dMoveCol)) {

            Posn movedWorker = new Posn(p.getRow() + dMoveRow, p.getCol() + dMoveCol);
            List<Posn> updatedWorkers = updatePosn(progress.getMyWorkers(), p, movedWorker);
            InProgressState movedState = new InProgressState(progress.getHeights(), progress.getOpponentWorkers(), updatedWorkers);

            possibleStates.addAll(getAllBuilds(movedState, movedWorker, dMoveRow, dMoveCol));
          }
        }
      }
    }

    return possibleStates;
  }

  /**
   * Gets all the possible states of the game from a starting state given that a worker just moved
   * and that worker must build on an adjacent building.
   * @param state The current state of the game
   * @param workerPosn The original posn of the worker before they moved
   * @param dMoveRow the delta in rows of the worker posn
   * @param dMoveCol the delta in cols of the worker posn
   * @return All the valid builds from the position specified for the worker
   */
  private List<IGameState> getAllBuilds(InProgressState state, Posn workerPosn, int dMoveRow, int dMoveCol) {
    List<IGameState> possibleStates = new ArrayList<>();

    for (int dBuildRow = -1; dBuildRow <= 1; dBuildRow += 1) {
      for (int dBuildCol = -1; dBuildCol <= 1; dBuildCol += 1) {
        if (rules.isValidWorkerBuild(state.getHeights(), state.getAllWorkers(), workerPosn, dBuildRow, dBuildCol)) {
          int[][] heights = duplicate(state.getHeights());
          heights[workerPosn.getRow() + dBuildRow][workerPosn.getCol() + dBuildCol] += 1;

          IGameState newState;
          if (rules.isGameOver(heights, state.getAllWorkers(), state.getMyWorkers())) {
            newState = new GameOverState(rules.didIWin(heights, state.getAllWorkers(), state.getMyWorkers()));
          } else {
            newState = new InProgressState(heights, state.getOpponentWorkers(), state.getMyWorkers());
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


