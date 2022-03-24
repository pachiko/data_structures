package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    /* Comparator */
    Comparator<T> myComparator;

    /**
     * Ctor
     */
    public MaxArrayDeque(Comparator<T> c) {
        super();
        myComparator = c;
    }

    /**
     * Max. JAVA doesn't allow default parameters
     */
    public T max() {
        if (isEmpty() || myComparator == null) return null;
        return findMax(myComparator);
    }
    public T max(Comparator<T> c) {
        if (isEmpty() || c == null) return null;
        return findMax(c);
    }
    private T findMax(Comparator<T> c) {
        T currentMax = null;
        for (T item : this) {
            if (currentMax == null) {
                currentMax = item;
            } else {
                if (c.compare(currentMax, item) < 0) {
                    currentMax = item;
                }
            }
        }
        return currentMax;
    }

    /**
     * Equals?
     */
    @Override
    public boolean equals(Object o) {
        return o == this; // Don't care!
    }
}
