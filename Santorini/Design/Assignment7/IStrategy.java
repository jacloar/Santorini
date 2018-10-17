/**
 *  Interface for wrapping a strategy (i.e. an ai strategy using alpha beta, minimax,
 *  alpha beta pruning, or a simple random strategy)
 */
public interface IStrategy {
    /**
     * Get a "place worker" action that will place a worker on the common.board
     */
    Action getPlaceWorker(ReadableBoard b);

    /**
     * Get a full turn of actions, as defined by the implementation of the strategy
     */
    List<Action> getTurn(ReadableBoard b);
}