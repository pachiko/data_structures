package bearmaps.proj2d;

import java.util.List;

public interface TrieMap61B<T> {

    /** Clears all items out of Trie */
    void clear();

    /** Returns true if the Trie contains KEY, false otherwise */
    boolean contains(String key);

    /** Inserts string KEY into Trie */
    void add(String key, T value);

    /** Returns a list of all values that start with PREFIX */
    List<T> valuesWithPrefix(String prefix);

    /** Returns the longest prefix of KEY that exists in the Trie
     * Not required for Lab 9. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    String longestPrefixOf(String key);

}
