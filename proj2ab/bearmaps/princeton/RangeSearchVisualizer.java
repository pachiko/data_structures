package bearmaps.princeton;
/* ******************************************************************************
 *  Compilation:  javac-algs4 RangeSearchVisualizer.java
 *  Execution:    java-algs4 RangeSearchVisualizer input.txt
 *  Dependencies: PointST.java KdTreeST.java
 *
 *  Read points from a file (given as the command-line argument) and
 *  draw to standard draw. Also draw all of the points in the rectangle
 *  the user selects by dragging the mouse.
 *
 *  The range search results using the brute-force algorithm are drawn
 *  in red; the results using the kd-tree algorithms are drawn in blue.
 *
 **************************************************************************** */

import bearmaps.KDTree;
import bearmaps.NaivePointSet;
import bearmaps.Point;
import bearmaps.Rect;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class RangeSearchVisualizer {

    public static void main(String[] args) {

        // initialize the data structures with n points from file
        String filename = args[0];
        In in = new In(filename);

        ArrayList<Point> pts = new ArrayList<>();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point p = new Point(x, y);
            pts.add(p);
        }
//        NaivePointSet brute = new NaivePointSet(pts);
        KDTree kdtree = new KDTree(pts);

        // the rectangle drawn by the user
        double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
        double x1 = 0.0, y1 = 0.0;      // current location of mouse
        boolean isDragging = false;     // is the user dragging a rectangle

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point p : pts) p.draw();

        // process query rectangle drawn by user
        StdDraw.enableDoubleBuffering();
        while (true) {

            // user starts to drag a rectangle
            if (StdDraw.mousePressed() && !isDragging) {
                x0 = x1 = StdDraw.mouseX();
                y0 = y1 = StdDraw.mouseY();
                isDragging = true;
            }

            // user is dragging a rectangle
            else if (StdDraw.mousePressed() && isDragging) {
                x1 = StdDraw.mouseX();
                y1 = StdDraw.mouseY();
            }

            // user stops dragging the rectangle
            else if (!StdDraw.mousePressed() && isDragging) {
                isDragging = false;
            }


            // draw the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            for (Point p : pts) p.draw();

            // draw the rectangle
            Rect rect = new Rect(Math.min(x0, x1), Math.min(y0, y1),
                                     Math.max(x0, x1), Math.max(y0, y1));
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius();
            rect.draw();

            // draw the range search results for brute-force data structure in red
//            StdDraw.setPenRadius(0.03);
//            StdDraw.setPenColor(StdDraw.RED);
//            for (Point p : brute.range(rect)) p.draw();

            // draw the range search results for kd-tree in blue
            StdDraw.setPenRadius(0.02);
            StdDraw.setPenColor(StdDraw.BLUE);
            for (Point p : kdtree.range(rect)) p.draw();

            // display everything on screen
            StdDraw.show();
            StdDraw.pause(20);
        }
    }
}
