import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Note that we don't use Djikstra's since it is the same as BFS.
 * There are no special weights on the edges.
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Performs an A star search from vertex s. */
    private void astar() {
        PriorityQueue<PQItem> pq = new PriorityQueue<>(new PQItemComparator());
        pq.add(new PQItem(s, 0));

        while (!pq.isEmpty()) {
            PQItem minItem = pq.poll();
            marked[minItem.v] = true;

            announce();
            if (minItem.v == t) {
                targetFound = true;
                return;
            }

            for (int n : maze.adj(minItem.v)) {
                if (marked[n]) continue;
                distTo[n] = distTo[minItem.v] + 1;
                edgeTo[n] = minItem.v;
                pq.add(new PQItem(n, distTo[n] + h(n)));
            }
        }
    }

    @Override
    public void solve() {
        astar();
    }

    /**
     * Priority Queue Item, containing a vertex index and the score (+ heuristic)
     */
    private class PQItem {
        int v;
        int score;

        PQItem(int v, int score) {
            this.v = v;
            this.score = score;
        }
    }

    /**
     * Priority Queue Item comparator, compares score only
     */
    private class PQItemComparator implements  Comparator<PQItem> {
        @Override
        public int compare(PQItem o1, PQItem o2) {
            return o1.score - o2.score;
        }
    }
}

