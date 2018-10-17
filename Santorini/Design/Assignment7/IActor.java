/**
 * This a representation of how our player class will be able to
 * make decisions in the context of santorini. This is important as it allows us
 * to make concrete implementations for how a player will act whether it be
 * via an AI implementation, Network implementation of a remote player, or
 * a local console implementation
 */
public interface IActor {
    /**
     * This method is used to get an action that places a worker on a common.board. It has it's own method
     * since placing a worker is fundamentally different from a normal turn, which the IActor will make the
     * rest of the game
     * @param b ReadableBoard representation of the common.board to make a decision with
     * @return the specified action for placing a working at the beginning
     */
    Action getPlaceWorker(ReadableBoard b);

    /**
     * This methods gets a normal turn's worth of actions from the IActor, as defined by what each implementation of
     * IActor thinks a turn is
     * @param b ReadableBoard representation of the baord to make a decision with
     * @return List<Action> which denotes a sequence of atomic actions on the common.board making up a turn
     * of santorini
     */
    List<Action> getTurn(ReadableBoard b);

    /**
     * Method to get the name of this player from the source of input, printing a message to humans
     * or receiving a String from an AI
     * @return Gets the name of this player
     */
    String getPlayerName();
}