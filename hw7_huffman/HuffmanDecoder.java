public class HuffmanDecoder {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Wrong number of inputs: " + args.length);
            System.out.println("Requires 1 for Huffman filename, 1 for decoded filename");
            return;
        }

        // Read BinaryTrie and massive BitSequence
        ObjectReader reader = new ObjectReader(args[0]);
        BinaryTrie trie = (BinaryTrie) reader.readObject();
        int symbolLength = (int) reader.readObject();
        BitSequence bs = (BitSequence) reader.readObject();

        // Rebuild the symbols
        char[] symbols = new char[symbolLength];
        for (int i = 0; i < symbolLength; i++) {
            Match m = trie.longestPrefixMatch(bs);
            symbols[i] = m.getSymbol();
            bs = bs.allButFirstNBits(m.getSequence().length());
        }

        // Write rebuilt symbols to file
        FileUtils.writeCharArray(args[1], symbols);
    }
}
