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

    private final int depth;

    public StayAliveStrategy(int depth) {
        this.rulesEngine = new StandardSantoriniRulesEngine();
        this.depth = depth;
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
    public List<List<Action>> getLegalMoves(String playerName, IReadonlyBoard board) {
        List<Worker> playerWorkers = board.getPlayerWorkers(playerName);

        List<List<Action>> results = new ArrayList<>();
        // Foreach worker calculate all the moves, and all the movebuilds it can do
        for (Worker worker : playerWorkers) {
            List<Action> legalMoves = this.calculateLegalMoves(board, worker.getWorkerId(), playerName);
            List<List<Action>> winningMoves = filterToWinMoves(legalMoves, board, playerName);
            List<List<Action>> moveBuilds = calculateLegalMoveBuilds(worker.getWorkerId(), board, legalMoves, playerName);

            results.addAll(winningMoves);
            results.addAll(moveBuilds);
        }
        return results;
    }

    /**
     * Calculate all move actions regardless of legality so that they can be used to
     * determine legal move-only turns as well as legal full turns that include builds
     * @return a list of Actions objects with the relevant data
     */
    private List<Action> calculateLegalMoves(IReadonlyBoard board, String workerId, String playerName) {
        List<Action> results = new ArrayList<>();
        for (Direction d : this.DIRECTIONS) {
            Action newAction = new Action(ActionType.MOVE, workerId, d);
            if (rulesEngine.isMoveLegal(board, newAction, playerName)) {
                results.add(newAction);
            }
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
    private List<List<Action>> filterToWinMoves(List<Action> actions, IReadonlyBoard board, String playerName) {
        return actions.stream()
                      .map(Collections::singletonList)
                      .filter(turn -> this.rulesEngine.isTurnLegal(board, turn, playerName))
                      .collect(Collectors.toList());
    }

    /**
     * Calculates the legal moveBuilds given a worker, board, and list of legal singular moves
     * @param workerId The id of the worker to look around
     * @param board the board to look on
     * @param moves the list of all moves already computed that we now want to build of of
     * @return the list of legal turns that constitute a move + build this worker can do.
     */
    private List<List<Action>> calculateLegalMoveBuilds(String workerId, IReadonlyBoard board, List<Action> moves, String playerName) {
        List<List<Action>> results = new ArrayList<>();
        for (Action action : moves) {
            List<Action> turn = Collections.singletonList(action);
            for (Direction d : this.DIRECTIONS) {
                List<Action> newTurn = new ArrayList<>(turn);
                Action build = new Action(ActionType.BUILD, workerId, d);
                newTurn.add(build);
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

    /**
     * Gets the turn based on the given board.
     * @param b the given board to work off of
     * @return the formulated list of actions
     */
    public List<Action> getTurn(IReadonlyBoard b) {

        List<List<Action>> posTurns = this.getLegalMoves(currentPlayer, b);

        List<Action> bestMove = posTurns.get(0);
        for (List<Action> turn : posTurns) {
            if (this.score(turn, b, depth) == 1) {
                return turn;
            }
        }

        return bestMove;
    }

    private boolean didIActuallyLose(IReadonlyBoard board) {
        return rulesEngine.didPlayerLose(board, currentPlayer) || rulesEngine.didPlayerWin(board, opponent);
    }
}
