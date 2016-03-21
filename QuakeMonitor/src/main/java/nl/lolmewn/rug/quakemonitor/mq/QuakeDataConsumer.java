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

/**
 * Consumer of the earthquake data. Uses RabbitMQ to fetch the data.
 *
 * @author Lolmewn
 */
public class QuakeDataConsumer {

    private final Consumer consumer;

    /**
     * Constructs the QuakeDataConsumer object and connects to RabbitMQ.
     *
     * @throws IOException Thrown when connection to RabbitMQ could not be made
     * for any reason.
     * @throws TimeoutException Thrown when connection to RabbitMQ times out
     */
    public QuakeDataConsumer() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
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
        // TODO
    }

}
