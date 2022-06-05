package bearmaps.proj2ab;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T>, Iterable<T> {
    Node[] items;
    private int size;
    private HashMap<T, Integer> indexMap;
    private int minCap = 8;

    private double minLoadFactor = 0.25;
    private double maxLoadFactor = 0.75;

    public ArrayHeapMinPQ() {
        items = new ArrayHeapMinPQ.Node[minCap];
        indexMap = new HashMap<>();
        size = 0;
    }

    private void resize(boolean up) {
        int newSize = up? items.length*2 : items.length/2;
        Node[] newItems = new ArrayHeapMinPQ.Node[newSize];
        System.arraycopy(items, 1, newItems, 1, size);
        items = newItems;
    }

    private int parent(int i) {
        return i/2;
    }

    private int left(int i) {
        return 2*i;
    }

    private int right(int i) {
        return 2*i + 1;
    }

    private boolean invalidIdx(int i) { return i <= 0 || i > size;}

    private void sink(int i) {
        while (!invalidIdx(i)) {
            int left = left(i);
            int right = right(i);
            int minIdx;

            boolean badLeft = invalidIdx(left);
            boolean badRight = invalidIdx(right);

            if (badLeft && badRight) break;
            else if (badRight) minIdx = left; // Binary heaps are complete trees
            else minIdx = (items[left].compareTo(items[right]) < 0) ? left : right;

            if (items[minIdx].compareTo(items[i]) < 0) {
                swap(minIdx, i);
                i = minIdx;
            }
            else break;
        }
    }

    private void swim(int i) {
        while (!invalidIdx(i)) {
            int parent = parent(i);
            if (invalidIdx(parent)) break;

            if (items[parent].compareTo(items[i]) > 0) {
                swap(parent, i);
                i = parent;
            }
            else break;
        }
    }

    private void swap(int i, int j) {
        Node ni = items[i];
        Node nj = items[j];
        items[j] = ni;
        items[i] = nj;
        indexMap.put(ni.item, j);
        indexMap.put(nj.item, i);
    }

    @Override
    public void add(T item, double priority) {
        if (indexMap.containsKey(item)) throw new IllegalArgumentException();

        Node n = new Node(item, priority);
        if ((size + 1)/((double) items.length) > maxLoadFactor) resize(true);
        items[size + 1] = n;
        size++;

        indexMap.put(item, size);

        swim(size);
    }

    @Override
    public boolean contains(T item) {
        return indexMap.containsKey(item);
    }

    @Override
    public T getSmallest() {
        if (size == 0) throw new NoSuchElementException();
        return items[1].item;
    }

    @Override
    public T removeSmallest() {
        if (size == 0) throw new NoSuchElementException();
        if ((size - 1)/((double) items.length) < minLoadFactor && items.length > minCap) resize(false);

        Node n = items[1];

        items[1] = items[size];
        indexMap.put(items[1].item, 1);

        items[size] = null;
        size--;
        indexMap.remove(n.item);

        sink(1);
        return n.item;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void changePriority(T item, double priority) {
       if (indexMap.containsKey(item)) {
           int idx = indexMap.get(item);
           Node n = items[idx];
           if (n.priority == priority) return;

           boolean sink = n.priority < priority;
           n.priority = priority;

           if (sink) sink(idx);
           else swim(idx);
       } else {
           throw new NoSuchElementException();
       }
    }

    @Override
    public Iterator<T> iterator() {
        return new PQIterator(items, size);
    }

    // Level-Order Traversal
    private class PQIterator implements Iterator {
        Node[] items;
        int size;
        int idx;

        public PQIterator(Node[] items, int size) {
            this.items = items;
            this.size = size;
            this.idx = 1;
        }

        @Override
        public boolean hasNext() {
            return idx <= size;
        }

        @Override
        public Object next() {
            return items[idx++].item;
        }
    }

    class Node implements Comparable<Node> {
        T item;
        double priority;

        public Node(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }

        @Override
        public int compareTo(Node o) {
            return Double.compare(priority, o.priority);
        }
    }

    
    @Test
    public void testIndexing() {
        assertEquals(6, left(3));
        assertEquals(10, left(5));
        assertEquals(7, right(3));
        assertEquals(11, right(5));

        assertEquals(3, parent(6));
        assertEquals(5, parent(10));
        assertEquals(3, parent(7));
        assertEquals(5, parent(11));
    }

    @Test
    public void testSwim() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        for (int i = 1; i <= 7; i += 1) {
            pq.add("x"+i, i);
        }
        // Change item x6's priority to a low value.
        pq.changePriority("x6", 0);

        try {
            pq.changePriority("wtf", 69);
        }
        catch (NoSuchElementException e) {}
        assertEquals("x6", pq.items[1].item);
        assertEquals("x2", pq.items[2].item);
        assertEquals("x1", pq.items[3].item);
        assertEquals("x4", pq.items[4].item);
        assertEquals("x5", pq.items[5].item);
        assertEquals("x3", pq.items[6].item);
        assertEquals("x7", pq.items[7].item);
    }

    @Test
    public void testSink() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        for (int i = 1; i <= 7; i += 1) {
            pq.add("x" + i, i);
        }
        // Change root's priority to a large value.
        pq.changePriority("x1", 10);
        
        assertEquals("x2", pq.items[1].item);
        assertEquals("x4", pq.items[2].item);
        assertEquals("x3", pq.items[3].item);
        assertEquals("x1", pq.items[4].item);
        assertEquals("x5", pq.items[5].item);
        assertEquals("x6", pq.items[6].item);
        assertEquals("x7", pq.items[7].item);
    }


    @Test
    public void testInsert() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        pq.add("c", 3);
        assertEquals("c", pq.items[1].item);

        pq.add("i", 9);
        assertEquals("i", pq.items[2].item);

        pq.add("g", 7);
        pq.add("d", 4);
        assertEquals("d", pq.items[2].item);

        pq.add("a", 1);
        assertEquals("a", pq.items[1].item);

        pq.add("h", 8);
        pq.add("e", 5);
        pq.add("b", 2);
        try {
            pq.add("c", 3);
        } catch (IllegalArgumentException e) {}
        try {
            pq.add("d", 4);
        } catch (IllegalArgumentException e) {}
        assertEquals(8, pq.size());
        assertEquals("a", pq.items[1].item);
        assertEquals("b", pq.items[2].item);
        assertEquals("e", pq.items[3].item);
        assertEquals("c", pq.items[4].item);
        assertEquals("d", pq.items[5].item);
        assertEquals("h", pq.items[6].item);
        assertEquals("g", pq.items[7].item);
        assertEquals("i", pq.items[8].item);
    }


    @Test
    public void testaddAndRemoveOnce() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        pq.add("c", 3);
        pq.add("i", 9);
        pq.add("g", 7);
        pq.add("d", 4);
        pq.add("a", 1);
        pq.add("h", 8);
        pq.add("e", 5);
        pq.add("b", 2);
        try {
            pq.add("c", 3);
        } catch (IllegalArgumentException e) {}
        try {
            pq.add("d", 4);
        } catch (IllegalArgumentException e) {}
        String removed = pq.removeSmallest();
        assertEquals("a", removed);
        assertEquals(7, pq.size());
        assertEquals("b", pq.items[1].item);
        assertEquals("c", pq.items[2].item);
        assertEquals("e", pq.items[3].item);
        assertEquals("i", pq.items[4].item);
        assertEquals("d", pq.items[5].item);
        assertEquals("h", pq.items[6].item);
        assertEquals("g", pq.items[7].item);
    }

    @Test
    public void testaddAndRemoveAllButLast() {
        ExtrinsicMinPQ<String> pq = new ArrayHeapMinPQ<>();
        pq.add("c", 3);
        pq.add("i", 9);
        pq.add("g", 7);
        pq.add("d", 4);
        pq.add("a", 1);
        pq.add("h", 8);
        pq.add("e", 5);
        pq.add("b", 2);
        try {
            pq.add("c", 3);
        } catch (IllegalArgumentException e) {}
        try {
            pq.add("d", 4);
        } catch (IllegalArgumentException e) {}

        int i = 0;
        String[] expected = {"a", "b", "c", "d", "e", "g", "h", "i"};
        while (pq.size() > 1) {
            assertEquals(expected[i], pq.removeSmallest());
            i += 1;
        }
    }
}
