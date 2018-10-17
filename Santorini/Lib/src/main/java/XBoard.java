import common.board.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.data.Direction;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XBoard {
    private static final String regex = "\\]\\s*\\[";
    private static final String replacement = "],[";


    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        IBoard board = new Board();

        Pattern p = Pattern.compile(regex);

        String line = "";

        boolean isInitialized = false;
        while (sc.hasNextLine()) {
            // Could have 1 or multiple commands
            line += sc.nextLine();

            if (isBalanced(line)) {
                Matcher m = p.matcher(line);

                String result = m.replaceAll(replacement);
                String commandArray = "[" + result + "]";
                List<JsonNode> commands = getJson(commandArray);

                if (!isInitialized) {
                    JsonNode boardCreation = commands.get(0);
                    board = handleBoardCreation(boardCreation);
                    commands.remove(0);
                    isInitialized = true;
                }

                for (JsonNode c : commands) {
                    handleCommand(c, board);
                }

                line = "";
            }
        }
    }

    private static boolean isBalanced(String input) {
        int openBraces = StringUtils.countMatches(input, "[");
        int closedBraces = StringUtils.countMatches(input, "]");
        return openBraces == closedBraces;
    }

    private static IBoard handleBoardCreation(JsonNode node) {
        ICell[][] cells = buildDefaultCells();

        for (int row = 0; row < node.size(); row++) {
            JsonNode curRow = node.get(row);
            for (int column = 0; column < curRow.size(); column++) {
                JsonNode curCell = curRow.get(column);
                cells[row][column] = handleICell(curCell, row, column);

            }
        }

        return new Board(cells);
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

    private static void handleCommand(JsonNode node, IBoard board) {
        String command = node.get(0).textValue();

        String worker = node.get(1).textValue();

        String eastWest = node.get(2).get(0).textValue();
        String northSouth = node.get(2).get(1).textValue();

        Direction d = new Direction(eastWest, northSouth);

        switch(command) {
            case "move":
                board.move(worker, d);
                System.out.println("[]");
                break;
            case "build":
                board.build(worker, d);
                System.out.println("[]");
                break;
            case "neighbors":
                boolean isNeighbors = board.toViewModel().isNeighbor(worker, d);
                if (isNeighbors) {
                    System.out.println("yes");
                } else {
                    System.out.println("no");
                }
                break;
            case "occupied?":
                boolean result = board.toViewModel().isOccupied(worker, d);
                if (result) {
                    System.out.println("yes");
                } else {
                    System.out.println("no");
                }
                break;
            case "height":
                int height = board.toViewModel().height(worker, d);
                System.out.println(height);
                break;
            default:
                System.out.println("Invalid command inputted");
        }
    }

    private static List<JsonNode> getJson(String maybeJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode commandList = objectMapper.readTree(maybeJson);

        List<JsonNode> results = new ArrayList<>();
        for (int i = 0; i < commandList.size(); i++) {
            results.add(commandList.get(i));
        }

        return  results;
    }
}
