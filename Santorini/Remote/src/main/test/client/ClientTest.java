package client;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.ConnectException;
import org.junit.Test;
import server.Server;

public class ClientTest {

  /**
   * Test that the client cannot connect without a server running
   * @throws IOException
   */
  @Test(expected = ConnectException.class)
  public void testClient() throws IOException {
    String config = "{ \"players\" : [[\"good\", \"one\", \"file://Santorini/Player/src/main/java/player/AIPlayer.java\"],\n"
        + "[\"good\", \"two\", \"file://Santorini/Player/src/main/java/player/AIPlayer.java\"]],\n"
        + "\"observers\" : [],\n"
        + "\"ip\": \"127.0.0.1\",\n"
        + "\"port\": 55556\n"
        + "}";
    StringReader stringReader = new StringReader(config);

    Client client = new Client(stringReader);
    client.readConfig();
  }

  /**
   * Tests that the observer observes a tournament
   */
  @Test
  public void testObserverObserves() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    System.setOut(printStream);

    assertThat(outputStream.toString()).isEmpty();

    Server.serverHelper(2, 8000, 3, 0);
    this.startClientWithObserver();

    assertThat(outputStream.toString()).isNotEmpty();
  }

  private void startClientWithObserver() {
    String config = "{ \"players\" : [[\"good\", \"one\", \"file://Santorini/Player/src/main/java/player/AIPlayer.java\"],\n"
        + "[\"good\", \"two\", \"file://Santorini/Player/src/main/java/player/AIPlayer.java\"]],\n"
        + "\"observers\" : [[\"observer\", \"file://Santorini/Observer/src/main/java/observer/StdOutObserver.java\" ]],\n"
        + "\"ip\": \"127.0.0.1\",\n"
        + "\"port\": 8000\n"
        + "}";
    StringReader stringReader = new StringReader(config);

    Client client = new Client(stringReader);

    new Thread(() -> {
      try {
        client.readConfig();
      } catch (IOException e) {
        // do nothing
      }
    }).start();
  }
}
