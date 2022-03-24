package deque;

import org.junit.Test;
import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;

/** Performs some basic array tests. */
public class ArrayDequeTest {
    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        ArrayDeque<String> ad1 = new ArrayDeque<>();

        assertTrue("A newly initialized LLDeque should be empty", ad1.isEmpty());
        ad1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, ad1.size());
        assertFalse("lld1 should now contain 1 item", ad1.isEmpty());

        ad1.addLast("middle");
        assertEquals(2, ad1.size());

        ad1.addLast("back");
        assertEquals(3, ad1.size());

        System.out.println("Printing out deque: ");
        ad1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        // should be empty
        assertTrue("ad1 should be empty upon initialization", ad1.isEmpty());

        ad1.addFirst(10);
        // should not be empty
        assertFalse("ad1 should contain 1 item", ad1.isEmpty());

        ad1.removeFirst();
        // should be empty
        assertTrue("ad1 should be empty after removal", ad1.isEmpty());

        ad1.addLast(25);
        assertFalse("ad1 should contain 1 item", ad1.isEmpty());

        ad1.removeLast();
        assertTrue("ad1 should be empty after removal", ad1.isEmpty());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(3);

        ad1.removeLast();
        ad1.removeFirst();
        ad1.removeLast();
        ad1.removeFirst();

        int size = ad1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        ArrayDeque<String>  ad1 = new ArrayDeque<>();
        ArrayDeque<Double>  ad2 = new ArrayDeque<>();
        ArrayDeque<Boolean> ad3 = new ArrayDeque<>();

        ad1.addFirst("string");
        ad2.addFirst(3.14159);
        ad3.addFirst(true);

        String s = ad1.removeFirst();
        double d = ad2.removeFirst();
        boolean b = ad3.removeFirst();

        assertEquals("Testing if string equals", s, "string");
        assertEquals("Testing if double equals", d, 3.14159, 1e-6);
        assertEquals("Testing if b equals", b, true);
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();

        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, ad1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, ad1.removeLast());

    }

    @Test
    /* Tests get()*/
    public void getTest() {
        ArrayDeque<String> ad1 = new ArrayDeque<>();
        assertNull("Should be null", ad1.get(69));
        assertNull("Should be null", ad1.get(-1));

        assertTrue(ad1.isEmpty());

        ad1.addLast("Delta");
        ad1.addFirst("Charlie");
        ad1.addFirst("Bravo");
        ad1.addFirst("Alpha");
        ad1.removeFirst();
        assertFalse(ad1.isEmpty());

        String[] expected = new String[]{"Bravo", "Charlie" , "Delta"};
        for (int i = 0; i < ad1.size(); i++) {
            String g = ad1.get(i);
            assertEquals("Same elements", expected[i], g);
        }
        ad1.printDeque();
    }
    
    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigADequeTest() {
        for (int k = 0; k < 4; k++) {
            ArrayDeque<Integer> ad1 = new ArrayDeque<>();

            switch (k) {
                case 0:
                    for (int i = 500000; i < 1000000; i++) { // 500k - 999k
                        ad1.addLast(i);
                    }
                    for (int i = 499999; i >= 0; i--) { // 499k - 0
                        ad1.addFirst(i);
                    }
                    break;
                case 1:
                    for (int i = 0; i < 1000000; i++) { // 0 - 999k
                        ad1.addLast(i);
                    }
                    break;
                case 2:
                    for (int i = 999999; i >= 0; i--) { // 999k - 0
                        ad1.addFirst(i);
                    }
                    break;
                case 3: // alternate. still has wrap-around due to contiguous resize strategy
                    for (int i = 0; i < 500000; i++) {
                        ad1.addFirst(499999 - i);
                        ad1.addLast(500000 + i);
                    }
                    break;
            }

            switch (k) {
                case 0:
                    for (Integer i = 0; i < 500000; i++) { // 0 - 499k
                        assertEquals("Should have the same value", i, ad1.removeFirst());
                    }
                    for (Integer i = 999999; i >= 500000; i--) { // 999k - 500k
                        assertEquals("Should have the same value", i, ad1.removeLast());
                    }
                    break;
                case 1:
                    for (Integer i = 0; i < 1000000; i++) { // 0 - 999k
                        assertEquals("Should have the same value", i, ad1.removeFirst());
                    }
                    break;
                case 2:
                    for (Integer i = 999999; i >= 0; i--) { // 999k - 0
                        assertEquals("Should have the same value", i, ad1.removeLast());
                    }
                    break;
                case 3: // alternate. still has wrap-around due to contiguous resize strategy
                    for (Integer i = 0; i < 500000; i++) {
                        assertEquals("Should have the same value", i, ad1.removeFirst());
                        assertEquals("Should have the same value", (Integer) (999999 - i), ad1.removeLast());
                    }
                    break;
            }
        }
    }
    
    @Test
    /* Random add remove tests */
    public void randomizedAddRemoveTest() {
        ArrayDeque<Integer> A = new ArrayDeque<>();
        int N = 5000;

        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                Integer randVal = StdRandom.uniform(0, 100);
                int oldSize = A.size();
                A.addLast(randVal);
                assertEquals(A.size(), oldSize + 1);
                assertEquals(randVal, A.removeLast());
                A.addLast(randVal);
            } else if (operationNumber == 1) {
                // addFirst
                Integer randVal = StdRandom.uniform(0, 100);
                int oldSize = A.size();
                A.addFirst(randVal);
                assertEquals(A.size(), oldSize + 1);
                assertEquals(randVal, A.removeFirst());
                A.addFirst(randVal);
            }else if (operationNumber == 2) {
                if (!A.isEmpty()) {
                    int oldSize = A.size();
                    A.removeLast();
                    assertEquals(A.size(), oldSize - 1);
                } else {
                    A.removeLast();
                    assertEquals(A.size(), 0);
                }
            } else if (operationNumber == 3) {
                if (!A.isEmpty()) {
                    int oldSize = A.size();
                    A.removeFirst();
                    assertEquals(A.size(), oldSize - 1);
                } else {
                    A.removeFirst();
                    assertEquals(A.size(), 0);
                }
            }
        }
    }

    @Test
    /* Random get and getRecursive tests */
    public void randomizedGetTest() {
        ArrayDeque<Integer> A = new ArrayDeque<>();
        int N = 5000;

        Integer[] indices = new Integer[N];
        for (int i = 0; i < N; i++) {
            A.addLast(i);
            indices[i] = i;
        }
        StdRandom.shuffle(indices);

        for (int i = 0; i < N; i++) {
            Integer index = indices[i];
            assertEquals(A.get(index), index);
        }
    }

    @Test
    /* Test equals method */
    public void testEquals() {
        ArrayDeque<Integer> A = new ArrayDeque<>();
        ArrayDeque<Integer> B = new ArrayDeque<>();
        ArrayDeque<Integer> C = new ArrayDeque<>();
        ArrayDeque<Float> D = new ArrayDeque<>();
        LinkedListDeque<Integer> E = new LinkedListDeque<>();
        ArrayDeque<Integer> F = new ArrayDeque<>();

        int N = 10;
        int start = 11;
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