package nl.lolmewn.rug.quakesensor;

import java.io.IOException;
import java.util.UUID;
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
public class Sensor {

    private Settings settings;
    private ServerManager serverManager;

    public Sensor() {
        loadSettings();
        loadServerManager();
        new ServerSyncer(this);
        new QuakeSimulator(this);
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
