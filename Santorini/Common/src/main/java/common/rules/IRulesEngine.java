package common.rules;

import common.board.IReadonlyBoard;
import common.data.Action;
import common.data.PlaceWorkerAction;

import java.util.List;

/**
 * An IRulesEngine is an interface that is used to evaluate whether actions being proposed by
 * the Players are legal according to the specific implementation of the common.rules. An IRulesEngine
 * should always be consulted by an Administrator before executing an action on the common.board.
 */
public interface IRulesEngine {

    // Return whether a place worker action is legal
    boolean isPlaceWorkerLegal(IReadonlyBoard board, PlaceWorkerAction action);

    // Verify that the given common.board state represents a legal starting point for a game according to the
    // rule implementation. This provides a base case for a virtual "inductive proof of validity", in that
    // by confirming the initial state is valid, and verifying every subsequent move is legal according
    // to the common.rules, the common.board will be in a consistent state at all times
    boolean isStartingStateLegal(IReadonlyBoard board, List<String> playerNames);

    // Return whether a turn is legal. A "turn" is a list of actions representing the sequence
    // of atomic actions an IPlayer wants to make, and in the order they want to make them in. Having
    // this be a list enables the creation of alternative rule sets where the players can do more than
    // one move and one build in a turn.
    boolean isTurnLegal(IReadonlyBoard board, List<Action> actions, String activePlayer);

    // Return whether the active player has lost the game according the rule implementation. One player losing
    // is not necessarily equivalent to other players winning if multiple players are allowed by the common.rules, so
    // having this separate from didPlayerWin allows for a greater range of control by the referee over who wins,
    // who loses, and when these things happen
    boolean didPlayerLose(IReadonlyBoard board, String activePlayer);

    // Return whether the activePlayer has won the game. Win conditions can vary wildly between rule sets
    // and so this is a general assessment of whether a player won. An IReferee will handle how
    // to notify users of their wins and loses. This design assumes that for any implementable game
    // there are only two players.
    boolean didPlayerWin(IReadonlyBoard board, String activePlayer);

    // Return the number of workers each IPlayer should have according to this rule set
    int getNumberOfWorkers();
}
