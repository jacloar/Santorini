package Common;

import java.util.List;

/**
 * Interface for checking the rules of the game.
 */
public interface IRules {

  /**
   * Is the proposed move valid?
   *
   * A move is valid if the worker is moving to a tile on the board, the
   * tile is unoccupied by another worker, the height of the building
   * in the new space is no more than the current height of the worker
   * plus one, and the building height is 3 or less.
   *
   * For the move to be valid:
   * dRow must be in range [-1, 1]
   * dCol must be in range [-1, 1]
   * dRow and dCol cannot both be 0
   *
   * The worker must belong to the Player making the call and it must be that player's turn
   *
   * @param heights Heights for the game that is currently being played
   * @param allWorkers List of positions of all the workers on the board
   * @param workerPosn Position of worker to possibly move
   * @param dRow change in row
   * @param dCol change in col
   * @return true if valid move, false otherwise
   */
  boolean isValidWorkerMove(int[][] heights, List<Posn> allWorkers, Posn workerPosn, int dRow, int dCol);

  /**
   * Calls isValidWorkerMove
   *
   * @param board IBoard for game
   * @param worker Worker to move
   * @param dRow change in row
   * @param dCol change in col
   * @return true if valid move, false otherwise
   */
  boolean isValidWorkerMove(IBoard board, Worker worker, int dRow, int dCol);

  /**
   * Is the proposed construction valid?
   *
   * A construction is valid if the tile is on the board, the building is
   * unoccupied, and the building height is less than 4.
   *
   * The worker that is proposing a build must have just moved for it to be valid.
   * It will be impossible to give the referee a move/build request with more than
   * one worker.
   *
   * For the construction to be valid:
   * dRow must be in range [-1, 1]
   * dCol must be in range [-1, 1]
   * dRow and dCol cannot both be 0
   *
   * The worker must belong to the Player making the call and it must be that player's turn
   *
   * @param heights Heights for the game that is currently being played
   * @param allWorkers List of positions of all the workers on the board
   * @param workerPosn Position of worker to possibly build
   * @param dRow change in row
   * @param dCol change in col
   * @return true if valid construction, false otherwise
   */
  boolean isValidWorkerBuild(int[][] heights, List<Posn> allWorkers, Posn workerPosn, int dRow, int dCol);

  /**
   * Calls isValidWorkerBuild
   *
   * @param board IBoard for game
   * @param worker Worker to move
   * @param dRow change in row
   * @param dCol change in col
   * @return true if valid move, false otherwise
   */
  boolean isValidWorkerBuild(IBoard board, Worker worker, int dRow, int dCol);

  /**
   * Is the proposed place worker valid?
   *
   * For the placement to be valid:
   * The specified row and col coordinates must be on the grid
   * The specified tile must be empty
   * The building at the specified position must not be greater than 3
   *
   * @param heights Heights for the game that is currently being set up
   * @param allWorkers List of positions of all the workers on the board
   * @param row row coordinate of new worker
   * @param col col coordinate of new worker
   * @return true if valid placement, false otherwise
   */
  boolean isValidPlaceWorker(int[][] heights, List<Posn> allWorkers, int row, int col);

  /**
   * Calls isValidPlaceWorker
   *
   * @param board IBoard for game
   * @param row row of new worker
   * @param col col of new worker
   * @return true if valid placement, false otherwise
   */
  boolean isValidPlaceWorker(IBoard board, int row, int col);


  boolean isGameOver(int[][] heights, List<Posn> allWorkers, List<Posn> myWorkers);


  boolean didIWin(int[][] heights, List<Posn> allWorkers, List<Posn> myWorkers);
}
