import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test harness for the Rules class.
 */
public class xrules {

  private static ObjectMapper mapper = new ObjectMapper();
  private static Map<String, Worker> workerMap = new HashMap<>();

  public static void main(String[] args) throws Exception {

    IRules rules = new Rules();

    JsonParser parser = new JsonFactory().createParser(System.in);

    JsonNode boardNode = mapper.readTree(parser);
    IBoard board = buildBoard(boardNode);

    JsonNode move = mapper.readTree(parser);
    Worker worker = workerMap.get(move.get(1).asText());
    JsonNode directionMove = move.get(2);
    EastWest ewMove = EastWest.valueOf(directionMove.get(0).asText());
    NorthSouth nsMove = NorthSouth.valueOf(directionMove.get(1).asText());

    boolean validMove = rules.isValidWorkerMove(board, worker, nsMove.getDirection(), ewMove.getDirection());

    if (validMove) {
      move(board, worker, ewMove, nsMove);
    } else {
      System.out.println("\"no\"");
      System.exit(0);
    }

    JsonNode build;
    if (!parser.isClosed() && (build = mapper.readTree(parser)) != null) {
      JsonNode directionBuild = build.get(1);
      EastWest ewBuild = EastWest.valueOf(directionBuild.get(0).asText());
      NorthSouth nsBuild = NorthSouth.valueOf(directionBuild.get(1).asText());

      boolean validBuild = rules.isValidWorkerBuild(board, worker, nsBuild.getDirection(), ewBuild.getDirection());

      if (validBuild) {
        System.out.println("\"yes\"");
      } else {
        System.out.println("\"no\"");
      }
    } else {
      System.out.println("\"yes\"");
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
   * Builds the board for the game
   *
   * @param boardNode JsonNode describing the board
   * @returns IBoard built
   */
  private static IBoard buildBoard(JsonNode boardNode) {

    Building[][] grid = new Building[Board.gridSize][Board.gridSize];
    List<Worker> workers = new ArrayList<>(4);

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
          Worker worker = new Worker(i, j);
          workers.add(worker);
          workerMap.put(name, worker);
        }
      }
    }

    return new Board(grid, workers);
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
