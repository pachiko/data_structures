package bearmaps;

import edu.princeton.cs.algs4.StdDraw;

import java.util.Objects;

public class Rect {
    private double xmin;
    private double xmax;
    private double ymin;
    private double ymax;

    public Rect(double xmin, double ymin, double xmax, double ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }

    public Rect(Point p1, Point p2) {
        this.xmin = p1.getX();
        this.xmax = p2.getX();
        this.ymin = p1.getY();
        this.ymax = p2.getY();
    }

    public double xmin() { return xmin; }
    public double xmax() { return xmax; }
    public double ymin() { return ymin; }
    public double ymax() { return ymax; }

    public boolean contains(Point p) {
        return p.getX() >= xmin && p.getX() <= xmax && p.getY() >= ymin && p.getY() <= ymax;
    }

    public boolean intersects(Rect r) {
        return r.xmin <= xmax && r.xmax >= xmin && r.ymin <= ymax && r.ymax >= ymin;
    }

    public double distance(Point p) {
        // See Princeton Algs4
        double dx = 0;
        double dy = 0;

        if (p.getX() < xmin) dx = p.getX() - xmin;
        else if (p.getX() > xmax) dx = p.getX() - xmax;

        if (p.getY() < ymin) dy = p.getY() - ymin;
        else if (p.getY() > ymax) dy = p.getY() - ymax;

        return dx*dx + dy*dy;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Rect otherRect = (Rect) other;
        return Double.compare(otherRect.xmin, xmin) == 0 &&
                Double.compare(otherRect.xmax, xmax) == 0 &&
                Double.compare(otherRect.ymin, ymin) == 0 &&
                Double.compare(otherRect.ymax, ymax) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xmin, xmax, ymin, ymax);
    }

    public void draw() {
        StdDraw.line(xmin, ymin, xmin, ymax);
        StdDraw.line(xmax, ymin, xmax, ymax);
        StdDraw.line(xmin, ymin, xmax, ymin);
        StdDraw.line(xmin, ymax, xmax, ymax);
    }
}
