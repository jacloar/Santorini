import admin.result.GameResult;
import admin.tournament.TournamentManager;
import java.util.List;

public class xrun {

  public static void main(String[] args) {
    TournamentManager manager = new TournamentManager();
    manager.readInput();

    List<String> cheaters = manager.getCheatersNames();
    String cheatersJson = listToJsonArray(cheaters);

    List<GameResult> results = manager.getResults();
    StringBuilder resultsJson = new StringBuilder("[");
    for (int i = 0; i < results.size(); i += 1) {
      resultsJson.append("[\"");
      resultsJson.append(results.get(i).getWinner());
      resultsJson.append("\", \"");
      resultsJson.append(results.get(i).getLoser());
      resultsJson.append("\"]");

      if (i < results.size() - 1) {
        resultsJson.append(", ");
      }
    }
    resultsJson.append("]");

    String output = "[" + cheatersJson + ", " + resultsJson.toString() + "]";
    System.out.print(output);

    System.exit(0);
  }

  public static String listToJsonArray(List<String> list) {
    StringBuilder builder = new StringBuilder("[");

    for (int i = 0; i < list.size(); i += 1) {
      builder.append("\"");
      builder.append(list.get(i));
      builder.append("\"");

      if (i < list.size() - 1) {
        builder.append(", ");
      }
    }
    builder.append("]");

    return builder.toString();
  }

}
