package nl.lolmewn.rug.quakecommon.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Observable;
import nl.lolmewn.rug.quakecommon.net.ServerAddress;

/**
 * Class holding Server data and connection.
 *
 * @author Lolmewn
 */
public class Server extends Observable {

    private final ServerAddress address;
    private Socket socket;

    /**
     * Instantiates, but does not connect, a connection to the Server.
     *
     * @param address Address of the server
     */
    public Server(ServerAddress address) {
        this.address = address;
    }

    /**
     * Get the Address on which the server is located
     *
     * @return Address of the server
     */
    public ServerAddress getAddress() {
        return address;
    }

    /**
     * Connects to the server. Disconnects first if a connection was already
     * established.
     *
     * @see #disconnect()
     *
     * @return Created Socket
     * @throws IOException Thrown when connection failed (e.g. server is not
     * online)
     */
    public Socket connect() throws IOException {
        disconnect();
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address.getAddress(), address.getPort()));
        this.notifyObservers(); // Notify connect
        return socket;
    }

    /**
     * Gets the underlying socket of this Server. Cannot be null if connect()
     * has been called.
     *
     * @return Socket of the server, or null if no connection was made
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Disconnects the Socket from the server if it was connected. Otherwise it
     * does nothing. Notifies observers if the socket was disconnected.
     *
     * @throws IOException Thrown when disconnecting went wrong
     */
    public void disconnect() throws IOException {
        if (!isConnected()) {
            return;
        }
        System.out.println("Closing socket to " + address.toString());
        socket.close();
        this.notifyObservers(); // Notify disconnect
    }

    /**
     * Returns true if the socket is connected to a server.
     *
     * @see Socket#isConnected()
     * @see Socket#isClosed()
     * @return true if !null and isConnected() and !isClosed()
     */
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

}
