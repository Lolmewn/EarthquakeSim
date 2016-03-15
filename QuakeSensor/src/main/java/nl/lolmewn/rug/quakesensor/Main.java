package nl.lolmewn.rug.quakesensor;

import nl.lolmewn.rug.quakesensor.gui.MainGUI;

/**
 *
 * @author Lolmewn
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("Starting sensor...");
        SensorMain sensor = new SensorMain();
        
        System.out.println("Launching GUI...");
        MainGUI gui = new MainGUI(sensor);
    }
    
    
    
}
