package Player;

import java.util.List;

import Common.Posn;

/**
 * Implements a placement strategy that places workers along the first free space along the diagonal
 * starting from (0,0).
 */
public class DiagonalPlacementStrategy implements IPlacementStrategy {

  @Override
  public Posn placeWorker(int size, List<Posn> workersOnBoard, List<Posn> myWorkers) {
    for (int i = 0; i < size; i++) {
      for(Posn p : workersOnBoard) {
        if(!(p.getRow() == i && p.getCol() == i)) {
          return new Posn(i, i);
        }
      }
    }
    throw new IllegalArgumentException("There are no open spaces along the diagonal of the board");
  }

}
