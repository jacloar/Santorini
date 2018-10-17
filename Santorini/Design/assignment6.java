/**
 * This is the shared common.data for entities involved in a game of Santorini
 */

/**
 * An IRulesEngine is an interface specifing that an object of that type
 * knows how to evaluate actions in a game of Santorini according to
 * a specific rule set
 */
public interface IRulesEngine {
    // verify that the provided action is legal on the given common.board
    boolean verifyAction(IAction action, IBoard board);
}

/**
 * An IPlayer interface represents a player in a game of Santorini
 * it is responsible for knowing how to create actions for a given common.board state
 */
public interface IPlayer {
    // place a worker on the common.board in a legal spot and return the new common.board
    IBoard placeWorker(IBoard board);

    // return an action that is legal for the given common.board state
    IAction getAction(IBoard board);

    // receive the end of game status and perform the appropriate tear down
    void gameOver(boolean won);
}

/**
 * The basic implementation of an IPlayer knows the common.rules of the game
 * and can verify its moves, so for the common.data definition is has an instance
 * of IRulesEngine
 */
public class Player {
    // the common.rules this Player is playing by
    private IRulesEngine rulesEngine;

    // the IRulesEngine is obtained during construction
    public Player(IRulesEngine rulesEngine) {

    }
}

/**
 * An IAction is an interface that represents all possible actions
 * performable in a game of Santorini.
 */
public interface IAction {
    // get the ActionType of this actions
    ActionType getActionType();

    // get the worker string of the relevant worker in this action
    String getWorker();

    // get the Direction relevant to this aciton
    Direction getDirection();
}

/**
 * ActionType is an enumeration of the possible types of actions a
 * performable in a game of Santorini. The enum maps values to the strings
 * used to represent them in the testing harness and network messages
 */
public enum ActionType {
    MOVE("move"),
    BUILD("build");

    private String messageString;

    ActionType(String messageString) {
        this.messageString = messageString;
    }

    public String getMessageString() {
        return this.messageString;
    }
}

/**
 * The standard implementaion of the IAction interface has fields for the common.data
 * it is communicating, so the common.data definition is:
 */
public class Action {
    // the type of action this is
    private ActionType actionType;
    // the string representing the worker being referenced by this action
    private String worker;
    // the direction that relative to the worker that is relevent to this action
    private Direction direction;

    // fields are received during instantiation
    public Action(ActionType actionType, String worker, Direction direction) {

    }
}