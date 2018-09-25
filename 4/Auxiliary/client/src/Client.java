import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;

public class Client {

  private static ObjectMapper mapper = new ObjectMapper();
  private static String internalName;

  public static void main(String[] args) throws IOException {
    if(args.length < 1) {
      System.out.println("must include a server to connect to");
      System.exit(1);
    }
    InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    int portNumber = 8000;
    Socket socket = new Socket(args[0], portNumber);
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    String signUpName = "\"jolo-luba\"";
    out.println(signUpName);
    internalName = in.readLine();
    parseInput(inputStreamReader, out, in);
  }

  /**
   * Parses the JSON and sends batches when "at" command is received
   * @param reader Input stream coming from system.in
   * @param out Output stream going to the remote server
   * @param in Input stream from the remote server
   * @throws IOException IOexception needs to be handled
   */
  private static void parseInput(Reader reader, PrintWriter out, BufferedReader in) throws IOException {
    JsonParser parser = new JsonFactory().createParser(reader);
    StringBuilder commands = new StringBuilder("[");
    while (!parser.isClosed()) {
      JsonNode node = mapper.readTree(parser);
      if(node != null && node.isArray()) {
        commands.append(node.toString());
        if (node.size() > 0) {
          String type = node.get(0).asText();
          if ("at".equals(type) && node.size() == 4 &&
                  node.get(1).isTextual() &&
                  node.get(2).isNumber() &&
                  node.get(3).isNumber()) {
            commands.append("]");
            sendCommands(commands, out, in);
            commands = new StringBuilder("[");
          } else {
            commands.append(",");
          }
        } else {
          commands.append(",");
        }
      }
    }
  }

  /**
   * Sends commands once "at" command has been given.
   * @param commands All the commands since the last "at" call
   * @param out Output stream going to the remote server
   * @param in Input stream from the remote server
   * @throws IOException handle IOException
   */
  private static void sendCommands(StringBuilder commands, PrintWriter out, BufferedReader in) throws IOException {
    System.out.println(commands);
    out.println(commands.toString());
    String returnText;
    returnText = in.readLine();
    if ("-666".equals(returnText)) {
      System.out.println("false");
    } else {
      System.out.println(returnText);
    }
  }

}

