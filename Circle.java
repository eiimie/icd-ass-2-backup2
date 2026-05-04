// Circle.java
import java.awt.Color;

public class Circle {

    public int    id;
    public double x, y;
    public double dx, dy;
    public int    radius;
    public int    r, g, b;  // stored as ints for string serialisation

    public Circle(int id, double x, double y, double dx, double dy,
                  int radius, int r, int g, int b) {
        this.id     = id;
        this.x      = x;
        this.y      = y;
        this.dx     = dx;
        this.dy     = dy;
        this.radius = radius;
        this.r      = r;
        this.g      = g;
        this.b      = b;
    }

    // returns a colour object for rendering, not passed between threads
    public Color toColor() {
        return new Color(r, g, b);
    }
}
