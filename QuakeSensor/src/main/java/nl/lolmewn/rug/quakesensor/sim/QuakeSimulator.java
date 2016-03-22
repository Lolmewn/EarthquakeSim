package nl.lolmewn.rug.quakesensor.sim;

import nl.lolmewn.rug.quakecommon.mq.SenseData;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lolmewn.rug.quakecommon.Threader;
import nl.lolmewn.rug.quakesensor.SensorMain;

/**
 * The QuakeSimulator class takes care of simulating an earthquake and stacking
 * up the data that could be retrieved by an actual sensor in the case of an
 * Earthquake.
 *
 * @author Sybren
 */
public class QuakeSimulator implements Runnable {

    private final Queue<SenseData> quakeQueue = new ConcurrentLinkedQueue<>();

    /**
     * Instantiates the QuakeSimulator and runs its task.
     */
    public QuakeSimulator() {
        Threader.runTask(this);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void run() {
        while (true) {
            sleep();
            simulateQuake();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(10000, 30000));
        } catch (InterruptedException ex) {
            Logger.getLogger(QuakeSimulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Gets the next sensor data that is available. If there is no earthquake
     * going on, it returns an empty object (e.g. ground acceleration is 0)
     *
     * @return SenseData object representing sensory data
     */
    public SenseData getNextData() {
        if (quakeQueue.isEmpty()) {
            return new SenseData(0);
        }
        return quakeQueue.poll();
    }

    /**
     * Simulates an earthquake of between 2 and 4 seconds. Creates the sensory
     * data in advance and stores it in a queue until {@link #getNextData() } is
     * called.
     */
    private void simulateQuake() {
        int quakeTimeSeconds = ThreadLocalRandom.current().nextInt(2, 4); // quake between 5 and 30 seconds long.
        long quakeStartTime = System.currentTimeMillis();
        int totalDataPoints = quakeTimeSeconds * SensorMain.POLLS_PER_SECOND;
        int timePerDataPoint = 1000 / SensorMain.POLLS_PER_SECOND;
        for (int dataPointNr = 0; dataPointNr < totalDataPoints; dataPointNr++) {
            int msOffset = dataPointNr * timePerDataPoint;
            double acceleration = getAccelerationAt(
                    dataPointNr,
                    quakeTimeSeconds * SensorMain.POLLS_PER_SECOND
            );
            quakeQueue.add(new SenseData(acceleration, quakeStartTime + msOffset));
        }
    }

    /**
     * Gets the ground acceleration in m/s^2 at a given time x with the quake
     * taking maxX ticks.
     *
     * @param x Current location on the sinusoid.
     * @param maxX Length of the sinusoid 
     * @return value of the strength of the earthquake at a given time
     */
    private double getAccelerationAt(int x, int maxX) {
        return Math.sin(x / (maxX / Math.PI))
                * Math.sin(SensorMain.POLLS_PER_SECOND * maxX * 2 * x)
                * 0.4;
    }

}
