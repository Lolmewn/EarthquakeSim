package nl.lolmewn.rug.quakesensor.net;

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
import nl.lolmewn.rug.quakecommon.net.PacketType;
import nl.lolmewn.rug.quakecommon.net.ServerAddress;
import nl.lolmewn.rug.quakecommon.net.packet.ResponseServersPacket;
import nl.lolmewn.rug.quakecommon.net.packet.SimpleDataRequest;
import nl.lolmewn.rug.quakesensor.SensorMain;
import nl.lolmewn.rug.quakesensor.net.Server;
import nl.lolmewn.rug.quakesensor.net.ServerManager;

/**
 *
 * @author Lolmewn
 */
public class ServerSyncer implements Runnable {

    private final SensorMain sensor;

    public ServerSyncer(SensorMain sensor) {
        this.sensor = sensor;
        System.out.println("Syncing known servers with other servers...");
        Threader.runTask(this);
    }

    @Override
    public void run() {
        while (true) {
            // Let's look for a free server
            System.out.println("Starting sync...");
            Server activeServer = getActiveServer();
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
                System.out.println("Sending REQUEST_SERVERS...");
                GsonHelper.send(socket, new SimpleDataRequest(PacketType.REQUEST_SERVERS));
                System.out.println("Reading reply...");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ResponseServersPacket packet = (ResponseServersPacket) GsonHelper.receive(reader);
                System.out.println("Received " + packet.getClass().getName());
                this.getSettings().saveServers(combine(this.getSettings().getServers(), packet.getAddresses()));
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

    private Server getActiveServer() {
        for (Server server : this.getServerManager().getServers()) {
            if (server.isConnected()) {
                return server;
            }
        }
        // No active sockets; let's check for active servers.
        System.out.println("Checking for active servers...");
        for (Server server : this.getServerManager().getServers()) {
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

    public Set<ServerAddress> combine(Set<ServerAddress> own, Set<ServerAddress> other) {
        own.addAll(other);
        return own;
    }

    public Settings getSettings() {
        return sensor.getSettings();
    }

    public ServerManager getServerManager() {
        return sensor.getServerManager();
    }

}
