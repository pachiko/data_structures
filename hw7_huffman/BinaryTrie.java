import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;

public class BinaryTrie implements Serializable {
    private TrieNode root;

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        LinkedList<TrieNode> nodes = new LinkedList<>();
        for (Map.Entry<Character, Integer> entry : frequencyTable.entrySet()) {
            nodes.add(new TrieNode(entry.getValue(), entry.getKey()));
        }
        nodes.sort(null);
        root = mergeNodes(nodes);
    }

    private TrieNode mergeNodes(LinkedList<TrieNode> nodes) {
        while (!nodes.isEmpty()) {
            TrieNode least = nodes.pollFirst();
            TrieNode second = nodes.pollFirst();
            if (second == null) return least;

            TrieNode parent = new TrieNode(new TrieNode[]{least, second});
            nodes.add(parent);
            nodes.sort(null);
        }
        return null;
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        int i = 0;
        TrieNode current = root;
        while (current.children != null) {
            int b = querySequence.bitAt(i++);
            current = current.children[b];
        }
        return new Match(querySequence.firstNBits(i), current.value);
    }

    public Map<Character, BitSequence> buildLookupTable() {
        HashMap<Character, BitSequence> lut = new HashMap<>();
        BitSequence seq = new BitSequence();
        buildLUTHelper(root, seq, lut);
        return lut;
    }

    private void buildLUTHelper(TrieNode n, BitSequence seq, Map<Character, BitSequence> map) {
        if (n.children == null) {
            map.put(n.value, new BitSequence(seq));
        } else {
            for (int i = 0; i < n.children.length; i++) {
                buildLUTHelper(n.children[i], seq.appended(i), map);
            }
        }
    }

    private class TrieNode implements Comparable<TrieNode>, Serializable {
        TrieNode[] children;
        Integer freq;
        Character value;

        public TrieNode(TrieNode[] nodes) {
            freq = 0;
            value = null;
            children = nodes;
            for (TrieNode n: nodes) {
                if (n != null) {
                    freq += n.freq;
                }
            }
        }

        public TrieNode(int i, char c) {
            freq = i;
            value = c;
            children = null;
        }

        @Override
        public int compareTo(TrieNode o) {
            return freq - o.freq;
        }
    }
}
