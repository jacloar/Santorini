import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

  private static ObjectMapper mapper = new ObjectMapper();


  public static void main(String[] args) throws IOException {
    InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    int portNumber = 8000;
    Socket socket = new Socket(InetAddress.getLocalHost(), portNumber);
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    BufferedReader stdin = new BufferedReader(inputStreamReader);
    InputStreamReader inputStreamReader2 = new InputStreamReader(socket.getInputStream());
    BufferedReader in = new BufferedReader(inputStreamReader2);
    parseInput(inputStreamReader, out, stdin, in);
  }

  private static void parseInput(Reader reader, PrintWriter out, BufferedReader stdin, BufferedReader in) throws IOException {
    JsonParser parser = new JsonFactory().createParser(reader);
    StringBuilder commands = new StringBuilder();
    while (!parser.isClosed()) {
      ArrayNode node = mapper.readTree(parser);
      if(node != null) {
        System.out.println(node.toString());
        commands.append(node.toString());
        if (node.size() > 0) {
          String type = node.get(0).asText();
          if ("at".equals(type) && node.size() == 4 &&
                  node.get(1).isTextual() &&
                  node.get(2).isNumber() &&
                  node.get(3).isNumber()) {
            sendCommands(commands, out, stdin, in);
            commands = new StringBuilder();
          }
        }
      }
    }
  }

  private static void sendCommands(StringBuilder commands, PrintWriter out, BufferedReader stdin, BufferedReader in) throws IOException {
    out.println(commands.toString());
    String returnText;
    returnText = in.readLine();
    System.out.println(returnText);
  }

}

