import javax.swing.*;
import java.awt.*;

// renderer.java

// handles drawing only
// reads from a snapshot provided by the main thread each frame
// snapshot format: [id, x, y, radius, r, g, b]

public class Renderer extends JPanel {

    private static final long serialVersionUID = 3L;

    private volatile double[][] snapshot = new double[0][];

    private volatile int fps = 0;
    private volatile int circleCount = 0;
    private volatile int threadCount = 0;

    public Renderer(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
    }

    // called by main thread once per frame
    // swaps the snapshot reference (no deep copy here)
    public void updateSnapshot(double[][] newSnapshot,
                               int fps,
                               int circleCount,
                               int threadCount) {

        this.snapshot    = newSnapshot;
        this.fps         = fps;
        this.circleCount = circleCount;
        this.threadCount = threadCount;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        double[][] current = snapshot;

        for (double[] circle : current) {
            if (circle == null) continue;

            int x      = (int) circle[1];
            int y      = (int) circle[2];
            int radius = (int) circle[3];

            int r  = (int) circle[4];
            int gr = (int) circle[5];
            int b  = (int) circle[6];

            g.setColor(new Color(r, gr, b));
            g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        }

        // simple hud (top-left)
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));

        g.drawString("FPS: " + fps, 10, 20);
        g.drawString("Circles: " + circleCount, 10, 40);
        g.drawString("Threads: " + threadCount, 10, 60);
    }
}
