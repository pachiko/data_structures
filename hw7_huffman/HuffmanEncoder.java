import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        HashMap<Character, Integer> res = new HashMap<>();
        for (char c: inputSymbols) {
            if (!res.containsKey(c)) res.put(c, 1);
            else res.put(c, res.get(c) + 1);
        }
        return res;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Wrong number of inputs: " + args.length);
            System.out.println("Requires only 1 for filename");
            return;
        }

        // Read chars from file
        String name = args[0];
        char[] inputs = FileUtils.readFile(name);

        // Calculate frequency table, build trie, build lookup-table
        Map<Character, Integer> freqs = buildFrequencyTable(inputs);
        BinaryTrie trie = new BinaryTrie(freqs);
        Map<Character, BitSequence> lut = trie.buildLookupTable();

        // Convert list of symbols to BitSequence and concat to final BitSequence
        ArrayList<BitSequence> bitStream = new ArrayList<>();
        for (char c: inputs) {
            bitStream.add(lut.get(c));
        }
        BitSequence bs = BitSequence.assemble(bitStream);

        // Write Trie, total # of symbols and final BitSequence to file
        ObjectWriter huf = new ObjectWriter( name + ".huf");
        huf.writeObject(trie);
        huf.writeObject(inputs.length);
        huf.writeObject(bs);
    }
}