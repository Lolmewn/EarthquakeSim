package nl.lolmewn.rug.quakemonitor.net;

import java.util.HashSet;
import java.util.Set;
import nl.lolmewn.rug.quakecommon.net.ServerAddress;

/**
 * Manager of all known servers. Uses a Set to disallow duplicates
 *
 * @author Lolmewn
 */
public class ServerManager {

    private static final Set<ServerAddress> ADDRESSES = new HashSet<>();

    private ServerManager() {
        // Private constructor to disallow instantiation of class
    }

    /**
     * Add an address to the ServerManager. If the address is already in the
     * collection, nothing happens.
     *
     * @param address ServerSAddress to add
     * @see Set#add(java.lang.Object)
     */
    public static void addServerAddress(ServerAddress address) {
        ADDRESSES.add(address);
    }

    /**
     * Get all ServerAddresses known by this manager
     *
     * @return Set of ServerAddresses
     */
    public static Set<ServerAddress> getAddresses() {
        return ADDRESSES;
    }

    /**
     * Removes, if present, a ServerAddress from the collection
     *
     * @param address Address to remove
     */
    public static void removeAddress(ServerAddress address) {
        ADDRESSES.remove(address);
    }

}
