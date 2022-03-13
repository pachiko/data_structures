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
                    for (int i = 500; i < 1000; i++) { // 500 - 999
                        ad1.addLast(i);
                    }
                    for (int i = 499; i >= 0; i--) { // 499 - 0
                        ad1.addFirst(i);
                    }
                    break;
                case 1:
                    for (int i = 0; i < 1000; i++) { // 0 - 999
                        ad1.addLast(i);
                    }
                    break;
                case 2:
                    for (int i = 999; i >= 0; i--) { // 999 - 0
                        ad1.addFirst(i);
                    }
                    break;
                case 3:
                    for (int i = 0; i < 500; i++) { // alternate. still has wrap-around due to contiguous resize strategy
                        ad1.addFirst(499 - i);
                        ad1.addLast(500 + i);
                    }
                    break;
            }

            switch (k) {
                case 0:
                    for (Integer i = 0; i < 500; i++) { // 0 - 499
                        assertEquals("Should have the same value", i, ad1.removeFirst());
                    }
                    for (Integer i = 999; i >= 500; i--) { // 999 - 500
                        assertEquals("Should have the same value", i, ad1.removeLast());
                    }
                    break;
                case 1:
                    for (Integer i = 0; i < 1000; i++) { // 0 - 999
                        assertEquals("Should have the same value", i, ad1.removeFirst());
                    }
                    break;
                case 2:
                    for (Integer i = 999; i >= 0; i--) { // 999 - 0
                        assertEquals("Should have the same value", i, ad1.removeLast());
                    }
                    break;
                case 3:
                    for (Integer i = 0; i < 500; i++) { // alternate. still has wrap-around due to contiguous resize strategy
                        assertEquals("Should have the same value", i, ad1.removeFirst());
                        assertEquals("Should have the same value", (Integer) (999 - i), ad1.removeLast());
                    }
                    break;
            }
        }
    }
}