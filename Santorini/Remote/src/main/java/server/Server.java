package server;

import common.interfaces.IPlayer;
import java.net.ServerSocket;
import java.net.Socket;

public class StartServer {

  public static void main(String[] args) throws Exception {
    startServer();
  }

  private static void startServer() throws Exception {
    int portNumber = 8000;
    ServerSocket serverSocket = new ServerSocket(portNumber);
    Socket socket = serverSocket.accept();

    IPlayer player = new RemotePlayer(socket);
    Thread.sleep(10000);
    System.out.println(player.getPlayerName());
  }
}
