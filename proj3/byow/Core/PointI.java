package byow.Core;

import java.io.Serializable;
import java.util.Objects;

/**
 *  @source Princeton Algs4
 */
public class PointI implements Serializable {
    private int x;
    private int y;

    public PointI(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PointI(PointI p) {
        this.x = p.x;
        this.y = p.y;
    }

    public int getX() { return x; }

    public int getY() { return y; }

    public void setX(int x) { this.x = x; }

    public void setY(int y) { this.y = y; }

    /**
     * Returns the Manhattan distance
     */
    private static int distance(int x1, int x2, int y1, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    /**
     * Returns the euclidean distance (L2 norm) squared between two points.
     * Note: This is the square of the Euclidean distance, i.e.
     * there's no square root.
     */
    public static int distance(PointI p1, PointI p2) {
        return distance(p1.getX(), p2.getX(), p1.getY(), p2.getY());
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        PointI otherPoint = (PointI) other;
        return getX() == otherPoint.getX() && getY() == otherPoint.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("PointI x: %d, y: %d", x, y);
    }
}
