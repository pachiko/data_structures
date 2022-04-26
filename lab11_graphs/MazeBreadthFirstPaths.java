import java.util.LinkedList;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Queue<Integer> queue;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        queue = new LinkedList<>(); // link last, remove first
        queue.add(s);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        while (!queue.isEmpty()) {
            int v = queue.remove(); // Dequeue means visited
            marked[v] = true; // So mark it

            announce(); // Update GUI
            if (v == t) { // Return if found
                targetFound = true;
            }
            if (targetFound) {
                return;
            }

            for (int n : maze.adj(v)) { // Else enqueue neighbors for visit
                if (marked[n]) continue; // Ignore visited
                distTo[n] = distTo[v] + 1; // Increment distance from source
                edgeTo[n] = v; // Offshoot from current node
                queue.add(n); // To be visited
            }
        }
    }


    @Override
    public void solve() {
         bfs();
    }
}

