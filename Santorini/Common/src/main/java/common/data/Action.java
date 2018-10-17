package common.data;

/**
 * An Action is an interface that represents all possible actions
 * performable in a game of Santorini.
 */
public class Action {

    private final ActionType type;
    private final String workerId;
    private final Direction direction;

    public Action(ActionType type, String workerId, Direction direction) {
        this.type = type;
        this.workerId = workerId;
        this.direction = direction;
    }

    public ActionType getType() {
        return type;
    }

    public String getWorkerId() {
        return workerId;
    }

    public Direction getDirection() {
        return direction;
    }
}