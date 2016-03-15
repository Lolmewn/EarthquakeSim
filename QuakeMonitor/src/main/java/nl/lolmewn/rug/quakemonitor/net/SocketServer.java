package nl.lolmewn.rug.quakemonitor.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lolmewn.rug.quakecommon.GsonHelper;
import nl.lolmewn.rug.quakecommon.Settings;
import nl.lolmewn.rug.quakecommon.Threader;
import nl.lolmewn.rug.quakecommon.net.packet.DataPacket;
import nl.lolmewn.rug.quakecommon.net.packet.ResponseServersPacket;

/**
 *
 * @author Lolmewn
 */
public class SocketServer implements Runnable {

    private final int port;
    private final Settings settings;
    private ServerSocket serverSocket;

    public SocketServer(int port, Settings settings) {
        this.port = port;
        this.settings = settings;
    }

    public void start() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            System.out.println("Detected open server; closing before restarting...");
            stop();
        }
        serverSocket = new ServerSocket(port);
        System.out.println("Starting to listen for connections in separate socket...");
        Threader.runTask(this);
    }

    public void stop() {
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
                DataPacket packet = (DataPacket) GsonHelper.receive(in);
                handlePacket(packet, incoming);
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handlePacket(DataPacket packet, Socket socket) throws IOException {
        System.out.println("Received packet type " + packet.getPacketType());
        switch (packet.getPacketType()) {
            case REQUEST_SERVERS:
                GsonHelper.send(socket, new ResponseServersPacket(this.settings.getServers()));
                return;
            case RESPONSE_SERVERS:
            // Handle from other servers
        }
    }

    public int getPort() {
        return this.port;
    }

}
