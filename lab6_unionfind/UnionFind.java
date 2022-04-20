/** Weighted Quick Union */
public class UnionFind {
    /** Array of parent indices. Root nodes have values < 0,
     *  where the magnitude is the number of nodes in the tree/set (weight) */
    int[] parent;

    /** Creates a UnionFind data structure holding n vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int n) {
        parent = new int[n];
        // set all the parents to be -1 to symbolize that they are disjoint
        for (int i = 0; i < n; i++) {
            parent[i] = -1;
        }
    }

    /** Throws an exception if v1 is not a valid vertex. */
    private void validate(int v1) {
        if (v1 < 0 || v1 >= parent.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /** Returns the parent of v1. If v1 is the root of a tree,
     *  returns the negative size of the tree for which v1 is the root.
     *  (Looks like its meant for debugging) */
    public int parent(int v1) {
        validate(v1);
        return parent[v1];
    }

    /** Returns the size of the set v1 belongs to.
     * (Looks like its meant for debugging) */
    public int sizeOf(int v1) {
        int root = find(v1);
        return -1 * parent[root];
    }

    /** Returns true if nodes v1 and v2 are connected. */
    public boolean isConnected(int v1, int v2) {
        return find(v1) == find(v2);
    }

    /** Connects two elements v1 and v2 together. v1 and v2 can be any valid
       elements, and a union-by-size heuristic is used. If the sizes of the sets
       are equal, tie break by connecting v1's root to v2's root. Connecting a
       vertex with itself or vertices that are already connected should not 
       change the sets but may alter the internal structure of the data. (Due to path compression?) */
    public void connect(int v1, int v2) {
        int root1 = find(v1);
        int root2 = find(v2);

        if (root1 == root2) return;

        int size1 = -1*parent[root1];
        int size2 = -1*parent[root2];

        if (size1 > size2) {
            parent[root2] = root1;
            parent[root1] -= size2;
        } else { // set1 is smaller or equal to set2, so merge set1 into set2
            parent[root1] = root2;
            parent[root2] -= size1;
        }
    }

    /** Returns the root of the set v1 belongs to. */
    private int find(int v1) {
        validate(v1);
        int i = parent[v1];
        while (i >= 0) {
            v1 = i;
            i = parent[v1];
        }
        return v1;
    }
}