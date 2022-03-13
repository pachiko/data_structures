package deque;

/**
 * Array implementation of a double-ended queue
 */
public class ArrayDeque<T> {
    /* Size of the array */
    private int size;
    /* Index of the next element when addFirst */
    private int nextFirst;
    /* Index of the last element when addLast */
    private int nextLast;
    /* Array of elements */
    private T[] array;

    /**
     * Resizing parameters
     */
    /* Minimum required length of the array that permits down-sizing */
    private int minLength = 16;
    /* Load factor for down-sizing */
    private float minLoadFactor = 0.25f;
    /* Load factor for up-sizing */
    private float maxLoadFactor = 0.75f;
    /* Capacity multiplier */
    private int capMultiplier = 2;

    /**
     * Ctor
     */
    ArrayDeque() {
        size = 0;
        array = (T[]) new Object[8]; // initial capacity of 8
        nextFirst = 3;
        nextLast = 4;
    }

    /**
     * Size
     */
    public int size() { return size; }

    /**
     * Empty?
     */
    public boolean isEmpty() { return size == 0; }

    /**
     * Decrement with wrap-around.
     */
    private int decWrapAround(int i) {
        i -= 1;
        if (i < 0) i = array.length - 1;
        return i;
    }

    /**
     * Increment with wrap-around.
     */
    private int incWrapAround(int i) {
        i += 1;
        if (i >= array.length) i = 0;
        return i;
    }

    /**
     * Resize if necessary
     */
    private void resizeIfNecessary(boolean increment) {
        int incr = increment? 1 : -1;
        float capacity = array.length;
        float loadFactor = (size + incr)/capacity;
        if (increment && loadFactor > maxLoadFactor) {
            resize((int) (capacity*capMultiplier));
        } else if (!increment && loadFactor < minLoadFactor && capacity >= minLength) {
            resize((int) (capacity/capMultiplier));
        }
    }

    /**
     * Resize
     */
    private void resize(int capacity) {
        T[] newArray = (T[]) new Object[capacity];

        int currentFirst = incWrapAround(nextFirst);
        int currentLast = decWrapAround(nextLast);

        if (currentFirst > currentLast) { // Wrap-around. Maintain gap in middle
            int offset =  capacity - array.length;
            System.arraycopy(array, 0, newArray, 0, currentLast + 1);
            System.arraycopy(array, currentFirst, newArray, currentFirst + offset,
                    array.length - currentFirst);
            array = newArray;
            nextFirst = decWrapAround(currentFirst + offset);
        } else { // Contiguous. Always move to index 0
            System.arraycopy(array, currentFirst, newArray, 0, size);
            array = newArray;
            nextFirst = decWrapAround(0);
            nextLast = incWrapAround(size - 1);
        }
    }

    /**
     * Add element to front
     */
    public void addFirst(T item) {
        resizeIfNecessary(true);
        array[nextFirst] = item;
        size++;
        nextFirst = decWrapAround(nextFirst);
    }

    /**
     * Add element to back
     */
    public void addLast(T item) {
        resizeIfNecessary(true);
        array[nextLast] = item;
        size++;
        nextLast = incWrapAround(nextLast);
    }

    /**
     * Remove element from front and return
     */
    public T removeFirst() {
        if (isEmpty()) return null;
        resizeIfNecessary(false);
        nextFirst = incWrapAround(nextFirst);
        T res = array[nextFirst];
        array[nextFirst] = null;
        size--;
        return res;
    }

    /**
     * Remove element from back and return
     */
    public T removeLast() {
        if (isEmpty()) return null;
        resizeIfNecessary(false);
        nextLast = decWrapAround(nextLast);
        T res = array[nextLast];
        array[nextLast] = null;
        size--;
        return res;
    }

    /**
     * Print elements
     */
    public void printDeque() {
        if (!isEmpty()) {
            int currentFirst = incWrapAround(nextFirst);
            for (int i = currentFirst; i != nextLast; i = incWrapAround(i)) {
                System.out.print(array[i] + " ");
            }
        }
        System.out.println();
    }

    /**
     * Return ith element, null if none
     */
    public T get(int index) {
        if (index > size - 1 || index < 0) return null;
        return array[index + incWrapAround(nextFirst)];
    }
}
