/**
 * Interface for the strategy class. Makes decisions about player moves
 * based on the current state of the board.
 */
public interface IStrategy {
  // The strategy knows the dimensions of the board
  // The strategy knows the target height to win the game
  // The strategy knows the maximum height for a building.

  /**
   * Determines where to place a new worker based on the current state
   * of the board.
   * @param board The current state of the board. This should be a copy.
   * @return Posn to place the new worker at a valid location on the board.
   */
  Posn placeWorker(IBoard board);


  /**
   * Determines which worker to move, where to move them, and where to build (if not at
   * target height already).
   * @param board The current state of the board.
   * @return an instance of the Move class.
   */
  Move makeMove(IBoard board);


}

/* DATA DEFINITIONS */

/**
 * This is a data definition for a move. A move always has a worker. A move occurs in
 * two steps. First the given worker moves int moveDx and int moveDy [-1,1]. Then
 * from THAT location the worker may build to an adjacent building specified by buildDx
 * and buildDy [-1,1].
 */
class Move {

  Worker worker;
  int moveDx;
  int moveDy;
  int buildDx;
  int buildDy;

}
