package nl.lolmewn.rug.quakemonitor.net;

import java.util.UUID;

/**
 *
 * @author Lolmewn
 */
public class SensorInfo {

    private final UUID uuid;
    private final double longitude, latitude;

    public SensorInfo(UUID uuid, double longitude, double latitude) {
        this.uuid = uuid;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

}
