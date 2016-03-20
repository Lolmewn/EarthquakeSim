package nl.lolmewn.rug.quakesensor;

import nl.lolmewn.rug.quakesensor.gui.MainGUI;

/**
 * Main class of the application. The program starts here.
 * @author Lolmewn
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Starting sensor...");
        SensorMain sensorMain = new SensorMain();

        System.out.println("Launching GUI...");
        new MainGUI(sensorMain.getServerManager());
        
        // Create a shutdown hook thread, runs on shutdown of the GUI (and thereby, the program)
        Thread shutdownThread = new Thread(() -> {
            sensorMain.notifySensorOffline();
        });
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

}
