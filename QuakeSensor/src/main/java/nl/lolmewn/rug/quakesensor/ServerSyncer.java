package nl.lolmewn.rug.quakesensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lolmewn.rug.quakecommon.Threader;
import nl.lolmewn.rug.quakecommon.net.PacketType;
import nl.lolmewn.rug.quakecommon.net.packet.DataPacket;
import nl.lolmewn.rug.quakecommon.net.packet.ResponseServersPacket;
import nl.lolmewn.rug.quakecommon.net.packet.SimpleDataRequest;
import nl.lolmewn.rug.quakesensor.net.Server;

/**
 *
 * @author Lolmewn
 */
public class ServerSyncer implements Runnable {

    private final Sensor sensor;

    public ServerSyncer(Sensor sensor) {
        this.sensor = sensor;
        System.out.println("Syncing known servers with other servers...");
        Threader.runTask(this);
    }

    @Override
    public void run() {
        while (true) {
            // Let's look for a free server
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
                new SimpleDataRequest(PacketType.REQUEST_SERVERS).send(socket);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String json = reader.readLine();
                ResponseServersPacket packet = (ResponseServersPacket) DataPacket.receive(json);
            } catch (IOException ex) {
                Logger.getLogger(ServerSyncer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Server getActiveServer() {
        for (Server server : this.sensor.getServerManager().getServers()) {
            if (server.isConnected()) {
                return server;
            }
        }
        return null; // no connected servers
    }

}
