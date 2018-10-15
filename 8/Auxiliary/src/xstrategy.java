import Common.Board;
import Common.Building;
import Common.IBoard;
import Common.IRules;
import Common.Posn;
import Common.Rules;
import Common.Worker;
import Player.GameOverState;
import Player.IGameState;
import Player.InProgressState;
import Player.StayAliveMovementStrategy;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class xstrategy {

  private static ObjectMapper mapper = new ObjectMapper();
  private static Map<String, List<Worker>> workerMap = new HashMap<>();
  private static IRules rules = new Rules();

  public static void main(String[] args) throws Exception {

    StayAliveMovementStrategy strategy = new StayAliveMovementStrategy(0);

    JsonParser parser = new JsonFactory().createParser(System.in);

    // Gets the name of the player
    JsonNode playerNode = mapper.readTree(parser);
    String playerName = playerNode.asText();

    // Builds the board from the JSON input
    JsonNode boardNode = mapper.readTree(parser);
    IBoard board = buildBoard(boardNode);

    // Gets the number of rounds from the JSON input
    JsonNode roundNode = mapper.readTree(parser);
    int rounds = roundNode.asInt();

    // Determines the worker to move from JSON input
    JsonNode move;
    if (!parser.isClosed() && (move = mapper.readTree(parser)) != null) {
      String workerName = move.get(1).asText();
      int workerIdx = Integer.parseInt(workerName.substring(workerName.length() - 1)) - 1;
      Worker worker = workerMap.get(playerName).get(workerIdx);

      JsonNode directionMove = move.get(2);
      EastWest ewMove = EastWest.valueOf(directionMove.get(0).asText());
      NorthSouth nsMove = NorthSouth.valueOf(directionMove.get(1).asText());

      JsonNode build;
      if (!parser.isClosed() && (build = mapper.readTree(parser)) != null) {
        JsonNode directionBuild = build.get(1);
        EastWest ewBuild = EastWest.valueOf(directionBuild.get(0).asText());
        NorthSouth nsBuild = NorthSouth.valueOf(directionBuild.get(1).asText());

        move(board, worker, ewMove, nsMove);
        build(board, worker, ewBuild, nsBuild);

        IGameState state = boardToGameState(board, workerMap.get(playerName));

        if (strategy.isEveryStateGood(state, rounds)) {
          System.out.println("\"yes\"");
        } else {
          System.out.println("\"no\"");
        }

      } else {
        // If there is no build, move was a winning move.
        System.out.println("\"yes\"");
      }
    } else {
      IGameState state = boardToGameState(board, workerMap.get(playerName));

      if (strategy.isAnyStateGood(state, rounds)) {
        System.out.println("\"yes\"");
      } else {
        System.out.println("\"no\"");
      }
    }
  }

  /**
   * Converts the given board to a game state.
   *
   * @param board board to convert
   * @param myWorkers workers that belong to the player
   * @return IGameState representing the board
   */
  private static IGameState boardToGameState(IBoard board, List<Worker> myWorkers) {
    if (rules.isGameOver(board, myWorkers)) {
      return new GameOverState(rules.didIWin(board, myWorkers));
    }

    List<Posn> workerPosns = new ArrayList<>();
    for (Worker w : myWorkers) {
      workerPosns.add(w.getPosn());
    }

    return new InProgressState(board.getHeights(), getOpponentWorkers(board.getWorkers(), workerPosns), workerPosns);
  }

  private static List<Posn> getOpponentWorkers(List<Posn> allWorkers, List<Posn> myWorkers) {
    List<Posn> opponentWorkers = new ArrayList<>();

    for (Posn p : allWorkers) {
      if (!containsPosn(myWorkers, p)) {
        opponentWorkers.add(p);
      }
    }

    return opponentWorkers;
  }

  private static boolean containsPosn(List<Posn> list, Posn posn) {
    for (Posn p : list) {
      if (p.samePosn(posn)) {
        return true;
      }
    }

    return false;
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
   * Builds the board for the game
   *
   * @param boardNode JsonNode describing the board
   * @return IBoard built
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

          // Name is the name of the player
          String name = buildingWorker.substring(1, buildingWorker.length() - 1);
          int workerIdx = Integer.parseInt(buildingWorker.substring(buildingWorker.length() - 1)) - 1;
          Worker worker = new Worker(i, j);
          workers.add(worker);

          if (workerMap.containsKey(name)) {
            workerMap.get(name).set(workerIdx, worker);
          } else {
            ArrayList<Worker> playerWorkers = new ArrayList<>();
            playerWorkers.add(null);
            playerWorkers.add(null);

            playerWorkers.set(workerIdx, worker);
            workerMap.put(name, playerWorkers);
          }
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