package Player;

import Common.Posn;

/**
 * A move always has a Posn representing a worker. A move occurs in
 * two steps. First the specified worker moves moveDx, moveDy in [-1,1]. Then
 * from THAT location the worker may build to an adjacent building specified
 * by buildDx, buildDy in [-1,1].
 */
class Move {

  Posn workerPosn;

  int moveDx;
  int moveDy;

  int buildDx;
  int buildDy;

}