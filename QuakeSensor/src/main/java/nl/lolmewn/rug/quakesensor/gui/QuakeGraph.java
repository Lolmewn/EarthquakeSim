package nl.lolmewn.rug.quakesensor.gui;

import java.awt.Graphics;
import javax.swing.JPanel;
import nl.lolmewn.rug.quakesensor.sim.SenseData;
import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 *
 * @author Lolmewn
 */
public class QuakeGraph extends JPanel {

    /**
     * Every 5 pixels is one data point
     */
    private static final int PIXELS_PER_TICK = 2;
    private static final CircularFifoQueue<SenseData> QUEUE = new CircularFifoQueue(1080 / QuakeGraph.PIXELS_PER_TICK); // 1080 pixels worth of data should be enough

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        int startX = this.getWidth() - QUEUE.size() * PIXELS_PER_TICK; // old entries will be drawn outside of the screen, and that's okay

        int margin = this.getHeight() / 2; // Don't know a good name for this variable
        int middle = this.getHeight() / 2;
        int startY = middle;
        synchronized (QUEUE) {
            for (SenseData data : QUEUE) {
                // do something with the data
                int endY = (int) (data.getGroundAcceleration() * margin + middle);
                grphcs.drawLine(startX, startY, startX + PIXELS_PER_TICK, endY);
                startX += PIXELS_PER_TICK;
                startY = endY;
            }
        }
    }

    public static void addDatapoint(SenseData data) {
        synchronized (QUEUE) {
            QUEUE.add(data);
        }
    }

}
