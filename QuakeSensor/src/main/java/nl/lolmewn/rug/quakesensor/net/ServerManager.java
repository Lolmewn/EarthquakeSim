package nl.lolmewn.rug.quakesensor.net;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import nl.lolmewn.rug.quakecommon.net.ServerAddress;

/**
 *
 * @author Lolmewn
 */
public class ServerManager {

    private final List<Server> servers = new ArrayList<>();

    /**
     * Loads the connection data and connects to the endpoint
     *
     * @param address Address to connect to
     * @throws IOException Thrown when the socket could not connect to the
     * endpoint
     */
    public void load(ServerAddress address) throws IOException {
        Server server = new Server(address);
        this.servers.add(server);
        server.connect();
    }

    public boolean checkAvailability(String IP, int port) {
        try (Socket s = new Socket(IP, port)) {
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    public List<Server> getServers() {
        return servers;
    }

    public Server getActiveServer() {
        for (Server server : this.getServers()) {
            if (server.isConnected()) {
                return server;
            }
        }
        // No active sockets; let's check for active servers.
        System.out.println("Checking for active servers...");
        for (Server server : this.getServers()) {
            try {
                System.out.print("Connecting to " + server.toString() + "... ");
                server.connect();
                if (server.isConnected()) {
                    System.out.println("online.");
                    return server;
                }
            } catch (IOException ex) {
                System.out.println("offline. (" + ex.getLocalizedMessage() + ")");
            }
        }
        return null; // no connected servers
    }

}
