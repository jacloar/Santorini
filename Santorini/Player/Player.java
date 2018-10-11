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

  private String name;
  private IStrategy strategy;

  private List<Worker> workers = new ArrayList<>(2);

  public Player(IStrategy strategy, String name) {
    this.strategy = strategy;
    this.name = name;
  }

  @Override
  public void addWorker(Worker w) {
    workers.add(w);
  }

  @Override
  public Posn placeWorker(int [][] heights, List<Posn> workerPosns) {
    return strategy.placeWorker(heights, workerPosns, getWorkerPosns());
  }

  @Override
  public Move completeTurn(int[][] heights, List<Posn> workerPosns) {
    return strategy.makeMove(heights, workerPosns, getWorkerPosns());
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

  @Override
  public String getName() {
    return name;
  }
}
