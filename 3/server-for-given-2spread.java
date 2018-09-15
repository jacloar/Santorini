
/**
 * Formula interface. All formulas implement "evaluate" method.
 */
interface Formula {
  double evaluate();
}

/**
 * A number formula only contains a number.
 */
class NumberFormula implements Formula {
  private double value;

  NumberFormula(double value) {
    this.value = value;
  }

  public double evaluate() {
    return value;
  }
}

/**
 * A reference formula contains a reference to another formula.
 */
class ReferenceFormula implements Formula {
  private Formula formula;

  ReferenceFormula(Formula formula) {
    this.formula = formula;
  }

  public double evaluate() {
    return formula.evaluate();
  }
}

/**
 * Addition formula contains two formulas to be added.
 */
class AdditionFormula implements Formula {
  private Formula formula1;
  private Formula formula2;

  AdditionFormula(Formula formula1, Formula formula2) {
    this.formula1 = formula1;
    this.formula2 = formula2;
  }

  public double evaluate() {
    return formula1.evaluate() + formula2.evaluate();
  }
}

/**
 * Multiplication formula contains two formulas to be multiplied.
 */
class MultiplicationFormula implements Formula {
  private Formula formula1;
  private Formula formula2;

  MultiplicationFormula(Formula formula1, Formula formula2) {
    this.formula1 = formula1;
    this.formula2 = formula2;
  }

  public double evaluate() {
    return formula1.evaluate() * formula2.evaluate();
  }
}

/**
 * Spreadsheet class to represent a spreadsheet.
 */
class Spreadsheet{
  private Formula[][] grid;

  /**
   * Creates a spreadsheet based on the given grid of formulas
   * @param grid grid of formulas
   */
  Spreadsheet(Formula[][] grid) {
    this.grid = grid;
  }

  /**
   * Returns the value of the formula at the given row/column
   * @param row row of the formula
   * @param col column of the formula
   * @return value of the specified formula
   */
  double getValue(int row, int col) {
    return grid[row][col].evaluate();
  }

  /**
   * Places a new formula at the given row/column
   * @param formula formula to add to the grid
   * @param row row of the new formula
   * @param col column of the new formula
   */
  void place(Formula formula, int row, int col) {
    grid[row][col] = formula;
  }
}
