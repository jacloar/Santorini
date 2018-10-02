/**
 * Implementation of the rule checker
 */
public class Rules implements IRules {

  @Override
  public boolean isValidWorkerMove(IBoard board, Worker worker, int dx, int dy) {

    return false;
  }

  @Override
  public boolean isValidWorkerBuild(IBoard board, Worker worker, int dx, int dy) {
    return false;
  }

  @Override
  public boolean isValidPlaceWorker(IBoard board, int x, int y) {
    return false;
  }
}
