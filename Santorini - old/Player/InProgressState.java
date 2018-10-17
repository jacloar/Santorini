package Player;

import Common.Posn;
import java.util.ArrayList;
import java.util.List;

/**
 * If the game is in progress then it should be represented by InProgressState
 */
public class InProgressState implements IGameState {
  private Move prevMove; // The move that lead to this state
  private int[][] heights; // The current representation of the board
  private List<Posn> opponentWorkers;
  private List<Posn> myWorkers;

  public InProgressState(int[][] heights, List<Posn> opponentWorkers, List<Posn> myWorkers) {
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

  /**
   * Gets all of the current players workers
   * @return A list of posn representing the current players workers
   */
  public List<Posn> getMyWorkers() {
    return myWorkers;
  }
  /**
   * Gets all of the current players opponents workers
   * @return A list of posn representing the current players opponents workers
   */
  public List<Posn> getOpponentWorkers() {
    return opponentWorkers;
  }

  /**
   * A list of all the workers on the board in the form of posns
   * @return A list of all the workers posns on the board
   */
  public List<Posn> getAllWorkers() {
    List<Posn> allWorkers = new ArrayList<>(this.getMyWorkers());
    allWorkers.addAll(this.getOpponentWorkers());
    return allWorkers;
  }

  /**
   * Gets the current heights of the buildings on the board
   * @return
   */
  public int[][] getHeights() {
    return heights;
  }

  public IGameState flipState() {
    return new InProgressState(heights, myWorkers, opponentWorkers);
  }
}