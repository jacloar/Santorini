package lib.utils;

import common.board.ICell;
import com.fasterxml.jackson.databind.JsonNode;
import common.data.Action;
import common.data.ActionType;
import common.data.Direction;
import lib.testdata.Sequence;

import java.util.ArrayList;
import java.util.List;

/**
 * Convenience class used to parse sequences via the string representations
 * from Json, this parser relies on it being well-formed json as well as
 * turns that are properly inputted.
 */
public class SequenceParser {

    private static final String BUILD_OPTION = "+build";


    /**
     * method for parsing entire common.board sequences of creation of common.board, and possible
     * turns
     * @param inputs The json nodes
     * @return the list of Sequences from the json nodes
     */
    public static List<Sequence> parseSequences(List<JsonNode> inputs) {
        List<Sequence> results = new ArrayList<>();

        // Loop through all nodes
        for (int i = 0; i < inputs.size();) {
            List<JsonNode> actionNodes = new ArrayList<>();

            JsonNode creationNode = inputs.get(i);
            JsonNode moveNode = inputs.get(i + 1);
            actionNodes.add(moveNode);

            // Increment i by 2 to assume starting at next
            i += 2;

            // We may have a build command
            if (inputs.size() > i) {
                JsonNode maybeBuildNode = inputs.get(i);
                if (maybeBuildNode.get(0).asText().equals(BUILD_OPTION)) {
                    actionNodes.add(maybeBuildNode);
                    i += 1;
                }
            }

            ICell[][] cells = BoardParser.parse(creationNode);
            List<Action> actions = parseTurn(actionNodes);
            Sequence sequence = new Sequence(cells, actions);
            results.add(sequence);


        }

        return results;
    }

    public static List<Action> parseTurn(List<JsonNode> turns) {
        List<Action> results = new ArrayList<>();

        JsonNode moveAction = turns.get(0);

        String type = moveAction.get(0).asText();
        String workerId = moveAction.get(1).asText();

        String eastWest = moveAction.get(2).get(0).textValue();
        String northSouth = moveAction.get(2).get(1).textValue();

        Direction direction = new Direction(eastWest, northSouth);
        Action result = new Action(ActionType.from(type), workerId, direction);
        results.add(result);

        if (turns.size() == 2) {
            JsonNode buildAction = turns.get(1);

            String secondType = buildAction.get(0).asText();

            String secondEastWest = buildAction.get(1).get(0).textValue();
            String secondNorthSouth = buildAction.get(1).get(1).textValue();

            Direction secondDirection = new Direction(secondEastWest, secondNorthSouth);
            Action secondAction = new Action(ActionType.from(secondType), workerId, secondDirection);
            results.add(secondAction);
        }

        return  results;
    }

}
