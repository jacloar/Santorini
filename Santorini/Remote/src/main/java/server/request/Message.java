package server.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.PrintStream;

public class Message {

  private static ObjectMapper mapper = new ObjectMapper();

  public static void playingAs(PrintStream out, String name) {
    ArrayNode message = mapper.createArrayNode();
    message.add("playing-as");
    message.add(name);

    out.println(message.toString());
  }
}
