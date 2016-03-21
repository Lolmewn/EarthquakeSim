package nl.lolmewn.rug.quakecommon.net.packet;

import nl.lolmewn.rug.quakecommon.net.PacketType;

/**
 * DataPacket used in TCP transmission
 *
 * @author Lolmewn
 */
public interface DataPacket {

    /**
     * Type of the DataPacket. Used for identifying the information in the
     * Packet
     *
     * @return PacketType of this DataPacket
     */
    public PacketType getPacketType();

}
