package server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import admin.result.GameResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.board.Board;
import common.board.IBoard;
import common.data.Action;
import common.data.PlaceWorkerAction;
import common.interfaces.IPlayer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import player.BreakerPlayer;

public class MessageTest {


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

  @Test
  public void testPlayingAs() throws IOException {
    String name = "name";

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    when(mockClient.getOutputStream()).thenReturn(stream);

    PrintStream pStream = new PrintStream(stream);

    Message.playingAs(pStream, name);
    assertThat(stream.toString()).isEqualToIgnoringWhitespace("[\"playing-as\",\"" + name + "\"]");

  }

  @Test
  public void testOther() throws IOException {
    String name = "name";

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    when(mockClient.getOutputStream()).thenReturn(stream);

    PrintStream pStream = new PrintStream(stream);

    Message.other(pStream, name);
    assertThat(stream.toString()).isEqualToIgnoringWhitespace("\"" + name + "\"");
  }

  @Test
  public void testWorkerPlacement() throws IOException {
    String player1 = "one";
    String player2 = "two";

    String placementMessage = "[1,2]";

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    when(mockClient.getOutputStream()).thenReturn(out);

    ByteArrayInputStream in = new ByteArrayInputStream(placementMessage.getBytes());
    when(mockClient.getInputStream()).thenReturn(in);

    PrintStream pStream = new PrintStream(out);

    InputStreamReader iReader = new InputStreamReader(in);


    IBoard board = new Board();
    board.createWorker(player1, 1, 1);

    PlaceWorkerAction placement = Message.workerPlacement(iReader, pStream, board, player1, player2);
    assertThat(out.toString()).isEqualToIgnoringWhitespace("[[\"one1\", 1, 1]]");
    assertThat(placement.getRow()).isEqualTo(1);
    assertThat(placement.getColumn()).isEqualTo(2);
  }

  @Test
  public void testTakeTurn() throws IOException {
    String player1 = "one";
    String player2 = "two";

    String turnMessage = "[\"one1\", \"PUT\", \"SOUTH\", \"EAST\", \"PUT\"]";

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    when(mockClient.getOutputStream()).thenReturn(out);

    ByteArrayInputStream in = new ByteArrayInputStream(turnMessage.getBytes());
    when(mockClient.getInputStream()).thenReturn(in);

    PrintStream pStream = new PrintStream(out);

    InputStreamReader iReader = new InputStreamReader(in);


    IBoard board = new Board();
    board.createWorker(player1, 1, 1);

    List<Action> turn = Message.takeTurn(iReader, pStream, board);
    assertThat(out.toString()).isEqualToIgnoringWhitespace("[[0,0,0,0,0,0]," +
            "[0,\"0one1\",0,0,0,0]," +
            "[0,0,0,0,0,0]," +
            "[0,0,0,0,0,0]," +
            "[0,0,0,0,0,0]," +
            "[0,0,0,0,0,0]] ");

    assertThat(turn.get(0).getWorkerId()).isEqualTo("\"" + player1 + "1\"");
    assertThat(turn.get(0).getDirection().getEastWest()).isEqualTo("PUT");
    assertThat(turn.get(0).getDirection().getNorthSouth()).isEqualTo("SOUTH");
    assertThat(turn.get(1).getDirection().getEastWest()).isEqualTo("EAST");
    assertThat(turn.get(1).getDirection().getNorthSouth()).isEqualTo("PUT");
  }


  @Test
  public void testInform() throws IOException {
    String player1 = "one";
    String player2 = "two";

    IPlayer p1 = new BreakerPlayer(player1);
    IPlayer p2 = new BreakerPlayer(player2);


    List<GameResult> results = new ArrayList<>();
    GameResult g1 = new GameResult(p1, p2, false);
    GameResult g2 = new GameResult(p2, p1, false);
    GameResult g3 = new GameResult(p1, p2, true);
    results.add(g1);
    results.add(g2);
    results.add(g3);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    when(mockClient.getOutputStream()).thenReturn(out);

    PrintStream pStream = new PrintStream(out);


    IBoard board = new Board();
    board.createWorker(player1, 1, 1);

    Message.inform(pStream, results);

    assertThat(out.toString()).isEqualToIgnoringWhitespace("[[\"one\",\"two\"],[\"two\",\"one\"]," +
            "[\"one\",\"two\",\"irregular\"]]");

  }


}
