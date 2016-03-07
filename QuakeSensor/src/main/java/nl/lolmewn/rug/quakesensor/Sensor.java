package nl.lolmewn.rug.quakesensor;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.lolmewn.rug.quakesensor.net.ServerManager;

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
    }

    public Settings getSettings() {
        return settings;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    private void loadSettings() {
        try {
            this.settings = new Settings();
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
                serverManager.load(address);
            } catch (IOException ex) {
                System.err.println("===========================================");
                System.err.println("Failed to connect to " + address.toString());
                System.err.println("Error: " + ex.getLocalizedMessage());
                System.err.println("===========================================");
            }
        });
    }

}
