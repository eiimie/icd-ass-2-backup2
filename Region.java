// Region.java
// represents a rectangular section of the simulation space.
// each worker owns exactly one region.

// layouts depend on worker count (keep simple):
// 1  → full screen
// 2  → split vertically
// 4  → 2x2 grid
// 8  → 4x2 grid

// boundary margin used to flag circles near edges so the main thread
// can handle cross-region collisions

public class Region {

    public final int index;

    public final double x1, y1;
    public final double x2, y2;

    public static final double BOUNDARY_MARGIN = 20.0;

    public Region(int index, double x1, double y1, double x2, double y2) {
        this.index = index;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    // simple bounds check
    public boolean contains(double x, double y) {
        return x >= x1 && x < x2 &&
               y >= y1 && y < y2;
    }

    // checks if a circle is close to any *internal* edge of the region
    // outer screen edges are ignored (walls deal with those)
    public boolean isNearBoundary(double x, double y, int radius,
                                 int screenW, int screenH) {

        return (x - x1 < BOUNDARY_MARGIN + radius && x1 > 0) ||
               (x2 - x < BOUNDARY_MARGIN + radius && x2 < screenW) ||
               (y - y1 < BOUNDARY_MARGIN + radius && y1 > 0) ||
               (y2 - y < BOUNDARY_MARGIN + radius && y2 < screenH);
    }

    // find which region a point belongs to
    public static int findRegion(double x, double y, Region[] regions) {
        for (Region r : regions) {
            if (r.contains(x, y)) {
                return r.index;
            }
        }
        return 0; // fallback (shouldn't really happen)
    }

    // builds regions based on worker count
    public static Region[] buildGrid(int screenW, int screenH, int numWorkers) {

        if (numWorkers == 1) {
            return new Region[] {
                new Region(0, 0, 0, screenW, screenH)
            };
        }

        if (numWorkers == 2) {
            double midX = screenW / 2.0;

            return new Region[] {
                new Region(0, 0,    0, midX,    screenH),
                new Region(1, midX, 0, screenW, screenH)
            };
        }

        if (numWorkers == 8) {
            double midY = screenH / 2.0;
            double col  = screenW / 4.0;

            return new Region[] {
                new Region(0, 0,       0,    col,     midY),
                new Region(1, col,     0,    col*2,   midY),
                new Region(2, col*2,   0,    col*3,   midY),
                new Region(3, col*3,   0,    screenW, midY),

                new Region(4, 0,       midY, col,     screenH),
                new Region(5, col,     midY, col*2,   screenH),
                new Region(6, col*2,   midY, col*3,   screenH),
                new Region(7, col*3,   midY, screenW, screenH)
            };
        }

        // default: 2x2 (covers 4 workers cleanly)
        double midX = screenW / 2.0;
        double midY = screenH / 2.0;

        return new Region[] {
            new Region(0, 0,    0,    midX,    midY),
            new Region(1, midX, 0,    screenW, midY),
            new Region(2, 0,    midY, midX,    screenH),
            new Region(3, midX, midY, screenW, screenH)
        };
    }
}
