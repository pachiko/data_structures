package gitlet;

import java.util.HashMap;
import java.util.LinkedList;


/**
 * Finds the split point between 2 branches
 *
 *  @author phill
 * */
public class SplitPointFinder {
    public String mainSHA;
    public String mergeSHA;
    public Commit main;
    public Commit merge;

    /** Visited map: SHA -> distance from main */
    private HashMap<String, Integer> visited;
    /** Queue of commits */
    private LinkedList<String> queue;


    /** Constructor */
    public SplitPointFinder(String mainSHA, String mergeSHA, Commit main, Commit merge) {
        this.mainSHA = mainSHA;
        this.mergeSHA = mergeSHA;
        if (main == null) main = Commit.read(mainSHA);
        this.main = main;
        if (merge == null) merge = Commit.read(mergeSHA);
        this.merge = merge;
    }


    /** Find latest common ancestor*/
    public Commit findSplitPoint() {
        visited = new HashMap<>();
        queue = new LinkedList<>();
        visitMain();
        return findAncestor();
    }


    /** Visit the DAG of main branch */
    private void visitMain() {
        queue.add(mainSHA);
        visited.put(mainSHA, 0);
        Commit c = main;

        while(!queue.isEmpty()) {
            String sha = queue.remove();
            if (mergeSHA.equals(sha)) {
                System.out.println("Given branch is an ancestor of the current branch.");
                System.exit(0);
            }
            if (c == null) c = Commit.read(sha);

            int dist = visited.get(sha) + 1;
            visit(c.getParent(), dist);
            visit(c.getMergeParent(), dist);

            c = null;
        }
    }


    /** Visit a commit on main's DAG */
    private void visit(String sha, int dist) {
        if (sha != null && !visited.containsKey(sha)) {
            visited.put(sha, dist);
            queue.add(sha);
        }
    }


    /** Find ancestor starting from merge*/
    private Commit findAncestor() {
        queue.add(mergeSHA);
        Commit c = merge;

        while(!queue.isEmpty()) {
            String sha = queue.remove();
            if (c == null) c = Commit.read(sha);

            String p = c.getParent();
            String mp = c.getMergeParent();

            boolean parentVisited = visited.containsKey(p);
            boolean mergeVisited = visited.containsKey(mp);

            String ancestor = null;
            if (parentVisited && mergeVisited) {
                ancestor = (visited.get(p) > visited.get(mp))? p : mp;
            } else if (parentVisited) {
                ancestor = p;
            } else if (mergeVisited) {
                ancestor = mp;
            }

            fastForward(ancestor);
            if (ancestor != null) return Commit.read(ancestor);

            c = null;
        }

        System.out.println("No Ancestor!");
        System.exit(0);
        return null;
    }


    /** Fast-forward if main SHA is ancestor */
    private void fastForward(String ancestor) {
        if (mainSHA.equals(ancestor)) {
            merge.updateCWD();
            BranchManager.overwriteHEAD(mergeSHA);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
    }
}
