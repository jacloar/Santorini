package common.board;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;

/**
 * Represents an common.board.ICell in a Santorini game common.board with a worker on it
 */
public class BuildingWorker implements ICell {
    // the height of the building at this common.board.ICell
    private int height;

    // the id of the worker at this common.board.ICell
    private String playerName;

    // the number worker this is, i.e. 0 for first worker that belongs to a player
    private int workerNumber;

    // get all fields during construction
    public BuildingWorker(String playerName, int workerNumber, int height) {
        this.playerName = playerName;
        this.height = height;
        this.workerNumber = workerNumber;
    }

    // get the height at this common.board.ICell
    public int getHeight() {
        return this.height;
    }

    // get the playerName at this common.board.ICell
    public String getPlayerName() {
        return this.playerName;
    }

    // get the number worker this is
    public int getWorkerNumber() {
        return this.workerNumber;
    }

    @Override
    public ICell copy() {
        return new BuildingWorker(playerName, workerNumber, height);
    }

    // return whether there is a worker there
    public boolean isWorker() {
        return true;
    }

    @Override
    public String toString() {
        return height + playerName + workerNumber;
    }

    @Override
    public void appendSelf(Appendable app) throws IOException {
        app.append("\"");
        app.append(this.toString());
        app.append("\"");
    }

    @Override
    public JsonNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode temp = mapper.createArrayNode();
        String buildingWorker = height + playerName + workerNumber;
        temp.add(buildingWorker);
        return temp.get(0);
    }
}
