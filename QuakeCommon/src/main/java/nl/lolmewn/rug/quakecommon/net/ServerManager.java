package nl.lolmewn.rug.quakecommon.net;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lolmewn.rug.quakecommon.Settings;

/**
 * Manager of known servers.
 *
 * @author Lolmewn
 */
public class ServerManager {

    private final List<Server> servers = new ArrayList<>();
    private final Settings settings;

    public ServerManager(Settings settings) {
        this.settings = settings;
    }

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

    /**
     * Checks if there is a server active at the given IP and port. Does not
     * check if it is a QuakeMonitor Server.
     *
     * @param IP Address to connect to
     * @param port Port to connect to
     * @return true if there is a server, false otherwise (in case of an
     * IOException)
     */
    public boolean checkAvailability(String IP, int port) {
        try (Socket s = new Socket(IP, port)) {
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    /**
     * Get all servers managed by this class. May contain connected and
     * disconnected servers.
     *
     * @return List of all servers
     */
    public List<Server> getServers() {
        return servers;
    }

    /**
     * Tries to find an active server. If a connected server is found, it uses
     * that Server. Otherwise, it tries to set up a new connection to servers.
     *
     * @see Server#isConnected()
     *
     * @return Returns the first active server it can find, or null if none can
     * be found.
     */
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

    /**
     * Saves new server data to the settings file. Does not try to connect to
     * the server. If a server is found with the same IP and port, nothing
     * happens.
     *
     * @param IP Address of the server
     * @param port Port of the server
     */
    public void saveNewServer(String IP, int port) {
        Set<ServerAddress> addresses = settings.getServers();
        int size = addresses.size();
        addresses.add(new ServerAddress(IP, port));
        if (addresses.size() != size) { // Size changed, new item was added.
            try {
                settings.saveServers(addresses);
            } catch (IOException ex) {
                Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Removes a Server from the settings file. Does not disconnect the server.
     * If no server with the given IP and port are found, nothing happens.
     *
     * @param IP Address of the server
     * @param port Port of the server
     */
    public void deleteServer(String IP, int port) {
        String allServers = settings.getProperty("servers");
        String oneRemoved = allServers.replace(IP + ":" + port, "");
        if (allServers.equals(oneRemoved)) {
            // Not found
            System.err.println("Failed to delete server (" + IP + ":" + port + "), ignornig.");
            return;
        }
        settings.setProperty("servers", oneRemoved);
        try {
            settings.save();
        } catch (IOException ex) {
            Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
