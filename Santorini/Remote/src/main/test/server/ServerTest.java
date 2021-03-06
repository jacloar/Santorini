package server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import admin.result.GameResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class ServerTest {

  private Socket mockSocket(String in) throws IOException {
    Socket s = mock(Socket.class);

    ByteArrayInputStream inStream = new ByteArrayInputStream(in.getBytes());
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();

    when(s.getInputStream()).thenReturn(inStream);
    when(s.getOutputStream()).thenReturn(outStream);

    return s;
  }

  /**
   * Tests that close connections closes all the connections in a list
   */
  @Test
  public void testCloseConnections() {
    List<Socket> list = new ArrayList<>();
    Socket socket = new Socket();
    Socket socket2 = new Socket();

    list.add(socket);
    list.add(socket2);

    assertThat(!socket.isClosed()).isTrue();
    assertThat(!socket2.isClosed()).isTrue();

    Server.closeConnections(list);

    assertThat(socket.isClosed()).isTrue();
    assertThat(socket2.isClosed()).isTrue();
  }

  /**
   * Tests that inform calls inform for each player
   */
  @Test
  public void testInform() {
    RemotePlayer player = mock(RemotePlayer.class);
    RemotePlayer player1 = mock(RemotePlayer.class);

    List<RemotePlayer> plist = new ArrayList<>();

    plist.add(player);
    plist.add(player1);

    GameResult r1 = mock(GameResult.class);
    GameResult r2 = mock(GameResult.class);

    List<GameResult> rlist = new ArrayList<>();
    rlist.add(r1);
    rlist.add(r2);

    verify(player, never()).inform(any());
    verify(player1, never()).inform(any());

    Server.informResults(plist, rlist);
    verify(player, atLeastOnce()).inform(any());
    verify(player1, atLeastOnce()).inform(any());
  }


  /**
   * Tests that the server will close connections if the minimum number of players
   * is not met.
   */
  @Test
  public void testMinPlayersNotMet() throws IOException {
    Socket s1 = mock(Socket.class);
    Socket s2 = mock(Socket.class);
    List<Socket> connections = Arrays.asList(s1, s2);

    verify(s1, never()).close();
    verify(s2, never()).close();

    Server.runServer(3, connections);

    verify(s1, atLeastOnce()).close();
    verify(s2, atLeastOnce()).close();
  }

  /**
   * Tests run server with two players where they do not respond
   * @throws IOException
   */
  @Test
  public void testRunServerNoResponse() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(out);
    System.setOut(printStream);

    Socket s1 = mockSocket("\"one\"");
    Socket s2 = mockSocket("\"two\"");
    List<Socket> connections = Arrays.asList(s1, s2);

    verify(s1, never()).close();
    verify(s2, never()).close();

    Server.runServer(2, connections);

    verify(s1, atLeastOnce()).close();
    verify(s2, atLeastOnce()).close();

    assertThat(out.toString()).isEqualToIgnoringWhitespace("[[\"two\",\"one\",\"irregular\"]]");
  }


  /**
   * Tests run server with two players where one player gives up after placement
   * @throws IOException
   */
  @Test
  public void testRunServerGiveUp() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(out);
    System.setOut(printStream);

    String p1Json = "\"one\"\n"
        + "[0,0]\n"
        + "[2,2]\n"
        + "\"\"";
    String p2Json = "\"two\"\n"
        + "[1,1]\n"
        + "[3,3]\n"
        + "\"\"";

    Socket s1 = mockSocket(p1Json);
    Socket s2 = mockSocket(p2Json);
    List<Socket> connections = Arrays.asList(s1, s2);

    verify(s1, never()).close();
    verify(s2, never()).close();

    Server.runServer(2, connections);

    verify(s1, atLeastOnce()).close();
    verify(s2, atLeastOnce()).close();

    assertThat(out.toString()).isEqualToIgnoringWhitespace("[[\"two\",\"one\"],[\"two\",\"one\"],[\"two\",\"one\"]]");
  }

  /**
   * Tests run server with two players where they do not respond
   * @throws IOException
   */
  @Test
  public void testRunServerIllegalName() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(out);
    System.setOut(printStream);

    Socket s1 = mockSocket("1");
    Socket s2 = mockSocket("[\"two\"]");
    List<Socket> connections = Arrays.asList(s1, s2);

    verify(s1, never()).close();
    verify(s2, never()).close();

    Server.runServer(2, connections);

    verify(s1, atLeastOnce()).close();
    verify(s2, atLeastOnce()).close();

    assertThat(out.toString()).isEqualToIgnoringWhitespace("[]");
  }

  @Test
  public void testRunServerInvalidJson() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(out);
    System.setOut(printStream);

    Socket s1 = mockSocket("[\"");
    Socket s2 = mockSocket("two");
    List<Socket> connections = Arrays.asList(s1, s2);

    verify(s1, never()).close();
    verify(s2, never()).close();

    Server.runServer(2, connections);

    verify(s1, atLeastOnce()).close();
    verify(s2, atLeastOnce()).close();

    assertThat(out.toString()).isEqualToIgnoringWhitespace("[]");
  }

  /**
   * Start the server with the given config.
   * @param config config to start server with
   */
  private void startServerWithConfig(String config) {
    StringReader reader = new StringReader(config);

    Server server = new Server(reader);
    server.readConfig();
  }

  @Test
  public void testInvalidJsonConfig() {
    String config = "{";
    assertThatThrownBy(() -> startServerWithConfig(config)).isInstanceOf(IllegalArgumentException.class)
                                                           .hasMessage("Must be given valid Json");
  }

  @Test
  public void testNotObjectConfig() {
    String config = "[]";
    assertThatThrownBy(() -> startServerWithConfig(config)).isInstanceOf(IllegalArgumentException.class)
                                                           .hasMessage("Must be given Json object with \"min players\", \"port\", "
                                                               + "\"waiting for\", and \"repeat\". All values must be integers.");
  }

  @Test
  public void testMissingFieldConfig() {
    String config = "{\"min players\": 3, \"waiting for\": 5, \"port\": 8000}";
    assertThatThrownBy(() -> startServerWithConfig(config)).isInstanceOf(IllegalArgumentException.class)
                                                           .hasMessage("Must be given Json object with \"min players\", \"port\", "
                                                               + "\"waiting for\", and \"repeat\". All values must be integers.");
  }

  @Test
  public void testBadRepeat() {
    String config = "{\"min players\": 3, \"waiting for\": 5, \"port\": 8000, \"repeat\": 2}";
    assertThatThrownBy(() -> startServerWithConfig(config)).isInstanceOf(IllegalArgumentException.class)
                                                           .hasMessage("\"min players\" and \"port\" must be greater than or equal to 0, "
                                                               + "\"waiting for\" must be greater than 0, and \"repeat\" must be equal to 0 or 1");
  }
}
