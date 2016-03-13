package nl.lolmewn.rug.quakecommon.net.packet;

import java.util.Set;
import nl.lolmewn.rug.quakecommon.net.PacketType;
import nl.lolmewn.rug.quakecommon.net.ServerAddress;

/**
 *
 * @author Lolmewn
 */
public class ResponseServersPacket implements DataPacket {

    private final Set<ServerAddress> addresses;

    public ResponseServersPacket(Set<ServerAddress> addrs) {
        this.addresses = addrs;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.RESPONSE_SERVERS;
    }

    public Set<ServerAddress> getAddresses() {
        return addresses;
    }

}
