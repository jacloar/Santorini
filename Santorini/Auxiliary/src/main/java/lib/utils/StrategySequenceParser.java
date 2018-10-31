package lib.utils;

import com.fasterxml.jackson.databind.JsonNode;
import common.board.ICell;
import common.data.Action;
import common.data.ActionType;
import common.data.Direction;
import lib.testdata.StrategySequence;

import java.util.ArrayList;
import java.util.List;

public class StrategySequenceParser {

    private static final String BUILD_OPTION = "+build";

    public static List<StrategySequence> parseStrategySequences(List<JsonNode> inputs) {
        List<StrategySequence> results = new ArrayList<>();

        // Loop through all nodes
        for (int i = 0; i < inputs.size();) {
            String playerNameNode = inputs.get(i).textValue();
            JsonNode boardSpecNode = inputs.get(i+1);
            int depthNode = inputs.get(i+2).asInt();

            // move to next set
            i += 3;

            List<JsonNode> actionNodes = new ArrayList<>();
            if (inputs.size() > i) {
                JsonNode maybeMove = inputs.get(i);
                if (maybeMove.get(0).asText().equals(ActionType.MOVE.toString())) {
                    actionNodes.add(maybeMove);
                    i += 1;
                    if (inputs.size() > i) {
                        JsonNode maybeBuild = inputs.get(i);
                        if (maybeBuild.get(0).asText().equals(BUILD_OPTION)) {
                            actionNodes.add(maybeBuild);
                            i += 1;
                        }
                    }
                }
            }

            ICell[][] cells = BoardParser.parse(boardSpecNode);
            List<Action> actions = new ArrayList<>();
            if (!actionNodes.isEmpty()) {
                actions = parseTurn(actionNodes);
            }
            StrategySequence strategySequence = new StrategySequence(playerNameNode, cells, depthNode, actions);
            results.add(strategySequence);
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
