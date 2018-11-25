import client.Client;
import java.io.IOException;

public class xclients {

  public static void main(String[] args) throws IOException {
    Client client = new Client();
    client.readConfig();
  }
}
