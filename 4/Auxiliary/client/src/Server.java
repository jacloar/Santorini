import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  public static void main(String[] args) throws IOException {
    int portNumber = 8000;
    ServerSocket serverSocket = new ServerSocket(portNumber);
    Socket socket = serverSocket.accept();
    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    BufferedReader in = new BufferedReader(inputStreamReader);
    String inputString;
    while ((inputString = in.readLine()) != null) {
      out.println(0);
    }
  }
}
