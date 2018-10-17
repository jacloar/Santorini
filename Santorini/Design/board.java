// The purpose of this file is to describe the interface for a Santorini game common.board

/**
 * IBoard
 * Serves as the interface for any implementation of a common.board
 * in the game Santorini
 */
public interface IBoard {

    // return whether an IWorker exists at the given coordinates
    boolean hasWorker(int x, int y);

    // get the IWorker at the given coordinates
    IWorker getWorker(int x, int y);

    // create an IWorker at the given coordinates and return it
    IWorker createWorker(String playerName, int x, int y);

    /** 
     * move the provided IWorker to given coordinates and return the instance
     * It is important to note that an IWorker has the concept of which player 
     * it belongs to and as such the controller/client must call IBoard::getWorker
     * to retrieve the instance being passed in here
     */
    IWorker moveWorker(IWorker worker, int x, int y);

    // adds floor to building at the given coordinates, return the new height
    int addFloor(int x, int y);

    // returns the height of the building at given coordinates, 0-indexed
    int getFloors(int x, int y);
}

/**
 * Board
 * an implementation of IBoard that represents a playing Board for Santorini
 * provides pure functionality without rule enforcement
 */
public class Board implements IBoard {

    /**
     * The common.board is represented as an ISpreadsheet where the value of an IFormula
     * in a cell represents the height of the building currently there, which can
     * be 0 for no building up to 4 for a max height building.
     * 
     *
     * This represents a departure from our provided spec in the last assignment
     * as we realized it would be less efficient than this design
     */
    private final ISpreadsheet board;

    /**
     * The workers are represented by a List or IWorker. This allows the common.board to
     * know about how many workers there may be on the common.board. This is done by each
     * IWorker knowing where they are placed, as well as who they belong to via the
     * method IWorker::getPlayerName
     */
    private final List<IWorker> workers;
}

/**
 * IWorker
 * represents a worker piece owned by a player. Each worker has knownledge
 * about where they may be placed, as well as which player they belong to
 */
public interface IWorker {
    // get the identifier of the player this IWorker belongs to
    String getPlayerName();

    // set the X coordinate of this IWorker
    void setX(int x);

    // get the X coordinate of this IWorker
    int getX();

    // set the Y coordinate of this IWorker
    void setY(int y);

    // get the Y coordinate of this IWorker
    int getY();
}

/**
 * Worker class for each worker that a player can place.
 * Each worker holds onto it's position such that it's
 * accessible to other classes when determining valid moves
 */
public class Worker implements IWorker {
    // the identifier of the player who owns this piece
    private final String playerName;

    // the X and Y coordinates of this Worker on the game common.board
    private int xCoordinate;
    private int yCoordinate;
}
