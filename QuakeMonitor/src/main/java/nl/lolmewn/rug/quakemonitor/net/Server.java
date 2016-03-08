package nl.lolmewn.rug.quakemonitor.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lolmewn.rug.quakecommon.Threader;
import nl.lolmewn.rug.quakecommon.net.packet.DataPacket;
import nl.lolmewn.rug.quakecommon.net.packet.ResponseServersPacket;

/**
 *
 * @author Lolmewn
 */
public class Server implements Runnable {

    private final int port;
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
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
                    acceptInput(incoming);
                });
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void acceptInput(Socket incoming) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(incoming.getInputStream())); /*PrintWriter out = new PrintWriter(incoming.getOutputStream())*/) {
            String read = in.readLine();
            System.out.println(":: " + read);
            DataPacket packet = DataPacket.receive(read);
            handlePacket(packet, incoming);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handlePacket(DataPacket packet, Socket socket) throws IOException {
        switch (packet.getPacketType()) {
            case REQUEST_SERVERS:
                new ResponseServersPacket(null).send(socket);
                return;
            case RESPONSE_SERVERS:
                // Handle from other servers
        }
    }

    private void sendServers() {
    }

    public int getPort() {
        return this.port;
    }

}
