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
 *
 * @author Lolmewn
 */
public class GsonHelper {

    private static final transient Gson GSON;

    private GsonHelper() {
    }

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.enableComplexMapKeySerialization();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(DataPacket.class, new InterfaceAdapter<>());
        GSON = builder.create();
    }

    public static void send(Socket socket, DataPacket packet) throws IOException {
        System.out.println("Sending " + packet.getPacketType());
        String json = GSON.toJson(packet, DataPacket.class);
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println(json);
    }

    public static Object receive(BufferedReader reader) {
        StringBuilder buffer = new StringBuilder();
        String read;
        try {
            while ((buffer.length() == 0 || reader.ready()) && (read = reader.readLine()) != null) {
                buffer.append(read);
                System.out.println(":: Read " + read);
            }
        } catch (IOException ex) {
            Logger.getLogger(DataPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return GSON.fromJson(buffer.toString(), DataPacket.class);
    }

    public static String gsonify(Object data) {
        return GSON.toJson(data);
    }
}
