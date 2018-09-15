import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.Queue;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

public class JSONParser {

  private static ObjectMapper mapper = new ObjectMapper();

  /**
   * Main method for program. Calls the in/out methods so they read/write to STDIN/OUT
   * @param args args for method, ignored
   * @throws Exception if issue with read/write
   */
  public static void main(String[] args) throws Exception {
    startServer();

    //Queue<JsonNode> objects = getJsonNodes(System.in);

    //printJsonNodes(objects, System.out);
  }

  public static void startServer() throws InterruptedException {
    EventLoopGroup group = new NioEventLoopGroup();

    try{
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(group);
      serverBootstrap.channel(NioServerSocketChannel.class);
      serverBootstrap.localAddress(new InetSocketAddress("localhost", 8000));

      serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
        protected void initChannel(SocketChannel socketChannel) throws Exception {
          socketChannel.pipeline().addLast(new JsonServerHandler());
        }
      });
      ChannelFuture channelFuture = serverBootstrap.bind().sync();
      channelFuture.channel().closeFuture().sync();
    } catch(Exception e){
      e.printStackTrace();
    } finally {
      group.shutdownGracefully().sync();
    }

  }

  /**
   * Adds the given JsonNodes as a String to the given Appendable
   *
   * @param objects JsonNodes to add to Appendable
   * @param app Appendable to add JsonNodes to
   * @throws IOException if something goes wrong appending the Strings
   */
  public static void printJsonNodes(Queue<JsonNode> objects, Appendable app) throws IOException {
    int size = objects.size();
    for (int i = size - 1; i >= 0; i -= 1) {
      String s = String.format("[%d,%s]\n", i, objects.poll());
      app.append(s);
    }
  }

  /**
   * Interprets the InputStream to a queue of JsonNodes
   *
   * @param stream InputStream to interpret
   * @return Queue of JsonNodes represented by the InputStream
   * @throws IOException if something goes wrong processing the input stream, eg, invalid input
   */
  public static Queue<JsonNode> getJsonNodes(InputStream stream) throws IOException {
    Queue<JsonNode> objects = new LinkedList<>();
    JsonParser parser = new JsonFactory().createParser(stream);

    while (!parser.isClosed()) {
      JsonNode node = mapper.readTree(parser);

      if (node != null) {
        objects.offer(node);
      }
    }

    return objects;
  }
}

class JsonServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf inBuffer = (ByteBuf) msg;

    ByteBufInputStream bufInStream = new ByteBufInputStream(inBuffer, true);
    Queue<JsonNode> nodes = JSONParser.getJsonNodes(bufInStream);
    JSONParser.printJsonNodes(nodes, System.out);
  }

//  @Override
//  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
//            .addListener(ChannelFutureListener.CLOSE);
//  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}
