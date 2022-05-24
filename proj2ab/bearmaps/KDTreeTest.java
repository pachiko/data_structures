package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KDTreeTest {
    private ArrayList<Point> testPoints() {
        ArrayList<Point> testPoints = new ArrayList<>();
        testPoints.add(new Point(2,3)); // A
        testPoints.add(new Point(4,2)); // B
        testPoints.add(new Point(4,5)); // C
        testPoints.add(new Point(3,3)); // D
        testPoints.add(new Point(1,5)); // E
        testPoints.add(new Point(4,4)); // F
        return testPoints;
    }

    private ArrayList<Point> levelOrderPoints() {
        ArrayList<Point> expectedPoints = new ArrayList<>();
        expectedPoints.add(new Point(2,3)); // A
        expectedPoints.add(new Point(1,5)); // E
        expectedPoints.add(new Point(4,2)); // B
        expectedPoints.add(new Point(4,5)); // C
        expectedPoints.add(new Point(3,3)); // D
        expectedPoints.add(new Point(4,4)); // F
        return expectedPoints;
    }

    public static Point generateRandomPoint(Random rando) {
        boolean negX = rando.nextBoolean();
        double x = rando.nextDouble()*1000;
        if (negX) x = -x;

        boolean negY = rando.nextBoolean();
        double y = rando.nextDouble()*1000;
        if (negY) y = -y;

        return new Point(x, y);
    }

    public static ArrayList<Point> randomTestPoints(int n, int s) {
        ArrayList<Point> testPoints = new ArrayList<>();
        Random rando = new Random(s);
        for (int i = 0; i < n; i++) {
            testPoints.add(generateRandomPoint(rando));
        }
        return testPoints;
    }

    @Test
    public void testInsert() {
        KDTree tree = new KDTree(testPoints());
        ArrayList<Point> levelOrder = levelOrderPoints();
        int count = 0;
        for (Point p: tree) { // A, E, B, C, D, F
            assertEquals(p, levelOrder.get(count));
            count++;
        }
    }

    @Test
    public void testNearest() {
        KDTree tree = new KDTree(testPoints());
        Point nearest = tree.nearest(0, 7);
        assertEquals(nearest.getX(), 1, 0.00000001);
        assertEquals(nearest.getY(), 5, 0.00000001);

        nearest = tree.nearest(3, 2);
        assertEquals(nearest.getX(), 4, 0.00000001);
        assertEquals(nearest.getY(), 2, 0.00000001);

        nearest = tree.nearest(3, 5);
        assertEquals(nearest.getX(), 4, 0.00000001);
        assertEquals(nearest.getY(), 5, 0.00000001);
    }

    @Test
    public void testRandomNearest() {
        ArrayList<Point> testPoints = randomTestPoints(1000, 6767);
        KDTree tree = new KDTree(testPoints);
        NaivePointSet noob = new NaivePointSet(testPoints);

        Random rando = new Random(69420);
        for (int i = 0; i < 100000; i++) {
            Point query = generateRandomPoint(rando);
            double x = query.getX();
            double y = query.getY();
            Point nearestKD = tree.nearest(x, y);
            Point nearestNoob = noob.nearest(x, y);
            assertEquals(Point.distance(query, nearestKD), Point.distance(query, nearestNoob), 0.000001);
        }
    }

    @Test
    public void testKDTreeConstructionTiming() {
        int[] numPoints = {31250, 62500, 125000, 250000, 500000, 1000000, 2000000};
        System.out.println("KDTree Construction Timing");

        for (int i = 0; i < numPoints.length; i++) {
            int numPts = numPoints[i];
            ArrayList<Point> pts = randomTestPoints(numPts, 11249);
            Stopwatch s = new Stopwatch();
            new KDTree(pts);
            double t = s.elapsedTime();
            System.out.println("Number of points: " + numPts + "; Time (s): " + t + "; microsec/op: " + t/numPts*1e6);
        }
    }

    @Test
    public void noobNearestTiming() {
        nearestTiming(true);
    }

    @Test
    public void kdTreeNearestTiming() {
        nearestTiming(false);
    }

    private void nearestTiming(boolean noob) {
        int[] numPoints = noob? new int[]{125, 250, 500, 1000} :
                new int[]{31250, 62500, 125000, 250000, 500000, 1000000};

        System.out.println((noob? "NaivePointSet" : "KDTree") + " Nearest Timing (1M Points)");

        Random rando = new Random(123789);
        for (int i = 0; i < numPoints.length; i++) {
            int numPts = numPoints[i];
            ArrayList<Point> pts = randomTestPoints(numPts, 456333);
            PointSet ps = noob? new NaivePointSet(pts) : new KDTree(pts);
            Stopwatch s = new Stopwatch();
            testNearest(ps, 1000000, rando);
            double t = s.elapsedTime();
            System.out.println("Number of points: " + numPts + "; Time (s): " + t + "; microsec/op: " + t); // 1Mu/1M=1u
        }

    }

    private void testNearest(PointSet ps, int n, Random rando) {
        for (int i = 0; i < n; i++) {
            Point query = generateRandomPoint(rando);
            double x = query.getX();
            double y = query.getY();
            ps.nearest(x, y);
        }
    }
}
