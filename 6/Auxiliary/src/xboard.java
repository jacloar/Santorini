import Common.Board;
import Common.Building;
import Common.IBoard;
import Common.Worker;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that interprets JSON input and uses the Santorini board to make appropriate decisions
 */
public class xboard {
  private static ObjectMapper mapper = new ObjectMapper();
  private static Map<String, Worker> workerMap = new HashMap<>();

  /**
   * Main method for the xboard class
   *
   * @param args ignored
   * @throws IOException if something goes wrong reading the input
   */
  public static void main(String[] args) throws IOException {
    JsonParser parser = new JsonFactory().createParser(System.in);

    JsonNode boardNode = mapper.readTree(parser);
    IBoard board = new Board();

    buildBoard(boardNode, board);

    while (!parser.isClosed()) {
      JsonNode node = mapper.readTree(parser);

      Worker worker = workerMap.get(node.get(1).asText());
      JsonNode direction = node.get(2);
      EastWest ew = EastWest.valueOf(direction.get(0).asText());
      NorthSouth ns = NorthSouth.valueOf(direction.get(1).asText());

      switch(node.get(0).asText()) {
        case "move":
          move(board, worker, ew, ns);
          System.out.println("[]");
          break;
        case "build":
          build(board, worker, ew, ns);
          System.out.println("[]");
          break;
        case "neighbors":
          yesTrueNoFalse(neighbors(worker, ew, ns));
          break;
        case "occupied?":
          yesTrueNoFalse(isOccupied(board, worker, ew, ns));
          break;
        case "height":
          System.out.println(height(board, worker, ew, ns));
          break;
        default:
          throw new IllegalArgumentException("Invalid request");
      }
    }
  }

  /**
   * Builds the board for the game
   *
   * @param boardNode JsonNode describing the board
   * @param board IBoard to update
   */
  private static void buildBoard(JsonNode boardNode, IBoard board) {
    Building[][] grid = board.getGrid();
    for (int i = 0; i < boardNode.size(); i += 1) {
      for (int j = 0; j < boardNode.get(i).size(); j += 1) {
        JsonNode current = boardNode.get(i).get(j);
        if (current.isNumber()) {
          grid[i][j] = new Building(current.asInt());
        }
        if (current.isTextual()) {
          String buildingWorker = current.asText();
          int height = Integer.parseInt(buildingWorker.substring(0, 1));
          grid[i][j] = new Building(height);

          String name = buildingWorker.substring(1);
          Worker worker = board.placeWorker(i, j);
          workerMap.put(name, worker);
        }
      }
    }
  }

  /**
   * Prints "yes" if the condition is true, "no" if it is false
   *
   * @param condition boolean contidion
   */
  private static void yesTrueNoFalse(boolean condition) {
    if (condition) {
      System.out.println("yes");
    } else {
      System.out.println("no");
    }
  }

  /**
   * Moves the given worker on the board in the given direction.
   *
   * @param board Board the worker is on
   * @param worker Worker to move
   * @param ew EastWest direction
   * @param ns NorthSouth direction
   */
  private static void move(IBoard board, Worker worker, EastWest ew, NorthSouth ns) {
    board.workerMove(worker, ns.getDirection(), ew.getDirection());
  }

  /**
   * Constructs on the tile in the given direction relative to the given worker
   *
   * @param board Board the worker is on
   * @param worker Worker to move
   * @param ew EastWest direction
   * @param ns NorthSouth direction
   */
  private static void build(IBoard board, Worker worker, EastWest ew, NorthSouth ns) {
    board.workerBuild(worker, ns.getDirection(), ew.getDirection());
  }

  /**
   * Returns the height of the tile in the given direction relative to the given worker
   *
   * @param board Board the game is being played on
   * @param worker Worker to get for reference
   * @param ew EastWest direction
   * @param ns NorthSouth direction
   * @return the height of the specified tile
   */
  private static int height(IBoard board, Worker worker, EastWest ew, NorthSouth ns) {
    int x = worker.getX() + ns.getDirection();
    int y = worker.getY() + ew.getDirection();

    return board.getGrid()[x][y].getHeight();
  }

  /**
   * Is the specified tile on the board?
   *
   * @param worker Worker for reference
   * @param ew EastWest direction
   * @param ns NorthSouth direction
   * @return true if there is a tile, false otherwise
   */
  private static boolean neighbors(Worker worker, EastWest ew, NorthSouth ns) {
    int x = worker.getX() + ns.getDirection();
    int y = worker.getY() + ew.getDirection();

    return x >= 0 && x <= Board.gridSize && y >= 0 && y <= Board.gridSize;
  }

  /**
   * Is the specified tile occupied by a worker?
   *
   * @param board Board the game is played on
   * @param worker Worker for frame of reference
   * @param ew EastWest direction
   * @param ns NorthSouth direction
   * @return true if there is a worker, false otherwise
   */
  private static boolean isOccupied(IBoard board, Worker worker, EastWest ew, NorthSouth ns) {
    List<Worker> workers = board.getWorkers();

    int x = worker.getX() + ns.getDirection();
    int y = worker.getY() + ew.getDirection();

    for (Worker w : workers) {
      if (w.hasPosn(x, y)) {
        return true;
      }
    }

    return false;
  }
}

/**
 * Enum for the EastWest direction.
 */
enum EastWest {
  EAST(1),
  PUT(0),
  WEST(-1);

  private int direction;

  EastWest(int direction) {
    this.direction = direction;
  }

  /**
   * Returns the direction as an integer
   * @return direction
   */
  public int getDirection() {
    return direction;
  }
}

/**
 * Enum for the NorthSouth direction.
 */
enum NorthSouth {
  SOUTH(1),
  PUT(0),
  NORTH(-1);

  private int direction;

  NorthSouth(int direction) {
    this.direction = direction;
  }

  /**
   * Returns the direction as an integer
   * @return direction
   */
  public int getDirection() {
    return direction;
  }
}