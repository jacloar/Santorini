package admin.observer;

import common.board.IReadonlyBoard;
import common.data.Action;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import common.interfaces.IPlayer;

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
  public void update(List<Action> turn) {
    try {
      printTurn(turn);
    } catch (IOException e) {
      throw new RuntimeException("Error printing turn", e);
    }
  }

  @Override
  public void updateGiveUp(IPlayer player) {
    try {
      printPlayer(player);
    } catch (IOException e) {
      throw new RuntimeException("Error printing player", e);
    }
  }

  @Override
  public void updateWin(IPlayer player) {
    try {
      printPlayer(player);
    } catch (IOException e) {
      throw new RuntimeException("Error printing player", e);
    }
  }

  @Override
  public void updateError(String error) {
    try {
      app.append("\"");
      app.append(error);
      app.append("\"");
    } catch (IOException e) {
      throw new RuntimeException("Error printing error", e);
    }
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
    app.append("\n");
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

  /**
   * Prints the specified turn to the Appendable
   *
   * @param turn turn to print
   * @throws IOException if something goes wrong printing the turn
   */
  private void printTurn(List<Action> turn) throws IOException {
    app.append("[");

    // We know turn has size at least 1 to be considered a valid turn
    app.append("\"");
    app.append(turn.get(0).getWorkerId());
    app.append("\"");

    for (Action aTurn : turn) {
      app.append(",");
      printAction(aTurn);
    }

    app.append("]");
    app.append("\n");
  }

  /**
   * Prints the specified action to the Appendable
   *
   * @param action action to print
   * @throws IOException if something goes wrong printing the action
   */
  private void printAction(Action action) throws IOException {
    app.append("\"");
    app.append(action.getDirection().getEastWest());
    app.append("\"");
    app.append(",");
    app.append("\"");
    app.append(action.getDirection().getNorthSouth());
    app.append("\"");
  }

  /**
   * Prints the specified IPlayer to the appendable
   *
   * @param player IPlayer to print
   * @throws IOException if something goes wrong printing the player
   */
  private void printPlayer(IPlayer player) throws IOException {
    app.append("\"");
    app.append(player.getPlayerName());
    app.append("\"");
  }
}
