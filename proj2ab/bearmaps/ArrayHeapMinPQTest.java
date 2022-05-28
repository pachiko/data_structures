package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ArrayHeapMinPQTest {
    int[] numAdds = {1000, 5000, 10000, 50000, 100000, 500000, 1000000};

    @Test
    public void randomCorrectnessTest() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        NaiveMinPQ<String> noob = new NaiveMinPQ<>();

        Random rando = new Random(69420);
        for (int i = 0; i < 100000; i++) {
            int n = rando.nextInt(3);
            switch (n) {
                case 0: // add
                    pq.add("test_" + i, i);
                    noob.add("test_" + i, i);
                    assertEquals(pq.size(), noob.size());
                    assertEquals(pq.getSmallest(), noob.getSmallest());
                    break;
                case 1: // removeSmallest
                    try {
                        String pqItem = pq.removeSmallest();
                        String noobItem = noob.removeSmallest();
                        assertEquals(pqItem, noobItem);
                        assertEquals(pq.size(), noob.size());
                    } catch (NoSuchElementException e) {}
                    break;
                default: // changePriority
                    try {
                        int idx = rando.nextInt(pq.size() - 1) + 1;
                        double newPriority = rando.nextInt(pq.size() - 1) + 1 + rando.nextDouble(); // prevent ties
                        String item = pq.items[idx].item;

                        pq.changePriority(item, newPriority);
                        noob.changePriority(item, newPriority);

                        assertEquals(pq.getSmallest(), noob.getSmallest());
                        assertEquals(pq.size(), noob.size());
                    } catch (NoSuchElementException e) {}
                    catch (IllegalArgumentException e) {}
            }
        }
    }

    private ArrayHeapMinPQ<Integer> add(int adds) {
        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();
        Random rando = new Random();
        for (int j = 0; j < adds; j++) {
            pq.add(j, j + rando.nextDouble());
        }
        return pq;
    }

    @Test
    public void addTimingTest() {
        for (int adds : numAdds) {
            Stopwatch watch = new Stopwatch();
            ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();
            Random rando = new Random();
            for (int j = adds - 1; j > 0; j--) { // worst case, always swim
                pq.add(j, j + rando.nextDouble());
            }
            double time = watch.elapsedTime();
            System.out.println("N: " + adds + "; Time (s): " + time);
        }
    }

    @Test
    public void containsTimingTest() {
        for (int adds : numAdds) {
            ArrayHeapMinPQ<Integer> pq = add(adds);
            Stopwatch watch = new Stopwatch();
            for (int j = 0; j < adds; j++) {
                pq.contains(j);
            }
            double time = watch.elapsedTime();
            System.out.println("N: " + adds + "; Time for N contains (s): " + time);
        }
    }

    @Test
    public void removeSmallestTimingTest() {
        for (int adds : numAdds) {
            ArrayHeapMinPQ<Integer> pq = add(adds);
            Stopwatch watch = new Stopwatch();
            for (int j = 0; j < adds; j++) { // worst case, always sink
                pq.removeSmallest();
            }
            double time = watch.elapsedTime();
            System.out.println("N: " + adds + "; Time for N removeSmallest (s): " + time);
        }
    }

    @Test
    public void changePriorityTimingTest() {
        for (int adds : numAdds) {
            ArrayHeapMinPQ<Integer> pq = add(adds);
            Random rando = new Random();
            Stopwatch watch = new Stopwatch();
            for (int j = 0; j < adds; j++) {
                pq.changePriority(j, rando.nextDouble());
            }
            double time = watch.elapsedTime();
            System.out.println("N: " + adds + "; Time for N changePriority (s): " + time);
        }
    }
}
