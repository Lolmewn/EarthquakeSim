package nl.lolmewn.rug.quakecommon.net.packet;

import java.util.List;
import nl.lolmewn.rug.quakecommon.net.PacketType;
import nl.lolmewn.rug.quakecommon.net.ServerAddress;

/**
 *
 * @author Lolmewn
 */
public class ResponseServersPacket extends DataPacket {

    private final List<ServerAddress> addresses;

    public ResponseServersPacket(List<ServerAddress> addrs) {
        this.addresses = addrs;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.RESPONSE_SERVERS;
    }

    public List<ServerAddress> getAddresses() {
        return addresses;
    }

}
