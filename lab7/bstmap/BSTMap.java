package bstmap;

import java.util.Iterator;
import java.util.Set;

/** Binary Search Tree */
public class BSTMap<K extends Comparable, V>  implements Map61B<K, V>  {
    /** Root node */
    private BSTNode root;

    /** Size of tree */
    private int size;

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return find(root, key) != null;
    }

    /** Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        BSTNode n = find(root, key);
        return (n == null)? null : n.value;
    }

    /** Helper to find a node witha given key */
    private BSTNode find(BSTNode n, K key) {
        if (n == null) return null;
        int res = key.compareTo(n.key);
        if (res < 0) return find(n.leftChild, key);
        else if (res > 0) return find(n.rightChild, key);
        else return n;
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    /** Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        root = insert(root, key, value);
    }

    /** Helper to insert a node */
    private BSTNode insert(BSTNode n, K key, V value) {
        if (n == null) {
            size++;
            return new BSTNode(key, value);
        }
        int res = key.compareTo(n.key);
        if (res < 0) n.leftChild = insert(n.leftChild, key, value);
        else if (res > 0) n.rightChild = insert(n.rightChild, key, value);
        return n;
    }

    /** Print the tree in-order traversal */
    public void printInOrder() {
        printInOrder(root);
    }

    /** Helper to print in-order */
    private void printInOrder(BSTNode n) {
        if (n == null) return;
        printInOrder(n.leftChild);
        System.out.println("(" + n.key + ", " + n.value + ")");
        printInOrder(n.rightChild);
    }

    /** Node of tree */
    private class BSTNode {
        K key;
        V value;
        BSTNode leftChild;
        BSTNode rightChild;

        public BSTNode(K k, V v) {
            this.key = k;
            this.value = v;
        }
    }

    /** Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    /** Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /** Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 7. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    /** Iterator for in-order traversal */
    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
