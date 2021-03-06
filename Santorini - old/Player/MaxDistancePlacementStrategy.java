package Player;

import java.util.ArrayList;
import java.util.List;

import Common.Posn;

/**
 * Implements a placement strategy that places workers at the farthest point away from opponents
 * workers.
 */
public class MaxDistancePlacementStrategy implements IPlacementStrategy {

  @Override
  public Posn placeWorker(int size, List<Posn> workersOnBoard, List<Posn> myWorkers) {
    if(workersOnBoard.isEmpty()) {
      return new Posn(0,0); // if there are no workers on the board start at the top left corner.
    }

    List<Posn> opponentsWorkers = new ArrayList<>(2);

    for(Posn p: workersOnBoard) {
      if(!IPlacementStrategy.isPosnInList(p, myWorkers)) {
        opponentsWorkers.add(p);
      }
    }

    double maxDistance = 0;
    Posn placement = new Posn(0,0);

    for(int row = 0; row < size; row++) {
      for(int col = 0; col < size; col++) {
        // Skip this space if it is occupied. Cannot place worker here.
        if (IPlacementStrategy.isPosnInList(new Posn(row, col), workersOnBoard)) {
          continue;
        }

        double currentDistance = 0;

        for (Posn p : opponentsWorkers) {
          currentDistance += Math.sqrt(Math.pow(col - p.getCol(), 2) + Math.pow(row - p.getRow(), 2));
        }

        if (currentDistance > maxDistance) {
          maxDistance = currentDistance;
          placement = new Posn(row, col);
        }
      }
    }
    return placement;
  }
}
