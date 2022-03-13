package deque;

/**
 * Linked-list implementation of a double-ended queue
 */
public class LinkedListDeque<T> {
    /* Size of the list */
    private int size;
    /* Sentinel node */
    private ListNode<T> sentinel;

    /**
     * Ctor
     */
    LinkedListDeque() {
        size = 0;
        sentinel= new ListNode<>(null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
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

    /**
     * Print elements
     */
    public void printDeque() {
        ListNode<T> node = sentinel.next;
        while(node != sentinel) {
            System.out.print(node.item + " ");
            node = node.next;
        }
        System.out.println();
    }

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
