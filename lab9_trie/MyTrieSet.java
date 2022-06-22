import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hash-Table Mapped Trie Set
 */
public class MyTrieSet implements TrieSet61B {
    TrieNode root;

    public MyTrieSet() {
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
    public void add(String key) {
        Pair<TrieNode, Integer> p = find(key);
        TrieNode prev = p.a;
        int l = key.length();

        for (int i = p.b; i < l; i++) {
            TrieNode newNode = new TrieNode();
            prev.mapping.put(key.charAt(i), newNode);
            prev = newNode;
        }
        prev.isKey = true;
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

    @Override
    public List<String> keysWithPrefix(String prefix) {
        ArrayList<String> result = new ArrayList<>();

        Pair<TrieNode, Integer> p = find(prefix);
        if (p.b == prefix.length()) {
            collectHelper(prefix, result, p.a);
        }

        return result;
    }

    @Override
    public String longestPrefixOf(String key) {
        Pair<TrieNode, Integer> res = find(key);
        return key.substring(0, res.b);
    }

    private void collectHelper(String ss, List<String> result, TrieNode n) {
        for (Map.Entry<Character, TrieNode> entry : n.mapping.entrySet()) {
            Character c = entry.getKey();
            TrieNode cn = entry.getValue();
            String next = ss + c;
            if (cn.isKey) result.add(next);
            collectHelper(next, result, cn);
        }
    }

    private class TrieNode {
        Map<Character, TrieNode> mapping;
        boolean isKey;

        public TrieNode() {
            mapping = new HashMap<>();
            isKey = false;
        }
    }
}
