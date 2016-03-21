package nl.lolmewn.rug.quakecommon.net.packet;

import nl.lolmewn.rug.quakecommon.net.PacketType;

/**
 * A SimpleDataPacket is a DataPacket which does not hold any information on its
 * own, but for example requests it. (e.g. PacketType.REQUEST_SERVERS)
 *
 * @author Lolmewn
 */
public class SimpleDataPacket implements DataPacket {

    private final PacketType packetType;

    public SimpleDataPacket(PacketType packetType) {
        this.packetType = packetType;
    }

    @Override
    /**
     * {@inheritDoc }
     */
    public PacketType getPacketType() {
        return packetType;
    }

}
