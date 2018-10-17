/**
 * This defines an actor that would work over a network and utilize the incoming
 * and outcoming streams over said network. As a result it will need to take in
 * a 'host' and a 'port' to connect over. Once it is connected it will need to pass
 * the connection's input and output streams to it's parent class
 */
public abstract class NetworkActor extends StreamActor {
    public NetworkActor(String host, int port) {
        // Connect to socket over the host and port
        Socket sock = new Socket(host, port);
        // get the input and output and feed to parent class
        super(sock.getInputStream(), sock.getOutputStream());
    }
}