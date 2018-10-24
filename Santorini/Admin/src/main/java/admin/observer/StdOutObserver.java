package admin.observer;

import common.board.IReadonlyBoard;
import common.data.Action;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import player.IPlayer;

public class StdOutObserver implements IObserver {

  private final Appendable app;

  public StdOutObserver() {
    this(System.out);
  }

  public StdOutObserver(Appendable app) {
    this.app = app;
  }

  @Override
  public void update(IReadonlyBoard board) {
    try {
      printBoard(board);
    } catch (IOException e) {
      throw new RuntimeException("Error printing board", e);
    }
  }

  @Override
  public void update(IReadonlyBoard board, List<Action> turn) {

  }

  @Override
  public void updateGiveUp(IPlayer player) {

  }

  @Override
  public void updateWin(IPlayer player) {

  }

  @Override
  public void updateError(String error) {

  }

  /**
   * Prints the board to Appendable.
   *
   * @param board board to print
   * @throws IOException if something goes wrong with IO
   */
  private void printBoard(IReadonlyBoard board) throws IOException {
    app.append("[");

    for (int row = 0; row < board.getMaxRows(); row += 1) {
      app.append("[");

      for (int col = 0; col < board.getMaxColumns(); col += 1) {
        board.getCell(row, col).appendSelf(app);
        appendCommaOrBrace(board, col, IReadonlyBoard::getMaxColumns);
      }

      appendCommaOrBrace(board, row, IReadonlyBoard::getMaxRows);
    }
  }

  /**
   * Prints either a comma or brace to Appendable based on the given number and the getMax function
   *
   * @param board board we are printing
   * @param index current index
   * @param getMax function that returns the max index when supplied with board
   * @throws IOException if something goes wrong with IO
   */
  private void appendCommaOrBrace(IReadonlyBoard board, int index, Function<IReadonlyBoard, Integer> getMax) throws IOException {
    if (index + 1 < getMax.apply(board)) {
      app.append(",");
    } else {
      app.append("]");
    }
  }
}
