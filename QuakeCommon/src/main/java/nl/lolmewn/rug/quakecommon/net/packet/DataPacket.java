package nl.lolmewn.rug.quakecommon.net.packet;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import nl.lolmewn.rug.quakecommon.net.PacketType;

/**
 *
 * @author Lolmewn
 */
public abstract class DataPacket implements Serializable {

    private static volatile Gson GSON;

    public abstract PacketType getPacketType();

    static {
        GSON = new Gson();
    }

    public void send(Socket socket) throws IOException {
        String json = GSON.toJson(this);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.println(json);
    }

    public static DataPacket receive(String json) {
        return GSON.fromJson(json, DataPacket.class);
    }

}
