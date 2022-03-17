package deque;

public interface Deque<T> {
    /**
     * Size
     */
    public int size();

    /**
     * Empty?
     */
    public boolean isEmpty();

    /**
     * Add element to front
     */
    public void addFirst(T item);

    /**
     * Add element to back
     */
    public void addLast(T item);

    /**
     * Remove element from front and return
     */
    public T removeFirst();

    /**
     * Remove element from back and return
     */
    public T removeLast();

    /**
     * Print elements
     */
    public void printDeque();

    /**
     * Return ith element, null if none
     */
    public T get(int index);
}
