package client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.ConnectException;
import org.junit.Test;

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

    new Thread(() -> {
      try {
        startClientWithConfig(config);
      } catch (IOException e) {
        // do nothing
      }
    }).start();
  }

  /**
   * Start a client with the given config
   *
   * @param config config to start client with
   * @throws IOException if something goes wrong
   */
  private void startClientWithConfig(String config) throws IOException {
    StringReader reader = new StringReader(config);

    Client client = new Client(reader);
    client.readConfig();
  }

  /**
   * Tests that given invalid json config throws right error
   */
  @Test
  public void testInvalidJsonConfig() {
    String config = "{";

    assertThatThrownBy(() -> startClientWithConfig(config)).isInstanceOf(IllegalArgumentException.class)
                                                           .hasMessage("Must be given valid Json");
  }

  /**
   * Tests that given json that is not an object throws right error.
   */
  @Test
  public void testNotObjectConfig() {
    String config = "[]";

    assertThatThrownBy(() -> startClientWithConfig(config)).isInstanceOf(IllegalArgumentException.class)
                                                           .hasMessage("Invalid Json, must be an object with \"players\", \"observers\", \"ip\", and \"port\"");
  }

  /**
   * Tests that if the given json object is missing a field it throws the right error
   */
  @Test
  public void testConfigMissingField() {
    String config = "{\"players\": [], \"observers\": [], \"port\": 55556}";

    assertThatThrownBy(() -> startClientWithConfig(config)).isInstanceOf(IllegalArgumentException.class)
                                                           .hasMessage("Invalid Json, must be an object with \"players\", \"observers\", \"ip\", and \"port\"");
  }

  /**
   * Tests that if the players array is bad it throws the right error
   */
  @Test
  public void testInvalidPlayersArrayTooShort() {
    String config = "{\"players\": [[\"good\", \"one\"]], \"observers\": [], \"ip\": \"127.0.0.1\", \"port\": 55556}";

    assertThatThrownBy(() -> startClientWithConfig(config)).isInstanceOf(IllegalArgumentException.class)
                                                           .hasMessage("Every element in \"players\" must be an array of strings of size 3");
  }

  /**
   * Tests that if the players array is bad it throws the right error
   */
  @Test
  public void testInvalidPlayersArrayWithNum() {
    String config = "{\"players\": [[\"good\", 4, \"one\"]], \"observers\": [], \"ip\": \"127.0.0.1\", \"port\": 55556}";

    assertThatThrownBy(() -> startClientWithConfig(config)).isInstanceOf(IllegalArgumentException.class)
                                                           .hasMessage("Every element in \"players\" must be an array of strings of size 3");
  }

  /**
   * Tests that if the players array contains an invalid path it throws the right error.
   */
  @Test
  public void testInvalidPlayersArrayBadPath() {
    String config = "{\"players\": [[\"good\", \"one\", \"path\"]], \"observers\": [], \"ip\": \"127.0.0.1\", \"port\": 55556}";

    assertThatThrownBy(() -> startClientWithConfig(config)).isInstanceOf(IllegalArgumentException.class)
                                                           .hasMessage("A player has invalid class path");
  }

  /**
   * Tests that if the observers contains an array that it too short it throws the right error
   */
  @Test
  public void testInvalidObserversArrayTooShort() {
    String config = "{\"players\": [], \"observers\": [[\"name\"]], \"ip\": \"127.0.0.1\", \"port\": 55556}";

    assertThatThrownBy(() -> startClientWithConfig(config)).isInstanceOf(IllegalArgumentException.class)
                                                           .hasMessage("Every element in \"observers\" must be an array of strings of size 2");
  }

  /**
   * Tests that if the observers contains an array with a number it throws the right error
   */
  @Test
  public void testInvalidObserversArrayWithNum() {
    String config = "{\"players\": [], \"observers\": [[\"name\", 5]], \"ip\": \"127.0.0.1\", \"port\": 55556}";

    assertThatThrownBy(() -> startClientWithConfig(config)).isInstanceOf(IllegalArgumentException.class)
                                                           .hasMessage("Every element in \"observers\" must be an array of strings of size 2");
  }

  /**
   * Tests that if the observers contains an invalid path it throws the right error
   */
  @Test
  public void testInvalidObserversArrayBadPath() {
    String config = "{\"players\": [], \"observers\": [[\"name\", \"path\"]], \"ip\": \"127.0.0.1\", \"port\": 55556}";

    assertThatThrownBy(() -> startClientWithConfig(config)).isInstanceOf(IllegalArgumentException.class)
                                                           .hasMessage("An observer has invalid class path");
  }

  /**
   * Tests that if the port is not between 50000 and 60000 it throws the right error
   */
  @Test
  public void testInvalidPort() {
    String config = "{\"players\": [], \"observers\": [], \"ip\": \"127.0.0.1\", \"port\": 8000}";

    assertThatThrownBy(() -> startClientWithConfig(config)).isInstanceOf(IllegalArgumentException.class)
                                                           .hasMessage("\"port\" must be between 50000 and 60000");
  }
}
