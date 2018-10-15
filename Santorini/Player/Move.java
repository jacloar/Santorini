package Player;

import Common.Posn;

/**
 * A move always has a Posn representing a worker. A move occurs in
 * two steps. First the specified worker moves moveDx, moveDy in [-1,1]. Then
 * from THAT location the worker may build to an adjacent building specified
 * by buildDx, buildDy in [-1,1].
 */
class Move {

  private Posn workerPosn;

  private int moveDx;
  private int moveDy;

  private int buildDx;
  private int buildDy;


  public Move(Posn workerPosn, int moveDx, int moveDy, int buildDx, int buildDy) {
    this.workerPosn = workerPosn;
    this.moveDx = moveDx;
    this.moveDy = moveDy;
    this.buildDx = buildDx;
    this.buildDy = buildDy;
  }

  public Posn getWorkerPosn() {
    return workerPosn;
  }

  public int getMoveDx() {
    return moveDx;
  }

  public int getMoveDy() {
    return moveDy;
  }

  public int getBuildDx() {
    return buildDx;
  }

  public int getBuildDy() {
    return buildDy;
  }
}