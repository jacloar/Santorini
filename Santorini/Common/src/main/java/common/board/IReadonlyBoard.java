package common.board;

import com.fasterxml.jackson.databind.JsonNode;
import common.data.Direction;
import common.data.Worker;
import java.util.List;
import java.util.Map;

/**
 * Interface for a common.board that is read only
 * This is important as it becomes our ViewModel but takes
 * advantage of sharing important methods with it's write able
 * counterpart
 */
public interface IReadonlyBoard {

    boolean isOccupied(String worker, Direction direction);

    boolean isOccupied(int row, int column);

    int height(String worker, Direction direction);

    int height(int row, int column);

    boolean isNeighbor(String worker, Direction direction);

    boolean cellExists(int row, int column);

    boolean hasWorker(String workerId);

    Worker findWorker(String workerId);

    List<Worker> getPlayerWorkers(String playerName);

    Map<String, List<Worker>> getPlayerWorkerMap();

    int getMaxRows();

    int getMaxColumns();

    IBoard toBoard();

    /**
     * Returns the cell at the specified position.
     *
     * @param row row of cell
     * @param column column of cell
     * @return cell at (row,col)
     */
    ICell getCell(int row, int column);

    /**
     * Returns the number of workers on the board that belong to the player with the given id.
     *
     * @param playerId Id of player to reference
     * @return number of workers that belong to specified player
     */
    int getNumWorkers(String playerId);

    /**
     * Returns a Json object representing this board
     * @return Json object representing this board
     */
    JsonNode toJson();
}
