import com.fasterxml.jackson.databind.JsonNode;
import common.board.IBoard;
import common.data.Action;
import lib.testdata.StrategySequence;
import lib.utils.InputParser;
import lib.utils.StrategySequenceParser;
import strategy.StayAliveStrategy;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class XStrategy {
    public static void main(String[] args) throws IOException {
        List<JsonNode> testInputs = InputParser.parse(System.in);

        List<StrategySequence> strategySequences = StrategySequenceParser.parseStrategySequences(testInputs);

        for (StrategySequence strategySequence : strategySequences) {
            String playerName = strategySequence.getActivePlayer();
            IBoard board = strategySequence.getBoard();
            int depth = strategySequence.getDepth();
            List<Action> actions = strategySequence.getActions();

            Set<String> playerNames = board.toViewModel().getPlayerWorkerMap().keySet();
            playerNames.remove(playerName);
            String opponent = (String)playerNames.toArray()[0];

            StayAliveStrategy stayAliveStrategy = new StayAliveStrategy(playerName, opponent);
            int score = stayAliveStrategy.score(actions, board.toViewModel(), depth);

            if (score > 0) {
                System.out.println("\"yes\"");
            } else {
                System.out.println("\"no\"");
            }
        }
    }
}
