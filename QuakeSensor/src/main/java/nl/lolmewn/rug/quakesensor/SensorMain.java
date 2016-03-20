package nl.lolmewn.rug.quakesensor;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lolmewn.rug.quakecommon.GsonHelper;
import nl.lolmewn.rug.quakecommon.Settings;
import nl.lolmewn.rug.quakecommon.net.PacketType;
import nl.lolmewn.rug.quakecommon.net.packet.SimpleDataPacket;
import nl.lolmewn.rug.quakesensor.mq.Sensor;
import nl.lolmewn.rug.quakesensor.net.ServerManager;
import nl.lolmewn.rug.quakesensor.net.ServerSyncer;
import nl.lolmewn.rug.quakesensor.sim.QuakeSimulator;

/**
 * Main class of the Sensor.
 *
 * @author Lolmewn
 */
public class SensorMain {

    private Settings settings;
    private ServerManager serverManager;
    private final QuakeSimulator simulator;
    public static final String DATA_QUEUE_NAME = "quake_data";
    public static final int POLLS_PER_SECOND = 10; // number of data pulled from sensor per second

    public SensorMain() {
        loadSettings();
        loadServerManager();
        new ServerSyncer(this); // Sync known servers with online servers
        // Simulate quakes; we don't have actual sensors nor the time to wait for an actual quake to happen.
        this.simulator = new QuakeSimulator();
        try {
            // Start the sensor, using the Simulator as sensing equipment
            new Sensor(simulator);
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(SensorMain.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Could not start sensor; shutting down.");
            System.exit(1);
        }
        notifySensorOnline(); // Notify all online servers that we're in business
    }

    public Settings getSettings() {
        return settings;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    /**
     * Load the settings of the application. Settings include, but are not
     * limited to: latitude and longitude, UUID, server location
     */
    private void loadSettings() {
        try {
            this.settings = new Settings("settings.properties");
            if (settings.isNewSettings()) {
                this.settings.setProperty("uuid", UUID.randomUUID().toString());
                this.settings.save();
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Failed to generate config, cannot start.");
            System.exit(1);
        }
    }

    /**
     * Load the server manager and his servers.
     */
    private void loadServerManager() {
        this.serverManager = new ServerManager(settings);
        this.settings.getServers().stream().forEach(address -> {
            try {
                System.out.print("Connecting to " + address.toString() + "...");
                serverManager.load(address);
                System.out.println(" online.");
            } catch (IOException ex) {
                System.out.println(" offline. (" + ex.getLocalizedMessage() + ")");
            }
        });
    }

    /**
     * Notify to all online servers that we're online and ready to send some
     * quake data.
     */
    private void notifySensorOnline() {
        getServerManager().getServers().stream().filter((server) -> (server.isConnected())).forEach((server) -> {
            try {
                GsonHelper.send(server.getSocket(), new SimpleDataPacket(PacketType.SENSOR_ONLINE));
            } catch (IOException ex) {
                Logger.getLogger(SensorMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Notify all online servers that we're shutting down.
     */
    public void notifySensorOffline() {
        getServerManager().getServers().stream().filter((server) -> (server.isConnected())).forEach((server) -> {
            try {
                GsonHelper.send(server.getSocket(), new SimpleDataPacket(PacketType.SENSOR_OFFLINE));
            } catch (IOException ex) {
                Logger.getLogger(SensorMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}
