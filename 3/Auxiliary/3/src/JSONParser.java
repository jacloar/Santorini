import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
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
import java.util.logging.StreamHandler;

public class JSONParser {

  private static ObjectMapper mapper = new ObjectMapper();

  /**
   * Main method for program. Calls the in/out methods so they read/write to STDIN/OUT
   * @param args args for method, ignored
   * @throws Exception if issue with read/write
   */
  public static void main(String[] args) throws Exception {
//    startServer();

    Queue<JsonNode> objects = getJsonNodes(new InputStreamReader(System.in));

    printJsonNodes(objects, System.out);
  }

  public static void startServer() throws IOException {
    int portNumber = 8000;
    ServerSocket serverSocket = new ServerSocket(portNumber);
    while(true){
      System.out.println("Waiting for client request");
      //creating socket and waiting for client connection
      Socket socket = serverSocket.accept();
      //read from socket to ObjectInputStream object
      BufferedReader ois = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      //convert ObjectInputStream object to String
      Queue<JsonNode> nodes = getJsonNodes(ois);
//      Queue<JsonNode> nodes = getJsonNodes(ois);
      System.out.println("Message Received: " );
      //create ObjectOutputStream object
      PrintStream oos = new PrintStream(socket.getOutputStream());

      printJsonNodes(nodes,oos);
      //write object to Socket
      oos.println("Hi Client");


      //close resources
      ois.close();
      oos.close();
      socket.close();
      //terminate the server if client sends exit request
    }
//    Socket clientSocket = serverSocket.accept();
//    PrintStream out = new PrintStream(clientSocket.getOutputStream(), true);
//    BufferedReader in  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//    out.println("ONE");
////    Queue<JsonNode> nodes = getJsonNodes(in);
//    out.println(in + "TWO");
////    printJsonNodes(nodes, out);
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

