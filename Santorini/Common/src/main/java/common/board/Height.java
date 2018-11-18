package common.board;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;

/**
 * Represents an common.board.ICell in a Santorini game common.board that has no worker
 */
public class Height implements ICell {
    // the height of the building at this common.board.ICell
    private int height;

    // get the height during construction
    public Height(int height) {
        this.height = height;
    }

    // get the height at this common.board.ICell
    public int getHeight() {
        return this.height;
    }

    // return whether there is a worker there
    public boolean isWorker() {
        return false;
    }

    /**
     * These should never be called in this class as callers will check isWorker
     * before calling them
     */

    // get the workerId at this common.board.ICell.
    public String getPlayerName() {
        return null;
    }

    // get the number worker this is
    public int getWorkerNumber() {
        return -1;
    }

    @Override
    public ICell copy() {
        return new Height(height);
    }

    @Override
    public void appendSelf(Appendable app) throws IOException {
        app.append(this.toString());
    }

    @Override
    public String toString() {
        return "" + height;
    }

    @Override
    public JsonNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode temp = mapper.createArrayNode();
        temp.add(height);
        return temp.get(0);
    }
}
