package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /** The array of buckets; buckets can be implemented any way */
    private Collection<Node>[] buckets;
    /** Used to iterate over keySet */
    private HashSet<K> keys;
    /** Load Factor */
    private double loadFactor;
    /** Initial capacity */
    private int initialCapacity;

    /** Constructors */
    public MyHashMap() {
        this(16);
        loadFactor = 0.75;
    }
    public MyHashMap(int initialSize) {
        initialCapacity = initialSize;
        buckets = createTable(initialSize);
        keys = new HashSet<>(initialSize);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this(initialSize);
        loadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() { return new LinkedList<>(); }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        keys = new HashSet<>(initialCapacity);
        buckets = createTable(initialCapacity);
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    /** Finds a node in a bucket, null if none */
    protected Node find(Collection<Node> coll, K key) {
        for (Node n : coll) {
            if (n.key.equals(key)) return n;
        }
        return null;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (containsKey(key)) {
            int idx = Math.floorMod(key.hashCode(), buckets.length);
            Collection<Node> coll = buckets[idx];
            Node n = find(coll, key);
            if (n != null) return n.value;
        }
        return null;
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return keys.size();
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value) {
        if (keys.size() + 1 > loadFactor*buckets.length) {
            resize(buckets.length*2);
        }
        insert(key, value, buckets, keys);
    }

    /** Insert a mapping into a bucket, held by an array of buckets */
    private void insert(K key, V value, Collection<Node>[] array, HashSet<K> set) {
        int idx = Math.floorMod(key.hashCode(), array.length);
        Collection<Node> coll = array[idx];

        if (coll != null) {
            Node n = find(coll, key);
            if (n != null) {
                n.value = value;
                return;
            }
        } else {
            coll = createBucket();
            array[idx] = coll;
        }

        coll.add(createNode(key, value));
        set.add(key);
    }

    /** Resize and rehash hash table */
    private void resize(int newSize) {
        Collection<Node>[] newCollection = createTable(newSize);
        HashSet<K> newKeys = new HashSet<>(newSize);
        for (K key : this) {
            V val = get(key);
            insert(key, val, newCollection, newKeys);
        }
        buckets = newCollection;
        keys = newKeys;
    }

    /** Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return keys;
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("Does not support remove");
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("Does not support remove");
    }

    /** Iterator */
    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

    /** Iterator */
    private class MyHashMapIterator implements Iterator<K> {
        private Iterator<K> it;

        public MyHashMapIterator() {
            it = keys.iterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public K next() {
            return it.next();
        }
    }
}
