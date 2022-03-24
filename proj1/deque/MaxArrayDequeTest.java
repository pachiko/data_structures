package deque;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;

public class MaxArrayDequeTest {
    private class IntComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

    private class StringComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

    private class DumbStringComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return -1;
        }
        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

    @Test
    public void testMaxInt() {
        MaxArrayDeque<Integer> dq = new MaxArrayDeque<>(new IntComparator());
        for (int i = 0; i < 5; i++) {
            dq.addLast(i);
        }
        assertEquals((Integer) dq.max(), (Integer) 4);
    }

    @Test
    public void testMaxString() {
        MaxArrayDeque<String> dq = new MaxArrayDeque<>(new DumbStringComparator());

        dq.addLast("abc");
        dq.addLast("def");
        dq.addLast("zzzz");

        assertEquals((String) dq.max(new StringComparator()), (String) "zzzz");
    }
}
