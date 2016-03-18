package nl.lolmewn.rug.quakesensor.net;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lolmewn.rug.quakecommon.Settings;
import nl.lolmewn.rug.quakecommon.net.ServerAddress;
import nl.lolmewn.rug.quakesensor.SensorMain;

/**
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

    public void saveNewServer(String IP, int port) {
        Set<ServerAddress> addresses = settings.getServers();
        int size = addresses.size();
        addresses.add(new ServerAddress(IP, port));
        if (addresses.size() != size) { // Size changed, new item was added.
            try {
                settings.saveServers(addresses);
            } catch (IOException ex) {
                Logger.getLogger(SensorMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

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
            Logger.getLogger(SensorMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
