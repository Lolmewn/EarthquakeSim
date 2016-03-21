package nl.lolmewn.rug.quakesensor.sim;

import java.io.Serializable;

/**
 * Sensor data class. Stores the ground acceleration in m/s^2 and the timestamp
 * of when this occurred.
 *
 * @see System#currentTimeMillis()
 * @author Sybren
 */
public class SenseData implements Serializable {

    private final double groundAcceleration;
    private final long timestamp;

    /**
     * Initializes the Sensor data with the given acceleration and the current
     * timestamp
     *
     * @see System#currentTimeMillis()
     *
     * @param acceleration Ground acceleration in m/s^2
     */
    public SenseData(double acceleration) {
        this(acceleration, System.currentTimeMillis());
    }

    /**
     * Initializes the Sensor data with the given acceleration and the given
     * timestamp
     *
     * @param groundAcceleration Ground acceleration in m/s^2
     * @param timestamp Timestamp of when this acceleration occurred
     */
    public SenseData(double groundAcceleration, long timestamp) {
        this.groundAcceleration = groundAcceleration;
        this.timestamp = timestamp;
    }

    public double getGroundAcceleration() {
        return groundAcceleration;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
