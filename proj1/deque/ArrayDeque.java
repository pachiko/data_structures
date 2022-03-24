package deque;
import java.util.Iterator;

/**
 * Array implementation of a double-ended queue
 */
public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
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
    @Override
    public int size() { return size; }

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
    @Override
    public void addFirst(T item) {
        resizeIfNecessary(true);
        array[nextFirst] = item;
        size++;
        nextFirst = decWrapAround(nextFirst);
    }

    /**
     * Add element to back
     */
    @Override
    public void addLast(T item) {
        resizeIfNecessary(true);
        array[nextLast] = item;
        size++;
        nextLast = incWrapAround(nextLast);
    }

    /**
     * Remove element from front and return
     */
    @Override
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
    @Override
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
    @Override
    public void printDeque() {
        for (T item: this) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    /**
     * Return ith element, null if none
     */
    @Override
    public T get(int index) {
        if (index > size - 1 || index < 0) return null;
        return array[(incWrapAround(nextFirst) + index)%array.length];
    }

    /**
     * Equals?
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof ArrayDeque) {
            // o's T can be different type as this' T. No exceptions thrown
            ArrayDeque<T> other = (ArrayDeque<T>) o;

            if (other.size() != size()) {
                return false;
            }

            int i = 0;
            for (T item : other) {
                if (item != get(i)) {
                    return false;
                }
                i++;
            }
            return true;
        }
        return false;
    }

    /**
     * Iterator
     */
    public Iterator<T> iterator() {
        return new ADIterator();
    }

    /**
     * Iterator
     */
    private class ADIterator implements Iterator<T> {
        int i;
        int count;

        ADIterator() {
            i = incWrapAround(nextFirst);
            count = 0;
        }

        @Override
        public boolean hasNext() {
            return count < size;
        }

        @Override
        public T next() {
            T res = array[i];
            i = incWrapAround(i);
            count++;
            return res;
        }
    }
}
