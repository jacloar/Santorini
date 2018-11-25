package server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.interfaces.IPlayer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.junit.Test;
import server.RemotePlayer;

public class RemotePlayerTest {

  private static ObjectMapper mapper = new ObjectMapper();

  private Socket mockClient = getMockClient();

  private Socket getMockClient() {
    Socket client = mock(Socket.class);
    InputStream in = mock(InputStream.class);
    OutputStream out = mock(OutputStream.class);

    try {
      when(client.getInputStream()).thenReturn(in);
      when(client.getOutputStream()).thenReturn(out);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return client;
  }

  /**
   * Tests that getPlayerName returns the appropriate name of this player
   */
  @Test
  public void testGetPlayerName() throws IOException {
    String playerName = "name";
    String jsonName = mapper.writeValueAsString(playerName);

    when(mockClient.getInputStream()).thenReturn(new ByteArrayInputStream(jsonName.getBytes()));

    IPlayer remote = new RemotePlayer(mockClient);

    assertThat(remote.getPlayerName()).isEqualTo(playerName);
  }

  /**
   * Tests that setPlayerName sends the appropriate message to the player
   */
  @Test
  public void testSetPlayerName() throws IOException {
    String newName = "newname";

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    when(mockClient.getOutputStream()).thenReturn(stream);

    IPlayer remote = new RemotePlayer(mockClient);

    remote.setPlayerName(newName);
    assertThat(stream.toString()).isEqualToIgnoringWhitespace("[\"playing-as\",\"" + newName + "\"]");
  }

  /**
   * Tests that other returns the appropriate name of a player
   */
  @Test
  public void testOther() throws IOException {
    String playerName = "name";
    String jsonName = mapper.writeValueAsString(playerName);

    when(mockClient.getInputStream()).thenReturn(new ByteArrayInputStream(jsonName.getBytes()));

    IPlayer remote = new RemotePlayer(mockClient);

    assertThat(remote.getPlayerName()).isEqualTo(playerName);
  }

}
