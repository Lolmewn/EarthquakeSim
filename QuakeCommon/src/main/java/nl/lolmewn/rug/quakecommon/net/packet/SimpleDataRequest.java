package nl.lolmewn.rug.quakecommon.net.packet;

import nl.lolmewn.rug.quakecommon.net.PacketType;

/**
 *
 * @author Lolmewn
 */
public class SimpleDataRequest implements DataPacket {

    private final PacketType packetType;

    public SimpleDataRequest(PacketType packetType) {
        this.packetType = packetType;
    }

    @Override
    public PacketType getPacketType() {
        return packetType;
    }

}
