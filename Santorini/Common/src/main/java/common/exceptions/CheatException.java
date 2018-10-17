package common.exceptions;

public class CheatException extends RuntimeException {
    public CheatException(String attemptedMove) {
        super(String.format("Nice try cheating with move %s", attemptedMove));
    }
}
