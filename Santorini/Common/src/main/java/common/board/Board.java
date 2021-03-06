package common.board;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import common.data.Direction;
import common.data.Worker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board implements IBoard {

    private static final int DEFAULT_ROWS = 6;
    private static final int DEFAULT_COLUMNS = 6;
    private static final int INITIAL_HEIGHT = 0;

    // ICell 2d array must be rectangular DEFAULT_ROWS x DEFAULT_COLUMNS
    private final ICell[][] cells;

    public Board() {
        this.cells = new ICell[DEFAULT_ROWS][DEFAULT_COLUMNS];

        for (int i = 0; i < DEFAULT_ROWS; i++) {
            for (int j = 0; j < DEFAULT_COLUMNS; j++) {
                this.cells[i][j] = new Height(INITIAL_HEIGHT);
            }
        }
    }

    public Board(ICell[][] cells) {
        this.cells = new ICell[DEFAULT_ROWS][DEFAULT_COLUMNS];
        for (int i = 0;  i < DEFAULT_ROWS; i += 1) {
            for (int j = 0; j < DEFAULT_COLUMNS; j += 1) {
                if (i < cells.length && j < cells[i].length) {
                    this.cells[i][j] = cells[i][j];
                } else {
                    this.cells[i][j] = new Height(INITIAL_HEIGHT);
                }
            }
        }
    }

    public Board(JsonNode board) {
        this.cells = new ICell[DEFAULT_ROWS][DEFAULT_COLUMNS];

        for (int i = 0; i < DEFAULT_ROWS; i += 1) {
            for (int j = 0; j < DEFAULT_COLUMNS; j += 1) {
                if (i < board.size() && j < board.get(i).size()) {
                    JsonNode cell = board.get(i).get(j);
                    if (cell.isInt()) {
                        cells[i][j] = new Height(cell.asInt());
                    } else {
                        String buildingWorker = cell.asText();
                        int length = buildingWorker.length();

                        int height = Integer.parseInt(buildingWorker.substring(0, 1));
                        int workerNum = Integer.parseInt(buildingWorker.substring(length - 1));
                        String playerName = buildingWorker.substring(1, length - 1);

                        cells[i][j] = new BuildingWorker(playerName, workerNum, height);
                    }
                } else {
                    cells[i][j] = new Height(INITIAL_HEIGHT);
                }
            }
        }
    }

    // Creates a worker at the given row and column
    @Override
    public BuildingWorker createWorker(String playerName, int row, int column) {
        int workerNumber = this.getNumWorkers(playerName) + 1;
        int buildingHeight = this.cells[row][column].getHeight();
        BuildingWorker newWorker = new BuildingWorker(playerName, workerNumber, buildingHeight);
        this.cells[row][column] = newWorker;
        return newWorker;
    }

    // move the worker designated by the string in the given direction
    @Override
    public void move(String worker, Direction direction) {
        Worker currentWorker = this.findWorker(worker);
        int currentRow = currentWorker.getRow();
        int currentColumn = currentWorker.getColumn();
        Height originHeight = new Height(this.cells[currentRow][currentColumn].getHeight());
        int newRow =  currentRow + direction.getRowModifier();
        int newColumn =  currentColumn + direction.getColumnModifier();
        int destinationHeight = this.cells[newRow][newColumn].getHeight();
        BuildingWorker newWorker = new BuildingWorker(currentWorker.getPlayerName(), currentWorker.getWorkerNumber(), destinationHeight);
        this.cells[newRow][newColumn] = newWorker;
        this.cells[currentWorker.getRow()][currentWorker.getColumn()] = originHeight;
    }

    // adds floor to building in the direction relative to the given worker
    @Override
    public int build(String worker, Direction direction) {
        Worker currentWorker = this.findWorker(worker);
        int newRow = currentWorker.getRow() + direction.getRowModifier();
        int newColumn = currentWorker.getColumn() + direction.getColumnModifier();
        ICell targetCell = this.cells[newRow][newColumn];
        int newHeight = targetCell.getHeight() + 1;
        Height newCell = new Height(newHeight);
        this.cells[newRow][newColumn] = newCell;
        return newHeight;
    }

    /**
     * Returns this IBoard casted to an IReadonlyBoard so it cannot be mutated.
     */
    @Override
    public IReadonlyBoard toViewModel() {
        return this;
    }

    // return whether a common.board.BuildingWorker exists at the given common.data.Direction from the
    // given worker
    @Override
    public boolean isOccupied(String worker, Direction direction) {
        Worker currentWorker = this.findWorker(worker);
        int newRow = currentWorker.getRow() + direction.getRowModifier();
        int newColumn = currentWorker.getColumn() + direction.getColumnModifier();
        ICell targetEntity = this.cells[newRow][newColumn];
        return targetEntity.isWorker();
    }

    // overloaded version of isOccupied based on absolute coordinates instead of relative directions
    @Override
    public boolean isOccupied(int row, int column) {
        return this.cells[row][column].isWorker();
    }

    // returns the height of the building in the given direction relative to
    // given worker
    @Override
    public int height(String worker, Direction direction) {
        Worker currentWorker = this.findWorker(worker);
        int newRow = currentWorker.getRow() + direction.getRowModifier();
        int newColumn = currentWorker.getColumn() + direction.getColumnModifier();
        ICell targetCell = this.cells[newRow][newColumn];
        return targetCell.getHeight();
    }

    // overloaded version of height based on absolute coordinates
    @Override
    public int height(int row, int column) {
        return this.cells[row][column].getHeight();
    }

    // return whether there is a Cell in the given direction from the worker
    @Override
    public boolean isNeighbor(String worker, Direction direction) {
        Worker currentWorker = this.findWorker(worker);
        int newRow = currentWorker.getRow() + direction.getRowModifier();
        int newColumn = currentWorker.getColumn() + direction.getColumnModifier();
        boolean rowBounded = newRow >= 0 && newRow < DEFAULT_ROWS;
        boolean columnBounded = newColumn >= 0 && newColumn < DEFAULT_COLUMNS;
        return rowBounded && columnBounded;
    }

    // return whether the specified coordinates exist in the grid
    @Override
    public boolean cellExists(int row, int column) {
        return row >= 0 && row < this.cells.length && column >= 0 && column < this.cells[row].length;
    }

    // return whether a worker exists
    @Override
    public boolean hasWorker(String workerId) {
        return this.findWorker(workerId) != null;
    }

    // find the designated worker in the common.board, assumes that it exists
    @Override
    public Worker findWorker(String workerId) {
        int len = workerId.length();
        int workerNumber = Integer.parseInt(workerId.substring(len - 1));
        String playerName = workerId.substring(0, len - 1);
        for (int row = 0; row < this.cells.length; row++) {
            for (int column = 0; column < this.cells[row].length; column++) {
                ICell currentCell = this.cells[row][column];
                if (currentCell.isWorker()) {
                    if (currentCell.getPlayerName().equals(playerName) && currentCell.getWorkerNumber() == workerNumber) {
                        return new Worker(currentCell.getPlayerName(), currentCell.getWorkerNumber(), row, column);
                    }
                }
            }
        }
        return null;
    }

    // return a list of Workers belonging to the IPlayer with the given name
    @Override
    public List<Worker> getPlayerWorkers(String playerName) {
        List<Worker> workers = getPlayerWorkerMap().get(playerName);
        if(workers == null) {
            return new ArrayList<>();
        }
        return workers;
    }

    /**
     * Convenience method to obtain all the workers and their players within the board
     * @return a Map of Player Name -> List<Worker>
     */
    @Override
    public Map<String, List<Worker>> getPlayerWorkerMap() {
        Map<String, List<Worker>> resultMap = new HashMap<>();

        for (int row = 0; row < this.cells.length; row++) {
            for (int column = 0; column < this.cells[row].length; column++) {
                ICell currentCell = this.cells[row][column];
                if (currentCell.isWorker()) {
                    String playerName = currentCell.getPlayerName();
                    Worker worker = new Worker(playerName, currentCell.getWorkerNumber(), row, column);

                    if (!resultMap.containsKey(playerName)) {
                        resultMap.put(playerName, new ArrayList<>());
                    }

                    resultMap.get(playerName).add(worker);
                }
            }
        }

        return resultMap;
    }

    @Override
    public int getMaxRows() {
        return DEFAULT_ROWS;
    }

    @Override
    public int getMaxColumns() {
        return DEFAULT_COLUMNS;
    }

    /**
     * Generates a copy of this board such that it can be acted on in the context of a
     * function that can generate succesor states.
     * @return an instance of IBoard that is mutable.
     */
    @Override
    public IBoard toBoard() {
        int rows = this.cells.length;
        ICell[][] copy = new ICell[rows][];
        for (int i = 0; i < rows; i++) {
            int columns = this.cells[i].length;
            copy[i] = new ICell[columns];

            for (int j = 0; j < columns; j++) {
                copy[i][j] = this.cells[i][j].copy();
            }
        }

        return new Board(copy);
    }

    @Override
    public ICell getCell(int row, int column) {
        return cells[row][column];
    }

    @Override
    public int getNumWorkers(String playerId) {
        List<Worker> playersWorkers = this.getPlayerWorkerMap().get(playerId);

        // playersWorkers is null if nothing has been put in the player worker map
        if (playersWorkers == null) {
            return 0;
        } else {
            return playersWorkers.size();
        }
    }

    public JsonNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode board = mapper.createArrayNode();

        for (int i = 0; i < DEFAULT_ROWS; i += 1) {
            ArrayNode row = mapper.createArrayNode();
            for (int j = 0; j < DEFAULT_COLUMNS; j += 1) {
                row.add(cells[i][j].toJson());
            }
            board.add(row);
        }

        return board;
    }
}
