import java.io.IOException;
import server.Server;

public class xserver {

  public static void main(String[] args) throws IOException {
    Server server = new Server();
    server.readConfig();
  }
}
