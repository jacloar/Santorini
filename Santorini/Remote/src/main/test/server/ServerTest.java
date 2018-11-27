package server;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import admin.result.GameResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ServerTest {

//
//  @Test
//  public void testServer() {
//    //?
//    Server.startServer(2, 8000, 1, 0);
//  }

  /**
   * Tests that close connections closes all the connections in a list
   * @throws IOException
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


}
