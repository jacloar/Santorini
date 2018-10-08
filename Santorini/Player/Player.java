package Player;

import Common.IBoard;
import Common.Posn;
import Common.Worker;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of Player. Wrapper around the IStrategy it is given.
 */
public class Player implements IPlayer {

  private IBoard board;
  private IStrategy strategy;

  private List<Worker> workers = new ArrayList<>(2);

  public Player(IBoard board, IStrategy strategy) {
    this.board = board;
    this.strategy = strategy;
  }

  @Override
  public void addWorker(Worker w) {
    workers.add(w);
  }

  @Override
  public Posn placeWorker() {
    return strategy.placeWorker(board.getHeights(), board.getWorkers(), getWorkerPosns());
  }

  @Override
  public Move completeTurn() {
    return strategy.makeMove(board.getHeights(), board.getWorkers(), getWorkerPosns());
  }

  /**
   * Converts the list of workers to a list of positions
   *
   * @return a list of positions representing the workers.
   */
  private List<Posn> getWorkerPosns() {
    List<Posn> workerPosns = new ArrayList<>(2);

    for (Worker w : workers) {
      workerPosns.add(w.getPosn());
    }
    return workerPosns;
  }
}
