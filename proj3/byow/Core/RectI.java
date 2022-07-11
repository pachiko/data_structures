package byow.Core;

import java.util.Objects;

/**
 *  @source Princeton Algs4
 */
public class RectI {
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;

    public RectI(int xmin, int ymin, int xmax, int ymax) {
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }

    public RectI(PointI p1, int w, int h) {
        this.xmin = p1.getX();
        this.ymin = p1.getY();
        this.xmax = xmin + w - 1;
        this.ymax = ymin + h - 1;
    }

    public RectI(PointI p1, PointI p2) {
        this.xmin = p1.getX();
        this.ymin = p1.getY();
        this.xmax = p2.getX();
        this.ymax = p2.getY();
    }

    public int xmin() { return xmin; }
    public int setXmin(int v) { return xmin = v; }
    public int ymin() { return ymin; }
    public int setYmin(int v) { return ymin = v; }
    public int xmax() { return xmax; }
    public int setXmax(int v) { return xmax = v; }
    public int ymax() { return ymax; }
    public int setYmax(int v) { return ymax = v; }

    public int w() { return xmax - xmin + 1; }
    public int h() { return ymax - ymin + 1; }

    public boolean contains(PointI p) {
        return p.getX() >= xmin && p.getX() <= xmax && p.getY() >= ymin && p.getY() <= ymax;
    }

    public boolean intersects(RectI r) {
        return r.xmin <= xmax && r.xmax >= xmin && r.ymin <= ymax && r.ymax >= ymin;
    }

    public RectI overlap(RectI r) {
        if (!intersects(r)) return null;
        int x0 = Math.max(xmin, r.xmin);
        int y0 = Math.max(ymin, r.ymin);
        int x1 = Math.min(xmax, r.xmax);
        int y1 = Math.min(ymax, r.ymax);
        return new RectI(x0, y0, x1, y1);
    }

    public RectI shrunk(int i) {
        return new RectI(xmin + i, ymin + i, xmax - i, ymax - i);
    }

    /**
     * Returns the Manhattan distance
     */
    public int distance(PointI p) {
        // See Princeton Algs4
        int dx = 0;
        int dy = 0;

        if (p.getX() < xmin) dx = p.getX() - xmin;
        else if (p.getX() > xmax) dx = p.getX() - xmax;

        if (p.getY() < ymin) dy = p.getY() - ymin;
        else if (p.getY() > ymax) dy = p.getY() - ymax;

        return Math.abs(dx) + Math.abs(dy);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        RectI otherRect = (RectI) other;
        return xmin == otherRect.xmin && ymin == otherRect.ymin && xmax == otherRect.xmax && ymax == otherRect.ymax;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xmin, xmax, ymin, ymax);
    }

    @Override
    public String toString() {
        return String.format("RectI: start(%d, %d), size(%d, %d)", xmin, ymin, w(), h());
    }
}
