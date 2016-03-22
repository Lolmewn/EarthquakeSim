package nl.lolmewn.rug.quakecommon.mq;

import java.util.UUID;

/**
 *
 * @author Lolmewn
 */
public class SensorData {

    private final SenseData data;
    private final UUID sensorUUID;

    public SensorData(SenseData data, UUID sensorUUID) {
        this.data = data;
        this.sensorUUID = sensorUUID;
    }

    public UUID getSensorUUID() {
        return sensorUUID;
    }

    public double getGroundAcceleration() {
        return data.getGroundAcceleration();
    }

    public long getTimestamp() {
        return data.getTimestamp();
    }

}
