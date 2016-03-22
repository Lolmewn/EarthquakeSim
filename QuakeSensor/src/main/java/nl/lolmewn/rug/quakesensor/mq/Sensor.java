package nl.lolmewn.rug.quakesensor.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lolmewn.rug.quakecommon.GsonHelper;
import nl.lolmewn.rug.quakecommon.Settings;
import nl.lolmewn.rug.quakecommon.Threader;
import nl.lolmewn.rug.quakecommon.mq.SenseData;
import nl.lolmewn.rug.quakecommon.mq.SensorData;
import nl.lolmewn.rug.quakesensor.SensorMain;
import nl.lolmewn.rug.quakesensor.gui.QuakeGraph;
import nl.lolmewn.rug.quakesensor.sim.QuakeSimulator;

/**
 * Sensor class, acting like an earthquake sensor. Grabs its data from the
 * QuakeSimulator and sends the collected data to RabbitMQ
 *
 * @author Lolmewn
 */
public class Sensor implements Runnable {

    private final QuakeSimulator simulator;
    private final UUID sensorUUID;
    private final Channel dataChannel;

    /**
     * Instantiates the sensor and connects to RabbitMQ.
     *
     * @param simulator Simulator to use to simulate earthquake data
     * @param settings Settings of the application
     * @throws IOException Thrown when connection to RabbitMQ fails
     * @throws TimeoutException Thrown when connection to RabbitMQ times out
     */
    public Sensor(QuakeSimulator simulator, Settings settings) throws IOException, TimeoutException {
        this.simulator = simulator;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(settings.getProperty("mq-host"));
        factory.setPort(settings.getInteger("mq-port"));
        Connection connection = factory.newConnection();
        dataChannel = connection.createChannel();
        dataChannel.queueDeclare(SensorMain.DATA_QUEUE_NAME, false, false, false, null);
        this.sensorUUID = UUID.fromString(settings.getProperty("uuid"));
        Threader.runTask(this);
        System.out.println("=== Sensor online and collecting data ===");
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void run() {
        while (true) {
            // Poll data from sensor; in this case, the simulator.
            SenseData data = simulator.getNextData();
            QuakeGraph.addDatapoint(data); // add data point to GUI

            SensorData sensorData = new SensorData(data, this.sensorUUID); // Encapsulate the data and add our UUID
            String message = GsonHelper.gsonify(sensorData); // Stringify
            try {
                this.dataChannel.basicPublish("", SensorMain.DATA_QUEUE_NAME, null, message.getBytes()); // send it away
            } catch (IOException ex) {
                Logger.getLogger(Sensor.class.getName()).log(Level.SEVERE, null, ex);
            }
            sleep(1000 / SensorMain.POLLS_PER_SECOND);
        }
    }

    /**
     * Make a Thread sleep for a certain amount of time
     *
     * @param time time to sleep for, in milliseconds.
     * @see Thread#sleep(long)
     */
    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sensor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
