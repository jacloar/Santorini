
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
   * plus one, and the building height is less than maxHeight.
   *
   * For the move to be valid:
   * dRow must be in range [-1, 1]
   * dCol must be in range [-1, 1]
   * dRow and dCol cannot both be 0
   *
   * The worker must belong to the Player making the call and it must be that player's turn
   *
   * @param player the Player proposing this move
   * @param board IBoard for the game that is currently being played
   * @param worker Worker to possibly move
   * @param dRow change in x
   * @param dCol change in y
   * @return true if valid move, false otherwise
   */
  boolean isValidWorkerMove(Player player, IBoard board, Worker worker, int dRow, int dCol);

  /**
   * Is the proposed construction valid?
   *
   * A construction is valid if the tile is on the board, the building is
   * unoccupied, and the building height is less than maxHeight.
   *
   * The worker that is proposing a build must have just moved for it to be valid.
   *
   * For the construction to be valid:
   * dRow must be in range [-1, 1]
   * dCol must be in range [-1, 1]
   * dRow and dCol cannot both be 0
   *
   * The worker must belong to the Player making the call and it must be that player's turn
   *
   * @param player the Player proposing this build
   * @param board IBoard for the game that is currently being played
   * @param worker Worker to possibly build
   * @param dRow change in row
   * @param dCol change in col
   * @return true if valid construction, false otherwise
   */
  boolean isValidWorkerBuild(Player player, IBoard board, Worker worker, int dRow, int dCol);

  /**
   * Is the proposed place worker valid?
   *
   * For the placement to be valid:
   * The specified row and col coordinates must be on the grid
   * The specified tile must be empty
   * The building at the specified position must not be greater than or equal to maxHeight
   * The player placing a worker does not alreadCol have two workers on the board.
   *
   * @param player the Player proposing this placement
   * @param row row of new worker
   * @param col col of new worker
   * @return true if valid placement, false otherwise
   */
  boolean isValidPlaceWorker(Player player, IBoard board, int row, int col);
}
