/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    /** Have found a cycle? */
    boolean cycle = false;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        dfs(0, 0);
    }

    /** Depth first search to find cycle.
     * Input: v = current vertex to visit
     *        p = parent vertex of current vertex (1 DFS call before the current)
     * Output: cycle point (if > 0)
     * */
    public int dfs(int v, int p) {
        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            boolean visited = marked[w];

            if (visited) {
                if (w != p) { // Found a cycle if adjacent is marked and it is not the parent
                    cycle = true;
                    edgeTo[w] = v;
                    announce();
                    return w;
                }
                continue; // Prevent ping-pong if visited parent
            }

            int c = dfs(w, v);
            if (c >= 0) { // Need to draw the cycle
                edgeTo[w] = v;
                announce();
                return (c != v)? c : -1; // Stop drawing if reached the cycle the second time
            }
            if (cycle) return c; // Stop searching for cycles after finished drawing
        }

        return -1;
    }
}

