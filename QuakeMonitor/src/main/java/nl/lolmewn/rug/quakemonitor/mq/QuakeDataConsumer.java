package nl.lolmewn.rug.quakemonitor.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import nl.lolmewn.rug.quakecommon.GsonHelper;
import nl.lolmewn.rug.quakecommon.Settings;
import nl.lolmewn.rug.quakecommon.mq.SensorData;
import nl.lolmewn.rug.quakemonitor.FXMLController;
import nl.lolmewn.rug.quakemonitor.net.SensorInfo;

/**
 * Consumer of the earthquake data. Uses RabbitMQ to fetch the data.
 *
 * @author Lolmewn
 */
public class QuakeDataConsumer {

    private final FXMLController controller;
    private final Consumer consumer;

    /**
     * Constructs the QuakeDataConsumer object and connects to RabbitMQ.
     *
     * @param controller Main controller of the application, provides settings
     * and UI hooks
     * @throws IOException Thrown when connection to RabbitMQ could not be made
     * for any reason.
     * @throws TimeoutException Thrown when connection to RabbitMQ times out
     */
    public QuakeDataConsumer(FXMLController controller) throws IOException, TimeoutException {
        this.controller = controller;
        Settings settings = controller.getSettings();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(settings.getProperty("mq-host"));
        factory.setPort(settings.getInteger("mq-port"));
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                QuakeDataConsumer.this.handleDelivery(consumerTag, envelope, properties, body);
            }
        };
        channel.basicConsume("quake_data", true, consumer);
    }

    private void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
        // This method handles all packets coming in from RabbitMQ
        String message = new String(body);
        SensorData data = GsonHelper.ungsonify(message, SensorData.class);
        SensorInfo info = controller.getSensor(data.getSensorUUID());
        if(info == null){
            //System.out.println("Sensor was sending data but we have no idea who it was...");
            //System.out.println("Waiting for SENSOR_ONLINE packet to happen before data can be recorded.");
            return;
        }
        /*MapMarkers markers = controller.getMarkers().get(info);
        markers.quake(data.getGroundAcceleration());*/
        controller.quake(info, data.getGroundAcceleration());
    }

}
