package nl.lolmewn.rug.quakesensor.sim;

import java.io.Serializable;

/**
 * @author Sybren
 */
public class SenseData implements Serializable {

    private final double groundAcceleration;
    private final long timestamp;

    public SenseData(double acceleration) {
        this.groundAcceleration = acceleration;
        timestamp = System.currentTimeMillis();
    }

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
