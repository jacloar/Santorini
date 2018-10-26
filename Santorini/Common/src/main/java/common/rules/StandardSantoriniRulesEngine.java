package common.rules;

import common.board.IReadonlyBoard;
import common.data.*;
import common.utils.BoardGenerator;

import java.util.*;

/**
 * This is the implementation for the common.rules provided by this class. This is referred to as
 * "Standard Santorini", and the full spec can be found here:
 *
 * http://www.ccs.neu.edu/home/matthias/4500-f18/santorini.html
 */
public class StandardSantoriniRulesEngine implements IRulesEngine {
    // a building can have at most 4 floors
    private final int MAX_BUILDING_HEIGHT = 4;
    // each player should have 2 workers when playing by these common.rules
    private final int NUMBER_OF_PLAYER_WORKERS = 2;
    // an enumeration of legal Direction vectors to create Directions
    private final List<String> LEGAL_EAST_WEST = Arrays.asList("EAST", "PUT", "WEST");
    private final List<String> LEGAL_NORTH_SOUTH = Arrays.asList("NORTH", "PUT", "SOUTH");

    /**
     * In "Standard Santorini", a worker can only be placed in a square where there are no
     * workers already, so this checks that condition
     */
    public boolean isPlaceWorkerLegal(IReadonlyBoard board, PlaceWorkerAction action) {
        int actionRow = action.getRow();
        int actionColumn = action.getColumn();
        boolean cellExists = board.cellExists(actionRow, actionColumn);
        if (cellExists) {
            return !board.isOccupied(action.getRow(), action.getColumn());
        } else {
            return false;
        }
    }

    /**
     * For "Standard Santorini", this verifies that the common.board has to floors in any buildings yet, and
     * that there are 4 workers, 2 per each player, because this is the correct starting state.
     * Returns true if the state is valid
     */
    public boolean isStartingStateLegal(IReadonlyBoard board, List<String> playerNames) {
        for (String name : playerNames) {
            if (board.getPlayerWorkers(name).size() != this.NUMBER_OF_PLAYER_WORKERS) {
                return false;
            }
        }
        for (int row = 0; row < board.getMaxRows(); row++) {
            for (int column = 0; column < board.getMaxColumns(); column++) {
                if (board.cellExists(row, column)) {
                    if (board.height(row, column) != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    // The "Standard Santorini" common.rules specify a "turn" as a move action, followed by a build action.
    // This method uses two helper methods below to evaluate each action individually. isMoveLegal
    // confirms the worker specified in the move action belongs to the active player, and this function
    // confirms the first workerId is the same as the second, verifying the active player is using only
    // one piece, which does belong to them
    public boolean isTurnLegal(IReadonlyBoard board, List<Action> actions, String activePlayer) {
        if (actions.size() > 0) {
            Action firstAction = actions.get(0);
            boolean isMoveAction = firstAction.getType() == ActionType.MOVE;
            boolean isMoveLegal = this.isMoveLegal(board, firstAction, activePlayer);
            if (isMoveAction && isMoveLegal) {
                if (actions.size() > 1) {
                    Action secondAction = actions.get(1);
                    String firstWorkerId = firstAction.getWorkerId();
                    String secondWorkerId = secondAction.getWorkerId();
                    if (firstWorkerId.equals(secondWorkerId)) {
                        Worker worker = board.findWorker(secondWorkerId);
                        Direction moveDirection = firstAction.getDirection();
                        Direction buildDirection = secondAction.getDirection();
                        int currentWorkerRow = worker.getRow();
                        int currentWorkerColumn = worker.getColumn();
                        int movedWorkerRow = currentWorkerRow + moveDirection.getRowModifier();
                        int movedWorkerColumn = currentWorkerColumn + moveDirection.getColumnModifier();
                        if (actions.size() < 3) {
                            return this.isBuildLegal(board, movedWorkerRow, movedWorkerColumn, buildDirection, currentWorkerRow, currentWorkerColumn);
                        }
                    }
                } else {
                    List<Action> moveActionList = new ArrayList<>();
                    moveActionList.add(firstAction);
                    IReadonlyBoard newBoard = BoardGenerator.generateSuccessor(board, moveActionList);
                    return this.didPlayerWin(newBoard, activePlayer);
                }
            }
        }
        return false;
    }

    /**
     * This method verifies that an action is legal. It does this by confirming:
     * 1) The specified worker is on the common.board
     * 2) The specified worker belongs to the active player
     * 3) The space it wants to move to exists
     * 4) The space it wants to move to is not occupied
     * 5) The space it wants to move to is at most 1 floor taller than the height of where it is
     *
     * Returns true if all are confirmed, returns false otherwise
     */
    private boolean isMoveLegal(IReadonlyBoard board, Action action, String activePlayer) {
        String workerId = action.getWorkerId();
        Direction moveDirection = action.getDirection();
        // 1)
        if (board.hasWorker(workerId)) {
            Worker worker = board.findWorker(workerId);
            int currentWorkerRow = worker.getRow();
            int currentWorkerColumn = worker.getColumn();
            // 2)                                              3)
            if (worker.getPlayerName().equals(activePlayer) && board.isNeighbor(workerId, moveDirection)) {
                // 4)
                boolean notOccupied = !board.isOccupied(workerId, moveDirection);
                int movedWorkerRow = currentWorkerRow + moveDirection.getRowModifier();
                int movedWorkerColumn = currentWorkerColumn + moveDirection.getColumnModifier();
                int currentHeight = board.height(currentWorkerRow, currentWorkerColumn);
                int moveTargetHeight = board.height(movedWorkerRow, movedWorkerColumn);
                // 5)
                boolean canMove = moveTargetHeight <= currentHeight + 1;
                return notOccupied && canMove;
            }
        }
        return false;
    }

    /**
     * The method verifies that a build is legal based on the projected location of the worker after it
     * moves. It confirms:
     * 1) The target cell exists in the common.board
     * 2) The cell is not occupied by a worker
     * 3) The cell is not at the maximum height already
     *
     * It returns true if all of these are confirmed, and false if not.
     */
    private boolean isBuildLegal(IReadonlyBoard board, int movedWorkerRow,
                                 int movedWorkerColumn, Direction buildDirection,
                                 int originRow, int originColumn) {
        int buildTargetRow = movedWorkerRow + buildDirection.getRowModifier();
        int buildTargetColumn = movedWorkerColumn + buildDirection.getColumnModifier();
        // 1)
        if (board.cellExists(buildTargetRow, buildTargetColumn)) {
            // 2)
            boolean isLegal;
            boolean isOccupied = board.isOccupied(buildTargetRow, buildTargetColumn);
            if (!isOccupied) {
                isLegal = true;
            } else {
                isLegal = (originRow == buildTargetRow && originColumn == buildTargetColumn);
            }
            // 3)
            boolean canAdd = board.height(buildTargetRow, buildTargetColumn) < this.MAX_BUILDING_HEIGHT;
            return isLegal && canAdd;
        }
        return false;
    }

    /**
     * A player loses "Standard Santorini" when they have no legal moves, so this method checks to make sure
     * the active player has at least 1 legal move
     */
    public boolean didPlayerLose(IReadonlyBoard board, String activePlayer) {
        List<Worker> workers = board.getPlayerWorkers(activePlayer);
        for (Worker worker : workers) {
            String workerId = String.format("%s%s", worker.getPlayerName(), worker.getWorkerNumber());
            for (String eastWest : this.LEGAL_EAST_WEST) {
                for (String northSouth : this.LEGAL_NORTH_SOUTH) {
                    Direction tempDir = new Direction(eastWest, northSouth);
                    Action action = new Action(ActionType.MOVE, workerId, tempDir);
                    if (this.isMoveLegal(board, action, activePlayer)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * "Standard Santorini" is won when a player has a worker reach the third floor of a building, so this
     * checks to see if any worker that belongs to the active player is on the third floor of a building
     */
    public boolean didPlayerWin(IReadonlyBoard board, String activePlayer) {
        List<Worker> workers = board.getPlayerWorkers(activePlayer);
        for (Worker worker : workers) {
            int workerRow = worker.getRow();
            int workerColumn = worker.getColumn();
            if (board.height(workerRow, workerColumn) == 3) {
                return true;
            }
        }
        return false;
    }

    public int getNumberOfWorkers() {
        return this.NUMBER_OF_PLAYER_WORKERS;
    }
}
