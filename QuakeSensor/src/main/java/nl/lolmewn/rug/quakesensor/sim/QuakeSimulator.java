package nl.lolmewn.rug.quakesensor.sim;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lolmewn.rug.quakecommon.Threader;
import nl.lolmewn.rug.quakesensor.Sensor;

/**
 * @author Sybren
 */
public class QuakeSimulator implements Runnable {

    private final Sensor sensor;

    public QuakeSimulator(Sensor sensor) {
        this.sensor = sensor;
        Threader.runTask(this);
    }

    @Override
    public void run() {
        while (true) {
            sleep(true);
            sensor.sense(new SenseData());
        }
    }

    private void sleep(boolean rand) {
        try {
            Thread.sleep(1000 + (rand ? ThreadLocalRandom.current().nextInt(5000) : 0)); // between 1 and 6 seconds
        } catch (InterruptedException ex) {
            Logger.getLogger(QuakeSimulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
