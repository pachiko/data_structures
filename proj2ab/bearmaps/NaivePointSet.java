package bearmaps;

import java.util.List;

public class NaivePointSet implements PointSet {
    public List<Point> pointSet; // not really a set lol

    public NaivePointSet(List<Point> points) {
        pointSet = points;
    }

    public Point nearest(double x, double y) {
        Point res = null;
        double nearest = Double.MAX_VALUE;

        Point query = new Point(x, y);
        for (Point p: pointSet) { // theta(N)
            double dist = Point.distance(query, p);
            if (dist < nearest) {
                nearest = dist;
                res = p;
            }
        }
        return res;
    }
}
