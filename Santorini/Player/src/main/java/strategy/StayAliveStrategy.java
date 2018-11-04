package strategy;

import common.board.IReadonlyBoard;
import common.data.Action;
import common.data.ActionType;
import common.data.Direction;
import common.data.Worker;
import common.rules.IRulesEngine;
import common.rules.StandardSantoriniRulesEngine;
import common.utils.BoardGenerator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Stay alive strategy for a given player given that there is only 1 opponent
 */
public class StayAliveStrategy implements ITurnStrategy {

    private String currentPlayer;
    private String opponent;
    private final IRulesEngine rulesEngine;
    private final List<Direction> DIRECTIONS = Direction.getAllDirections();

    public StayAliveStrategy() {
        this.rulesEngine = new StandardSantoriniRulesEngine();
    }

    public void setPlayer(String playerName) {
        this.currentPlayer = playerName;
    }

    public void setOpponent(String opponentName) {
        this.opponent = opponentName;
    }

    /**
     * Calculates all the moves for each worker that a player has. Checks the validity of each move
     * and returns the List of moves that is valid against the board
     * @param playerName the player to analyze
     * @param board the board to look at
     * @return
     */
    @Override
    public List<List<Action>> getLegalMoves(String playerName, IReadonlyBoard board) {
        List<Worker> playerWorkers = board.getPlayerWorkers(playerName);

        List<List<Action>> results = new ArrayList<>();
        // Foreach worker calculate all the moves, and all the movebuilds it can do
        for (Worker worker : playerWorkers) {
            List<List<Action>> allMoves = this.calculateAllMoves(worker.getWorkerId());
            List<List<Action>> moves = calculateLegalMoves(allMoves, board, playerName);
            List<List<Action>> moveBuilds = calculateLegalMoveBuilds(worker.getWorkerId(), board, allMoves, playerName);

            results.addAll(moves);
            results.addAll(moveBuilds);
        }
        return results;
    }

    /**
     * Calculate all move actions regardless of legality so that they can be used to
     * determine legal move-only turns as well as legal full turns that include builds
     * @return a list of Actions objects with the relevant data
     */
    private List<List<Action>> calculateAllMoves(String workerId) {
        List<List<Action>> results = new ArrayList<>();
        for (Direction d : this.DIRECTIONS) {
            Action newAction = new Action(ActionType.MOVE, workerId, d);
            List<Action> newTurn = new ArrayList<>();
            newTurn.add(newAction);
            results.add(newTurn);
        }
        return results;
    }

    /**
     * Calculates the moves around a worker (denoted by the 8 possible new coordinates)
     * The evaluates the legality of those moves
     * @param actions the list of possible move turn actions to look over
     * @param board the board to look on
     * @param playerName the name of the player to ensure we're calculating moving the correct pieces
     * @return the list of legal turns this worker has
     */
    private List<List<Action>> calculateLegalMoves(List<List<Action>> actions, IReadonlyBoard board, String playerName) {
        return actions.stream().filter(a -> this.rulesEngine.isTurnLegal(board, a, playerName)).collect(Collectors.toList());
    }

    /**
     * Calculates the legal moveBuilds given a worker, board, and list of legal singular moves
     * @param workerId The id of the worker to look around
     * @param board the board to look on
     * @param moves the list of all moves already computed that we now want to build of of
     * @return the list of legal turns that constitute a move + build this worker can do.
     */
    private List<List<Action>> calculateLegalMoveBuilds(String workerId, IReadonlyBoard board, List<List<Action>> moves, String playerName) {
        List<List<Action>> results = new ArrayList<>();
        for (List<Action> turn : moves) {
            for (Direction d : this.DIRECTIONS) {
                List<Action> newTurn = turn.stream()
                        .map(action -> new Action(action.getType(), action.getWorkerId(), action.getDirection()))
                        .collect(Collectors.toList());
                Action newAction = new Action(ActionType.BUILD, workerId, d);
                newTurn.add(newAction);
                if (this.rulesEngine.isTurnLegal(board, newTurn, playerName)) {
                    results.add(newTurn);
                }
            }
        }
        return results;
    }

    public int score(List<Action> potentialTurn, IReadonlyBoard board, int depth) {
        if (rulesEngine.didPlayerWin(board, currentPlayer) || (depth == 0 && !potentialTurn.isEmpty())) {
            return 1;
        } else if (potentialTurn.isEmpty() || didIActuallyLose(board)) {
            return 0;
        }

        IReadonlyBoard successor = BoardGenerator.generateSuccessor(board, potentialTurn);

        List<List<Action>> opponentTurns = getLegalMoves(opponent, successor);

        // calcTree('my opponent', opponentTurns
        int nextDepth = depth - 1;
        return calcTree(opponent, opponentTurns, successor, nextDepth);
    }

    private int calcTree(String playerName, List<List<Action>> potentialActions, IReadonlyBoard board, int depth) {
        // TODO maybe have this return an int so we can minimize over it (looking ahead)
        // TODO this is important as we would return 0 if one state is a loss in the tree and that will bubble up
        // Base case of either no actions to take / loss or a win
        if ((playerName.equals(currentPlayer) && potentialActions.isEmpty())) {
            return 0;
        } else if (rulesEngine.didPlayerWin(board, currentPlayer)) {
            return 1;
        }

        Map<List<Action>, Integer> turnsToScore = new HashMap<>();

        for (List<Action> turn : potentialActions) {
            IReadonlyBoard nextState = BoardGenerator.generateSuccessor(board, turn);
            if (rulesEngine.didPlayerWin(nextState, opponent)) {
                return 0;
            }
            String nextName = playerName.equals(currentPlayer) ? opponent : currentPlayer;

            List<List<Action>> possiblNextTurns = getLegalMoves(nextName, nextState);
            int nextScores;
            if (depth == 0) {
                nextScores = 1;
            } else {
                int nextDepth = depth - 1;
                nextScores = calcTree(nextName, possiblNextTurns, nextState, nextDepth);
            }
            if (nextScores == 0) {
                return nextScores;
            } else {
                turnsToScore.put(turn, nextScores);
            }
        }
        int result = Collections.min(turnsToScore.values());
        return result;
    }

    private boolean didIActuallyLose(IReadonlyBoard board) {
        return rulesEngine.didPlayerLose(board, currentPlayer) || rulesEngine.didPlayerWin(board, opponent);
    }
}
