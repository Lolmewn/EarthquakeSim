package nl.lolmewn.rug.quakesensor.net;

import nl.lolmewn.rug.quakecommon.net.ServerAddress;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *
 * @author Lolmewn
 */
public class Server {

    private final ServerAddress address;
    private Socket socket;

    public Server(ServerAddress address) {
        this.address = address;
    }

    public ServerAddress getAddress() {
        return address;
    }

    public Socket connect() throws IOException {
        disconnect();
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address.getAddress(), address.getPort()));
        return socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void disconnect() throws IOException {
        if (socket == null || socket.isClosed()) {
            return;
        }
        socket.close();
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

}
