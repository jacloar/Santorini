package common.data;

/**
 * ActionType is an enumeration of the possible types of actions a
 * performable in a game of Santorini. The enum maps values to the strings
 * used to represent them in the testing harness and network messages
 */
public enum ActionType {
    MOVE("move"),
    BUILD("build");

    private String messageString;

    private ActionType(String messageString) {
        this.messageString = messageString;
    }

    public static ActionType from(String action) {
        switch (action) {
            case "move":
                return MOVE;
            case "+build":
                return BUILD;
            default:
                throw new IllegalArgumentException(String.format("%s is not a legal action", action));
        }
    }

    @Override
    public String toString() {
        return this.messageString;
    }
}
