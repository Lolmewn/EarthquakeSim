package nl.lolmewn.rug.quakecommon.net.packet;

import java.util.UUID;
import nl.lolmewn.rug.quakecommon.net.PacketType;

/**
 *
 * @author Lolmewn
 */
public class SensorOnlinePacket implements DataPacket {

    private final double latitude, longitude;
    private final UUID uuid;

    public SensorOnlinePacket(double latitude, double longitude, UUID uuid) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.uuid = uuid;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.SENSOR_ONLINE;
    }

}
