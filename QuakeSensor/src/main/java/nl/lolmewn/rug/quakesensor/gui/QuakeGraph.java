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
    private static final CircularFifoQueue<SenseData> QUEUE = new CircularFifoQueue(216); // 1080 pixels worth of data should be enough

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        int startX = this.getWidth() - QUEUE.size(); // old entries will be drawn outside of the screen, and that's okay

        int thisNeedsABetterName = this.getHeight() / 2;
        int middle = this.getHeight() / 2;
        int startY = middle;
        synchronized (QUEUE) {
            QUEUE.forEach((data) -> {
                // do something with the data
                int endY = (int) (data.getGroundAcceleration() * thisNeedsABetterName + middle);
                grphcs.drawLine(startX, startY, startX + 5, endY);
            });
        }
        grphcs.drawLine(5, 5, 50, 50);
    }

    public static void addDatapoint(SenseData data) {
        synchronized (QUEUE) {
            QUEUE.add(data);
        }
    }

}
