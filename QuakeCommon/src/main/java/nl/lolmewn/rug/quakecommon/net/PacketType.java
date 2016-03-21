package nl.lolmewn.rug.quakecommon.net;

/**
 * Type of packet used in DataPackets. Packet types are self-explanatory
 *
 * @author Lolmewn
 */
public enum PacketType {

    QUAKE_DATA,
    REQUEST_SERVERS,
    RESPONSE_SERVERS,
    SENSOR_ONLINE,
    SERVER_ONLINE, // unused
    SENSOR_OFFLINE,
    SERVER_OFFLINE // unused

}
