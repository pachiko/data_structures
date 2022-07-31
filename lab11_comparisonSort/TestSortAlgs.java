import edu.princeton.cs.algs4.Queue;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestSortAlgs {

    @Test
    public void testMergeSort() {
        Queue<Integer> q = new Queue<>();
        int[] arr = {32, 15, 2, 17, 19, 26, 41, 17, 17};
        for (int i : arr) q.enqueue(i);
        Queue<Integer> sortedQ = MergeSort.mergeSort(q);
        assertEquals(isSorted(sortedQ), true);
    }

    @Test
    public void testQuickSort() {
        Queue<Integer> q = new Queue<>();
        int[] arr = {32, 15, 2, 17, 19, 26, 41, 17, 17};
        for (int i : arr) q.enqueue(i);
        Queue<Integer> sortedQ = QuickSort.quickSort(q);
        assertEquals(isSorted(sortedQ), true);
    }

    /**
     * Returns whether a Queue is sorted or not.
     *
     * @param items  A Queue of items
     * @return       true/false - whether "items" is sorted
     */
    private <Item extends Comparable> boolean isSorted(Queue<Item> items) {
        if (items.size() <= 1) {
            return true;
        }
        Item curr = items.dequeue();
        Item prev;
        while (!items.isEmpty()) {
            prev = curr;
            curr = items.dequeue();
            if (curr.compareTo(prev) < 0) {
                return false;
            }
        }
        return true;
    }
}
