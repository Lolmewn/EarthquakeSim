package nl.lolmewn.rug.quakesensor;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lolmewn.rug.quakecommon.Settings;
import nl.lolmewn.rug.quakesensor.net.ServerManager;
import nl.lolmewn.rug.quakesensor.sim.QuakeSimulator;
import nl.lolmewn.rug.quakesensor.sim.SenseData;

/**
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
        new ServerSyncer(this);
        this.simulator = new QuakeSimulator();
        try {
            new Sensor(simulator);
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(SensorMain.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Could not start sensor; shutting down.");
            System.exit(1);
        }
    }

    public Settings getSettings() {
        return settings;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

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

    private void loadServerManager() {
        this.serverManager = new ServerManager();
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

    public void sense(SenseData senseData) {

    }

}
