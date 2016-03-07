package nl.lolmewn.rug.quakesensor.net;

import nl.lolmewn.rug.quakecommon.net.ServerAddress;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lolmewn
 */
public class ServerManager {
    
    private final List<Server> servers = new ArrayList<>();
    
    /**
     * Loads the connection data and connects to the endpoint
     * @param address Address to connect to
     * @throws IOException Thrown when the socket could not connect to the endpoint
     */
    public void load(ServerAddress address) throws IOException{
        Server server = new Server(address);
        this.servers.add(server);
        server.connect();
    }

    public boolean checkAvailability(String IP, int port) {
        // TODO implement
        return true;
    }

    public List<Server> getServers() {
        return servers;
    }

}
