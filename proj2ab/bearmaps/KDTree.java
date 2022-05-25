package bearmaps;

import java.util.*;

public class KDTree implements PointSet, Iterable<Point> {
    KDTreeNode root;

    public KDTree(List<Point> points) {
        Partition part = Partition.X;
        for (Point pt: points) {
            insert(pt, part);
            part = (part == Partition.X)? Partition.Y : Partition.X;
        }
    }

    public void insert(Point pt, Partition part) {
        KDTreeNode n = new KDTreeNode(pt, part);
        if (root != null) {
            KDTreeNode parent = root;
            while (true) {
                KDTreeNode next = (parent.compareTo(n.p) > 0)? parent.insertLesser(n) :
                        parent.insertGreater(n);
                if (next == n) break;
                parent = next;
            }
        } else {
            root = n;
            root.bound = new Rect(-Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        }
    }

    public Point nearest(double x, double y) {
        Point query = new Point(x, y);
        KDTreeNode best = root;
        KDTreeNode n = best;
        best = nearest(n, query, best);
        return best.p;
    }

    private KDTreeNode nearest(KDTreeNode n, Point query, KDTreeNode best) {
        if (Point.distance(n.p, query) < Point.distance(best.p, query)) best = n;

        KDTreeNode better = (n.compareTo(query) < 0)? n.greater : n.lesser;
        KDTreeNode worse = (better == n.greater)? n.lesser : n.greater;

        if (better != null) best = nearest(better, query, best);

        if (worse != null && !pruneSubTree(worse, query, best)) {
            best = nearest(worse, query, best);
        }

        return best;
    }

    private boolean pruneSubTree(KDTreeNode n, Point query, KDTreeNode best) {
        return Point.distance(best.p, query) <= n.bound.distance(query);
    }

    public List<Point> range(Rect r) {
        ArrayList<Point> res = new ArrayList<>();
        range(r, root, res);
        return res;
    }

    private void range(Rect r, KDTreeNode n, List<Point> res) {
        if (n == null) return;
        if (r.intersects(n.bound)) {
            if (r.contains(n.p)) {
                res.add(n.p);
            }
            range(r, n.lesser, res);
            range(r, n.greater, res);
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new KDTreeIterator(root);
    }


    // Level-Order Traversal
    private class KDTreeIterator implements Iterator {
        Queue<KDTreeNode> queue;

        public KDTreeIterator(KDTreeNode n) {
            queue = new LinkedList<>();
            queue.add(n);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public Object next() {
            KDTreeNode n = queue.remove();
            if (n.lesser != null) queue.add(n.lesser);
            if (n.greater != null) queue.add(n.greater);
            return n.p;
        }
    }


    private class KDTreeNode implements Comparable<Point> {
        Point p;
        Rect bound;
        KDTreeNode lesser;
        KDTreeNode greater;
        Partition part;

        public KDTreeNode(Point p, Partition part) {
            this.p = p;
            this.part = part;
        }

        public KDTreeNode insertLesser(KDTreeNode n) {
            if (lesser == null) {
                lesser = n;
                lesser.bound = (part == Partition.X)? new Rect(bound.xmin(), bound.ymin(), p.getX(), bound.ymax())
                        : new Rect(bound.xmin(), bound.ymin(), bound.xmax(), p.getY());
            }
            return lesser;
        }

        public KDTreeNode insertGreater(KDTreeNode n) {
            if (greater == null) {
                greater = n;
                greater.bound = (part == Partition.X)? new Rect(p.getX(), bound.ymin(), bound.xmax(), bound.ymax())
                        : new Rect(bound.xmin(), p.getY(), bound.xmax(), bound.ymax());
            }
            return greater;
        }

        @Override
        public int compareTo(Point o) {
            double diff = (part == Partition.X)? p.getX() - o.getX() : p.getY() - o.getY();
            if (diff > 0) return 1;
            else if (diff < 0) return -1;
            else return 0;
        }
    }

    enum Partition {
        X,
        Y,
    }
}
