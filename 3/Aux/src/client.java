import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.HashMap;

// row = y, col = x
public class client {

  private static ObjectMapper mapper = new ObjectMapper();

  // Names of our spreadsheets
  private static HashMap<String, Spreadsheet> sheets = new HashMap<>();

  public static void main(String[] args) throws IOException {
    JsonParser parser = new JsonFactory().createParser(System.in);

    while (!parser.isClosed()) {
      ArrayNode node = mapper.readTree(parser);

      String type = node.get(0).toString();

      try {
        switch(type) {
          case "sheet":
            sheet(node);
            break;
          case "set":
            set(node);
            break;
          case "at":
            at(node);
            break;
          default:
            // do nothing
        }
      } catch (Exception e) {
        continue;
      }
    }
  }

  /**
   * Creates a named spreadsheet with formulas placed according to the given JSON
   * @param node The JSON passed in from stdin containing the command, name and formulas
   */
  private static void sheet(ArrayNode node) {
    String name = node.get(1).asText();

    ArrayNode grid = (ArrayNode) node.get(2);
    int rows = grid.size();

    if (rows == 0) {
      sheets.put(name, Spreadsheet.buildSpreadsheet(new Formula[0][0]));
      return;
    }

    int cols = grid.get(0).size();

    Formula[][] formulas = new Formula[rows][cols];
    for (int i = 0; i < rows; i += 1) {
      for (int j = 0; j < cols; j += 1) {
        formulas[i][j] = formulaParser(grid.get(i).get(j));
      }
    }

    sheets.put(name, Spreadsheet.buildSpreadsheet(formulas));
  }

  /**
   * Sets the formula at the given x/y location
   * @param node The JSON containing the command, name of
   *        the spreadsheet, x/y location and new formula
   */
  private static void set(ArrayNode node) {
    Formula newFormula = formulaParser(node.get(4));
    Spreadsheet sheet = sheets.get(node.get(1).asText());

    int row = node.get(3).asInt();
    int col = node.get(2).asInt();

    sheet.addFormula(newFormula, row, col);
  }

  private static void at(ArrayNode node) {
    Spreadsheet sheet = sheets.get(node.get(1).asText());

    int row = node.get(3).asInt();
    int col = node.get(2).asInt();

    try {
      double value = sheet.findValue(row, col);
      System.out.println(value);
    } catch (Exception e) {
      System.out.println("false");
    }
  }

  /**
   * Parses the given JsonNode into a Formula. Assumes it is given a valid JF
   * @param node Json to parse
   * @return Formula the Json represents
   */
  private static Formula formulaParser(JsonNode node) {
    if (node.isNumber()) {
      return new NumberFormula(node.asDouble());
    }

    ArrayNode arrayNode = (ArrayNode) node;
    if (node.size() != 3) {
      throw new IllegalArgumentException("Not well formed Json. Expecting array of size 3");
    }

    if (arrayNode.get(0).isTextual() && arrayNode.get(0).asText().equals(">")) {
      return new CellPositionFormula(arrayNode.get(1).asInt(), arrayNode.get(2).asInt());
    }

    Formula left = formulaParser(arrayNode.get(0));
    Formula right = formulaParser(arrayNode.get(2));

    if (arrayNode.get(1).asText().equals("+")) {
      return new AdditionFormula(left, right);
    } else {
      return new MultiplicationFormula(left, right);
    }
  }
}


/*
Mocks out our specifications
 */
interface Formula {
  int evaluate();
}

class NumberFormula implements Formula {

  NumberFormula(double number) {

  }

  @Override
  public int evaluate() {
    return 0;
  }
}

class CellPositionFormula implements Formula {

  CellPositionFormula(int row, int col) {

  }

  @Override
  public int evaluate() {
    return 0;
  }
}

class AdditionFormula implements Formula {

  AdditionFormula(Formula formula1, Formula formula2) {

  }

  @Override
  public int evaluate() {
    return 0;
  }
}

class MultiplicationFormula implements Formula {

  MultiplicationFormula(Formula formula1, Formula formula2) {

  }

  @Override
  public int evaluate() {
    return 0;
  }
}

interface Spreadsheet {
  static Spreadsheet buildSpreadsheet(Formula[][] formulas) {
    return null;
  }

  int findValue(int row, int col);

  void addFormula(Formula formula, int row, int col);
}