package nl.lolmewn.rug.quakesensor.sim;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lolmewn.rug.quakecommon.Threader;
import nl.lolmewn.rug.quakesensor.SensorMain;

/**
 * @author Sybren
 */
public class QuakeSimulator implements Runnable {

    private final Queue<SenseData> quakeQueue = new ConcurrentLinkedQueue<>();

    public QuakeSimulator() {
        Threader.runTask(this);
    }

    @Override
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

    public SenseData getNextData() {
        if (quakeQueue.isEmpty()) {
            return new SenseData(0);
        }
        return quakeQueue.poll();
    }

    private void simulateQuake() {
        int quakeTimeSeconds = ThreadLocalRandom.current().nextInt(5, 10); // quake between 5 and 30 seconds long.
        long quakeStartTime = System.currentTimeMillis();
        for (int second = 0; second < quakeTimeSeconds; second++) {
            for (int nrInSecond = 0; second < SensorMain.POLLS_PER_SECOND; second++) {
                int msOffset = second * 1000 + nrInSecond * (1000 / SensorMain.POLLS_PER_SECOND);
                double acceleration = getAccelerationAt(
                        second * SensorMain.POLLS_PER_SECOND + nrInSecond,
                        quakeTimeSeconds * SensorMain.POLLS_PER_SECOND
                );
                System.out.println(String.format("acc: %.3f at %d", acceleration, quakeStartTime + msOffset));
                quakeQueue.add(new SenseData(acceleration, quakeStartTime + msOffset));
            }
        }
    }

    private double getAccelerationAt(int x, int maxX) {
        return Math.sin(x / (maxX / Math.PI))
                * Math.sin(SensorMain.POLLS_PER_SECOND * maxX * 2 * x)
                * 0.4;
    }

}
