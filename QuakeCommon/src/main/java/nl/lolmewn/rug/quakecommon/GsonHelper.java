package nl.lolmewn.rug.quakecommon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lolmewn.rug.quakecommon.net.packet.DataPacket;

/**
 * GsonHelper is a class that performs basic GSON functions.
 *
 * @author Lolmewn
 */
public class GsonHelper {

    private static final transient Gson GSON;

    private GsonHelper() {
        // empty constructor to disallow anyone from instantiating this class
    }

    /**
     * Runs the first time a static method is called on this class. Sets up GSON
     * using the GsonBuilder.
     */
    static {
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(DataPacket.class, new InterfaceAdapter<>());
        GSON = builder.create();
    }

    /**
     * Send a DataPacket to someone using the supplied socket.
     *
     * @param socket Socket to send the packet on
     * @param packet Packet to send, must implement DataPacket
     * @throws IOException Thrown when sending of packet failed, e.g. due to
     * closed socket
     * @throws IllegalArgumentException Thrown when one of the arguments is null
     */
    public static void send(Socket socket, DataPacket packet) throws IOException {
        if (socket == null || packet == null) {
            throw new IllegalArgumentException("Cannot pass null variables to method");
        }
        System.out.println("Sending " + packet.getPacketType());
        String json = GSON.toJson(packet, DataPacket.class);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println(json);
    }

    /**
     * Read a DataPacket from a BufferedReader
     *
     * @param reader BufferedReader containing the DataPacket information.
     * @return Filled DataPacket object
     */
    public static DataPacket receive(BufferedReader reader) {
        StringBuilder buffer = new StringBuilder();
        String read;
        try {
            while ((buffer.length() == 0 || reader.ready()) && (read = reader.readLine()) != null) {
                buffer.append(read);
            }
        } catch (IOException ex) {
            Logger.getLogger(DataPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        DataPacket packet = GSON.fromJson(buffer.toString(), DataPacket.class);
        System.out.println("Received packet: " + packet.getPacketType());
        return packet;
    }

    /**
     * Turns any object into a GSON String
     * @param data Object to stringify
     * @return Stringified object
     */
    public static String gsonify(Object data) {
        return GSON.toJson(data);
    }
}
