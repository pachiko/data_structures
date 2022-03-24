package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.StdRandom;

/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        LinkedListDeque<String> lld1 = new LinkedListDeque<>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());

        lld1.addLast(25);
        assertFalse("lld1 should contain 1 item", lld1.isEmpty());

        lld1.removeLast();
        assertTrue("lld1 should be empty after removal", lld1.isEmpty());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        LinkedListDeque<String>  lld1 = new LinkedListDeque<>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

        assertEquals("Testing if string equals", s, "string");
        assertEquals("Testing if double equals", d, 3.14159, 1e-6);
        assertEquals("Testing if b equals", b, true);
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());

    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }

    }

    @Test
    /* Tests get() and getRecursive() */
    public void getTest() {
        LinkedListDeque<String> lld1 = new LinkedListDeque<>();
        assertNull("Should be null", lld1.get(69));
        assertNull("Should be null", lld1.get(-1));
        assertNull("Should be null", lld1.getRecursive(69));
        assertNull("Should be null", lld1.getRecursive(-1));

        assertTrue(lld1.isEmpty());

        lld1.addLast("Delta");
        lld1.addFirst("Charlie");
        lld1.addFirst("Bravo");
        lld1.addFirst("Alpha");
        lld1.removeFirst();
        assertFalse(lld1.isEmpty());

        for (int i = 0; i < lld1.size(); i++) {
            String g = lld1.get(i);
            String rg = lld1.getRecursive(i);
            assertEquals("Same elements", g, rg); // should be the same Object even
        }
        lld1.printDeque();
    }

    @Test
    /* Random add remove tests */
    public void randomizedAddRemoveTest() {
        LinkedListDeque<Integer> L = new LinkedListDeque<>();
        int N = 5000;

        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                Integer randVal = StdRandom.uniform(0, 100);
                int oldSize = L.size();
                L.addLast(randVal);
                assertEquals(L.size(), oldSize + 1);
                assertEquals(randVal, L.removeLast());
                L.addLast(randVal);
            } else if (operationNumber == 1) {
                // addFirst
                Integer randVal = StdRandom.uniform(0, 100);
                int oldSize = L.size();
                L.addFirst(randVal);
                assertEquals(L.size(), oldSize + 1);
                assertEquals(randVal, L.removeFirst());
                L.addFirst(randVal);
            }else if (operationNumber == 2) {
                if (!L.isEmpty()) {
                    int oldSize = L.size();
                    L.removeLast();
                    assertEquals(L.size(), oldSize - 1);
                } else {
                    L.removeLast();
                    assertEquals(L.size(), 0);
                }
            } else if (operationNumber == 3) {
                if (!L.isEmpty()) {
                    int oldSize = L.size();
                    L.removeFirst();
                    assertEquals(L.size(), oldSize - 1);
                } else {
                    L.removeFirst();
                    assertEquals(L.size(), 0);
                }
            }
        }
    }

    @Test
    /* Random get and getRecursive tests */
    public void randomizedGetTest() {
        LinkedListDeque<Integer> L = new LinkedListDeque<>();
        int N = 5000;

        Integer[] indices = new Integer[N];
        for (int i = 0; i < N; i++) {
            L.addLast(i);
            indices[i] = i;
        }
        StdRandom.shuffle(indices);

        for (int i = 0; i < N; i++) {
            Integer index = indices[i];
            assertEquals(L.get(index), L.getRecursive(index));
            assertEquals(L.get(index), index);
        }
    }

    @Test
    /* Test equals method */
    public void testEquals() {
        LinkedListDeque<Integer> A = new LinkedListDeque<>();
        LinkedListDeque<Integer> B = new LinkedListDeque<>();
        LinkedListDeque<Integer> C = new LinkedListDeque<>();
        LinkedListDeque<Float> D = new LinkedListDeque<>();
        ArrayDeque<Integer> E = new ArrayDeque<>();
        LinkedListDeque<Integer> F = new LinkedListDeque<>();

        int N = 5;
        int start = 6;
        for (int i = start; i < N + start; i++) {
            A.addLast(i);
            B.addLast(i + 1);
            C.addLast(i );
            D.addLast((float) i);
            E.addLast(i);
            F.addLast(i);
        }
        C.removeLast();

        assertTrue("I am myself!", A.equals(A));
        assertFalse("Not the same!", A.equals(B));
        assertFalse("Not the same size!", A.equals(C));
        assertFalse("Not the same!", A.equals(D));
        assertFalse("Not the same type!", A.equals(E));
        assertTrue("Same!", A.equals(F));
    }
}
