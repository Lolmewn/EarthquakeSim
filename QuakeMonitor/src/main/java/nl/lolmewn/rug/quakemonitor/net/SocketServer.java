package nl.lolmewn.rug.quakemonitor.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lolmewn.rug.quakecommon.GsonHelper;
import nl.lolmewn.rug.quakecommon.Settings;
import nl.lolmewn.rug.quakecommon.Threader;
import nl.lolmewn.rug.quakecommon.net.PacketType;
import nl.lolmewn.rug.quakecommon.net.ServerAddress;
import nl.lolmewn.rug.quakecommon.net.packet.DataPacket;
import nl.lolmewn.rug.quakecommon.net.packet.ResponseServersPacket;
import nl.lolmewn.rug.quakecommon.net.packet.SensorOnlinePacket;
import nl.lolmewn.rug.quakemonitor.FXMLController;

/**
 * SocketServer class handles all incoming Socket connections on a given port.
 * It then handles these connections in a separate thread as to allow multiple
 * connections at once.
 *
 * @author Lolmewn
 */
public class SocketServer implements Runnable {

    private final FXMLController controller;
    private final int port;
    private final Settings settings;
    private ServerSocket serverSocket;

    public SocketServer(FXMLController controller) {
        this.controller = controller;
        this.settings = controller.getSettings();
        this.port = settings.getInteger("server-port");
    }

    /**
     * Starts the Server. If server is already starting, this method acts as a
     * restart, calling stop() before continuing.
     *
     * @throws IOException Thrown when Server could not be started (e.g. port is
     * taken)
     */
    public void start() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            System.out.println("Detected open server; closing before restarting...");
            stop();
        }
        serverSocket = new ServerSocket(port);
        System.out.println("Starting to listen for connections in separate socket...");
        Threader.runTask(this);
    }

    /**
     * Stops the ServerSocket if it is not null and not closed.
     *
     * @throws IOException See {@link ServerSocket#close() }
     */
    public void stop() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            try {
                Socket incoming = serverSocket.accept();
                System.out.println("Got connection from " + incoming.getInetAddress() + ":" + incoming.getPort());
                Threader.runTask(() -> {
                    System.out.println("Accepting in separate thread...");
                    acceptInput(incoming);
                });
            } catch (IOException ex) {
                Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void acceptInput(Socket incoming) {
        try {
            while (incoming.isConnected() && !incoming.isClosed()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
                DataPacket packet = GsonHelper.receive(in);
                if (packet.getPacketType() == PacketType.SENSOR_OFFLINE) {
                    incoming.close();
                    return;
                }
                handlePacket(packet, incoming);
            }
        } catch (IOException ex) {
            if (ex instanceof SocketException && ex.getMessage().contains("Connection reset")) {
                // socket closed externally and not through SENSOR_OFFLINE
                System.err.println("Socket closed abrubtly (" + incoming.getInetAddress() + ":" + incoming.getPort() + ")");
                return;
            }
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handlePacket(DataPacket packet, Socket socket) throws IOException {
        System.out.println("Handling packet " + packet.getPacketType());
        switch (packet.getPacketType()) {
            case REQUEST_SERVERS:
                Set<ServerAddress> addresses = this.settings.getServers();
                GsonHelper.send(socket, new ResponseServersPacket(addresses));
                return;
            case RESPONSE_SERVERS:
                System.err.println("Got " + packet.getPacketType() + "; this should not have arrived here.");
                return;
            case SENSOR_ONLINE:
                SensorOnlinePacket sop = (SensorOnlinePacket) packet;
                SensorInfo info = new SensorInfo(sop.getUuid(), sop.getLongitude(), sop.getLatitude());
                //MapMarkers markers = new MapMarkers(info);
                //controller.getMarkers().put(info, new MapMarkers(info));
                //controller.activate(markers);
                controller.addSensor(info);
                return;
            default:
                System.out.println("DEFAULT: " + packet.getPacketType());
        }
    }

    public int getPort() {
        return this.port;
    }

}
