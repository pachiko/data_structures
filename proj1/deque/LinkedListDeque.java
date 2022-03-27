package deque;
import java.util.Iterator;

/**
 * Linked-list implementation of a double-ended queue
 */
public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    /* Size of the list */
    private int size;
    /* Sentinel node */
    private ListNode<T> sentinel;

    /**
     * Ctor
     */
    public LinkedListDeque() {
        size = 0;
        sentinel= new ListNode<>(null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    @Override
    /**
     * Size
     */
    public int size() { return size; }

    @Override
    /**
     * Add element to front
     */
    public void addFirst(T item) {
        ListNode<T> newFirst = new ListNode<>(item);

        ListNode<T> oldFirst = sentinel.next;
        newFirst.next = oldFirst;
        oldFirst.prev = newFirst;

        newFirst.prev = sentinel;
        sentinel.next = newFirst;

        size++;
    }

    @Override
    /**
     * Add element to back
     */
    public void addLast(T item) {
        ListNode<T> newLast = new ListNode<>(item);

        ListNode<T> oldLast = sentinel.prev;
        newLast.prev = oldLast;
        oldLast.next = newLast;

        newLast.next = sentinel;
        sentinel.prev = newLast;

        size++;
    }

    @Override
    /**
     * Remove element from front and return
     */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        ListNode<T> oldFirst = sentinel.next;

        sentinel.next = oldFirst.next;
        oldFirst.next.prev = sentinel;

        oldFirst.next = null;
        oldFirst.prev = null;
        size--;
        return oldFirst.item;
    }

    @Override
    /**
     * Remove element from back and return
     */
    public T removeLast() {
        if (isEmpty()) return null;
        ListNode<T> oldLast = sentinel.prev;

        sentinel.prev = oldLast.prev;
        oldLast.prev.next = sentinel;

        oldLast.next = null;
        oldLast.prev = null;
        size--;
        return oldLast.item;
    }

    @Override
    /**
     * Print elements
     */
    public void printDeque() {
        for (T item: this) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    @Override
    /**
     * Return ith element, null if none
     */
    public T get(int index) {
        if (index > size - 1 || index < 0) return null;

        ListNode<T> node;
        if (index < size/2) {
            node = sentinel.next;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        } else {
            node = sentinel.prev;
            for (int i = 0; i < size - index - 1; i++) {
                node = node.prev;
            }
        }

        return node.item;
    }

    /**
     * Return ith element, null if none (recursive)
     */
    public T getRecursive(int index) {
        if (index > size - 1 || index < 0) return null;

        if (index < size/2) {
            return getRecursiveHelper(sentinel.next, index, true).item;
        } else {
            return getRecursiveHelper(sentinel.prev, size - index - 1, false).item;
        }
    }

    /**
     * Helper for recursive get.
     */
    private ListNode<T> getRecursiveHelper(ListNode<T> node, int count, boolean forward) {
        if (count == 0) return node;
        return getRecursiveHelper(forward? node.next : node.prev,count - 1, forward);
    }
    /**
     * Equals?
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof LinkedListDeque) {
            // o's T can be different type as this' T. No exceptions thrown
            LinkedListDeque<T> other = (LinkedListDeque<T>) o;

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
    @Override
    public Iterator<T> iterator() {
        return new LLDIterator();
    }

    /**
     * Iterator
     */
    private class LLDIterator implements Iterator<T> {
        ListNode<T> node;

        LLDIterator() {
            node = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return node != sentinel;
        }

        @Override
        public T next() {
            T res = node.item;
            node = node.next;
            return res;
        }
    }

    /**
     * Doubly-linked list node
     */
    private static class ListNode<U> {
        U item;
        ListNode<U> next;
        ListNode<U> prev;

        ListNode(U newItem) {
            item = newItem;
            next = null;
            prev = null;
        }
    }
}
