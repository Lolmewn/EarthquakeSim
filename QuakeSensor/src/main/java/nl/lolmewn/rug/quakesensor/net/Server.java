package nl.lolmewn.rug.quakesensor.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Observable;
import nl.lolmewn.rug.quakecommon.net.ServerAddress;

/**
 *
 * @author Lolmewn
 */
public class Server extends Observable {

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
        this.notifyObservers(); // Notify connect
        return socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void disconnect() throws IOException {
        if (socket == null || socket.isClosed()) {
            return;
        }
        System.out.println("Closing socket to " + address.toString());
        socket.close();
        this.notifyObservers(); // Notify disconnect
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

}
