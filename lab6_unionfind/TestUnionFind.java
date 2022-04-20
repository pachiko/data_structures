import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

/** Class to test UnionFind */
public class TestUnionFind {
    @Test
    /** Initializes UnionFind and tests */
    public void initTest() {
        int N = 7;
        UnionFind uf = new UnionFind(N);
        for (int i = 0; i < N; i++) {
            assertEquals(uf.parent(i), -1);
            assertEquals(uf.sizeOf(i), 1);
        }
    }

    @Test
    /** Connects all vertices to 1 root */
    public void connectToOneTest() {
        int N = 7;
        UnionFind uf = new UnionFind(N);

        int[] indices = IntStream.range(0, 7).toArray();
        StdRandom.shuffle(indices);

        int root = indices[0];
        for (int i = 0; i < N; i++) {
            uf.connect(i, root);
        }

        int j = 0;
        for (int i = 0; i < N; i++) {
            assertTrue(uf.isConnected(i, j));
            assertEquals(uf.sizeOf(i), 7);
            j = i;
        }
    }

    @Test
    /** Test the connectivity deterministically */
    public void testConnect() {
        int N = 7;
        UnionFind uf = new UnionFind(N);

        /** 1
         *  2  0
         *  A tall tree
         * */
        uf.connect(2, 1);
        uf.connect(0, 2);

        /**   4
         *  3  5  6
         *  A heavy tree
         * */
        uf.connect(3, 4);
        uf.connect(5, 4);
        uf.connect(6, 4);

        assertFalse(uf.isConnected(2, 3));
        assertTrue(uf.isConnected(0, 2));
        assertTrue(uf.isConnected(3, 6));

        assertEquals(uf.parent(1), -3);
        assertEquals(uf.parent(4), -4);
        assertEquals(uf.parent(0), 1);
        assertEquals(uf.parent(2), 1);
        assertEquals(uf.parent(3), 4);
        assertEquals(uf.parent(5), 4);
        assertEquals(uf.parent(6), 4);

        uf.connect(5, 0);
        assertEquals(uf.parent(1), 4);
        assertEquals(uf.parent(4), -7);
        assertEquals(uf.parent(0), 1);
        assertEquals(uf.parent(2), 1);
        assertEquals(uf.parent(3), 4);
        assertEquals(uf.parent(5), 4);
        assertEquals(uf.parent(6), 4);
        assertTrue(uf.isConnected(1, 3));
    }
}
