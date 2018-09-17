import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.io.StringReader;
import java.util.Queue;
import org.junit.Test;

public class JSONParserTest {

  /*
  Tests that the parser works with the given example on the assignment
   */
  @Test
  public void testBasicInput() throws IOException {
    StringReader input = new StringReader("[1,2]\n{\"a\":1}");

    Queue<JsonNode> nodes = JSONParser.getJsonNodes(input);

    StringBuilder output = new StringBuilder();
    JSONParser.printJsonNodes(nodes, output);

    assertEquals("[1,[1,2]]\n[0,{\"a\":1}]\n", output.toString());
  }

  /*
  Tests that the parser ignores any blank lines in the input
   */
  @Test
  public void testBlankLines() throws IOException {
    StringReader input = new StringReader("[\n1\n\n   ]");

    Queue<JsonNode> nodes = JSONParser.getJsonNodes(input);

    StringBuilder output = new StringBuilder();
    JSONParser.printJsonNodes(nodes, output);

    assertEquals("[0,[1]]\n", output.toString());
  }

  /*
  Tests that the parser does not print anything when given no input
   */
  @Test
  public void testEmptyInput() throws IOException {
    StringReader input = new StringReader("");

    Queue<JsonNode> nodes = JSONParser.getJsonNodes(input);

    StringBuilder output = new StringBuilder();
    JSONParser.printJsonNodes(nodes, output);

    assertEquals("", output.toString());
  }

  /*
  Tests that the parser throws an exception if given input that is not valid JSON
   */
  @Test(expected = com.fasterxml.jackson.core.JsonParseException.class)
  public void testException() throws IOException {
    StringReader input = new StringReader(",");

    JSONParser.getJsonNodes(input);
  }
}
