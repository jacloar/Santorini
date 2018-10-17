package lib.utils;

import common.board.BuildingWorker;
import common.board.Height;
import common.board.ICell;
import com.fasterxml.jackson.databind.JsonNode;

public class BoardParser {

    public static ICell[][] parse(JsonNode node) {
        ICell[][] resultCells = buildDefaultCells();

        for (int row = 0; row < node.size(); row++) {
            JsonNode curRow = node.get(row);
            for (int column = 0; column < curRow.size(); column++) {
                JsonNode curCell = curRow.get(column);
                resultCells[row][column] = handleICell(curCell, row, column);

            }
        }

        return resultCells;
    }

    private static ICell[][] buildDefaultCells() {
        ICell[][] cells = new ICell[6][6];

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                cells[i][j] = new Height(0);
            }
        }

        return cells;
    }

    private static ICell handleICell(JsonNode input, int row, int column) {
        if (input.isInt()) {
            int height = input.asInt();
            return new Height(height);
        } else {
            String content = input.textValue();
            int height = Integer.parseInt(content.substring(0, 1));
            String workerId = content.substring(1);
            int len = workerId.length();
            String playerName = workerId.substring(0, len - 1);
            int workerNumber = Integer.parseInt(workerId.substring(len - 1));
            return new BuildingWorker(playerName, workerNumber, height);
        }
    }
}
