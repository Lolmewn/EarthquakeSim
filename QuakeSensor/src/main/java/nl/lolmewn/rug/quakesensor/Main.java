package nl.lolmewn.rug.quakesensor;

import nl.lolmewn.rug.quakesensor.gui.MainGUI;

/**
 *
 * @author Lolmewn
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Starting sensor...");
        SensorMain sensorMain = new SensorMain();

        System.out.println("Launching GUI...");
        new MainGUI(sensorMain);
        Thread shutdownThread = new Thread(() -> {
            sensorMain.notifySensorOffline();
        });
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

}
