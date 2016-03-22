package nl.lolmewn.rug.quakecommon.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lolmewn.rug.quakecommon.GsonHelper;
import nl.lolmewn.rug.quakecommon.Settings;
import nl.lolmewn.rug.quakecommon.Threader;
import nl.lolmewn.rug.quakecommon.net.packet.ResponseServersPacket;
import nl.lolmewn.rug.quakecommon.net.packet.SimpleDataPacket;

/**
 * Class which synchronizes the known servers with all active servers. This is
 * done to keep the list of known servers updated throughout time without having
 * to manually add a new server to all sensors.
 *
 * @author Lolmewn
 */
public class ServerSyncer implements Runnable {

    private final Settings settings;
    private final ServerManager serverManager;

    /**
     * Instantiates the Syncer and starts its task.
     *
     * @param settings Settings used for ServerAddresses
     * @param serverManager Manager of currently active servers
     */
    public ServerSyncer(Settings settings, ServerManager serverManager) {
        this.settings = settings;
        this.serverManager = serverManager;
        Threader.runTask(this);
    }

    @Override
    /**
     * {@inheritDoc }
     */
    public void run() {
        while (true) {
            // Let's look for a free server
            System.out.println("Starting sync...");
            Server activeServer = serverManager.getActiveServer();
            if (activeServer == null) {
                System.out.println("No servers online, cannot sync servers. Trying again in 1 minute");
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServerSyncer.class.getName()).log(Level.SEVERE, null, ex);
                }
                continue;
            }
            Socket socket = activeServer.getSocket();
            try {
                GsonHelper.send(socket, new SimpleDataPacket(PacketType.REQUEST_SERVERS));
                System.out.println("Reading reply...");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ResponseServersPacket packet = (ResponseServersPacket) GsonHelper.receive(reader);
                System.out.println("Received " + packet.getClass().getName());
                settings.saveServers(combine(settings.getServers(), packet.getAddresses()));
            } catch (IOException ex) {
                Logger.getLogger(ServerSyncer.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(60 * 1000L);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerSyncer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Combines the set of known ServerAddresses with the retrieved Set of
     * ServerAddresses. This performs the union of the two sets.
     *
     * @see Set#addAll(java.util.Collection)
     *
     * @param own Own Set of Addresses
     * @param other Other Set of Addresses
     * @return Union of the two given sets
     */
    public Set<ServerAddress> combine(Set<ServerAddress> own, Set<ServerAddress> other) {
        own.addAll(other);
        return own;
    }

}
