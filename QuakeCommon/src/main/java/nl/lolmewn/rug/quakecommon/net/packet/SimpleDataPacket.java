package nl.lolmewn.rug.quakecommon.net.packet;

import nl.lolmewn.rug.quakecommon.net.PacketType;

/**
 *
 * @author Lolmewn
 */
public class SimpleDataPacket implements DataPacket {

    private final PacketType packetType;

    public SimpleDataPacket(PacketType packetType) {
        this.packetType = packetType;
    }

    @Override
    public PacketType getPacketType() {
        return packetType;
    }

}
