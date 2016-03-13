package nl.lolmewn.rug.quakemonitor.net;

import java.util.ArrayList;
import java.util.List;
import nl.lolmewn.rug.quakecommon.net.ServerAddress;

/**
 *
 * @author Lolmewn
 */
public class ServerManager {

    private static final List<ServerAddress> ADDRESSES = new ArrayList<>();

    private ServerManager() {
    }

    public static void addServerAddress(ServerAddress address) {
        ADDRESSES.add(address);
    }

    public static List<ServerAddress> getAddresses() {
        return ADDRESSES;
    }
    
    public static void removeAddress(ServerAddress address){
        ADDRESSES.remove(address);
    }

}
