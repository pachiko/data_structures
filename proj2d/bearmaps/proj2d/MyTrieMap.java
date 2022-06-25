package bearmaps.proj2d;

import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hash-Table Mapped Trie Map
 */
public class MyTrieMap<T> implements TrieMap61B<T> {
    TrieNode root;

    public MyTrieMap() {
        root = new TrieNode();
    }

    @Override
    public void clear() {
        root = new TrieNode();
    }

    @Override
    public boolean contains(String key) {
        Pair<TrieNode, Integer> p = find(key);
        return p.b == key.length() && p.a.isKey;
    }

    @Override
    public void add(String key, T value) {
        Pair<TrieNode, Integer> p = find(key);
        TrieNode prev = p.a;
        int l = key.length();

        for (int i = p.b; i < l; i++) {
            TrieNode newNode = new TrieNode();
            prev.mapping.put(key.charAt(i), newNode);
            prev = newNode;
        }
        prev.isKey = true;
        if (prev.values == null) prev.values = new ArrayList<>();
        prev.values.add(value);
    }

    private Pair<TrieNode, Integer> find(String ss) {
        TrieNode n = root;
        int i = 0;
        int l = ss.length();
        while (i < l) {
            TrieNode p = n.mapping.get(ss.charAt(i));
            if (p != null) {
                n = p;
                i++;
            } else {
                break;
            }
        }
        return new Pair<>(n, i);
    }

    public List<T> get(String s) {
        Pair<TrieNode, Integer> p = find(s);
        if (p.b == s.length() && p.a.isKey) return p.a.values;
        else return new ArrayList<>();
    }

    @Override
    public List<T> valuesWithPrefix(String prefix) {
        ArrayList<T> result = new ArrayList<>();

        Pair<TrieNode, Integer> p = find(prefix);
        if (p.b == prefix.length()) {
            collectHelper(prefix, result, p.a, false);
        }

        return result;
    }

    @Override
    public String longestPrefixOf(String key) {
        Pair<TrieNode, Integer> res = find(key);
        return key.substring(0, res.b);
    }

    private void collectHelper(String ss, List<T> result, TrieNode n, boolean all) {
        for (Map.Entry<Character, TrieNode> entry : n.mapping.entrySet()) {
            Character c = entry.getKey();
            TrieNode cn = entry.getValue();
            String next = ss + c;
            if (cn.isKey) {
                if (all) result.addAll(cn.values);
                else result.add(cn.values.get(0));
            }
            collectHelper(next, result, cn, all);
        }
    }

    private class TrieNode {
        Map<Character, TrieNode> mapping;
        boolean isKey;
        ArrayList<T> values;

        public TrieNode() {
            mapping = new HashMap<>();
            isKey = false;
        }
    }
}
