package strategy;

import common.board.IReadonlyBoard;
import common.data.Action;

import java.util.List;

public interface ITurnStrategy {
    /**
     * Generates for a player all the legal moves it can make this turn
     * @param playerName the player to analyze
     * @param board the board to look at
     * @return all the legal moves that player can make
     */
    List<List<Action>> getLegalMoves(String playerName, IReadonlyBoard board);

    /**
     * Scores the potential action to take.
     * @param potentialTurn the possible turn
     * @param board the readonly board
     * @param depth the depth to look ahead to
     * @return the integer score of the turn
     */
    int score(List<Action> potentialTurn, IReadonlyBoard board, int depth);
}
