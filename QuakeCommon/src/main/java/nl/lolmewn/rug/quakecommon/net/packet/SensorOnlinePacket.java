package nl.lolmewn.rug.quakecommon.net.packet;

import java.util.UUID;
import nl.lolmewn.rug.quakecommon.net.PacketType;

/**
 * DataPacket which is sent to all servers when a Sensor goes online. DataPacket
 * contains information about the sensor's UUID and GPS coordinates.
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
    /**
     * {@inheritDoc }
     */
    public PacketType getPacketType() {
        return PacketType.SENSOR_ONLINE;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public UUID getUuid() {
        return uuid;
    }

}
