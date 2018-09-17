import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.StreamHandler;

public class JSONParser {

  private static ObjectMapper mapper = new ObjectMapper();

  /**
   * Main method for program. Calls the in/out methods so they read/write to STDIN/OUT
   * @param args args for method, ignored
   * @throws Exception if issue with read/write
   */
  public static void main(String[] args) throws Exception {
    startServer();
  }

  public static void startServer() throws IOException {
    int portNumber = 8000;
    ServerSocket serverSocket = new ServerSocket(portNumber);
    Socket socket = serverSocket.accept();

    PrintStream oos = new PrintStream(socket.getOutputStream());

    Queue<JsonNode> nodes = getJsonNodes(new InputStreamReader(socket.getInputStream()));

    printJsonNodes(nodes, oos);
    oos.flush();
  }

  /**
   * Adds the given JsonNodes as a String to the given Appendable
   *
   * @param objects JsonNodes to add to Appendable
   * @param app Appendable to add JsonNodes to
   * @throws IOException if something goes wrong appending the Strings
   */
  public static void printJsonNodes(Queue<JsonNode> objects, Appendable app) throws IOException {
    int size = objects.size();
    for (int i = size - 1; i >= 0; i -= 1) {
      String s = String.format("[%d,%s]\n", i, objects.poll());
      app.append(s);
    }
  }

  /**
   * Interprets the InputStream to a queue of JsonNodes
   *
   * @param stream InputStream to interpret
   * @return Queue of JsonNodes represented by the InputStream
   * @throws IOException if something goes wrong processing the input stream, eg, invalid input
   */
  public static Queue<JsonNode> getJsonNodes(Reader stream) throws IOException {
    Queue<JsonNode> objects = new LinkedList<>();

    JsonParser parser = new JsonFactory().createParser(stream);
    while (!parser.isClosed()) {
      JsonNode node = mapper.readTree(parser);

      if (node != null) {
        objects.offer(node);
      }
    }
    return objects;

  }
}

