import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    private static boolean debugRadix = false;
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        String[] res = Arrays.copyOf(asciis, asciis.length);

        boolean MSDSort = true;
        if (MSDSort) {
            sortHelperMSD(res, 0, asciis.length, 0);
        } else {
            int maxW = maxWidth(asciis);
            for (int i = maxW - 1; i >= 0; i--) {
                sortHelperLSD(res, i);
            }
        }
        return res;
    }

    private static int maxWidth(String[] asciis) {
        int res = Integer.MIN_VALUE;
        for (String ss: asciis) {
            int l = ss.length();
            if (l > res) {
                res = l;
            }
        }
        return res;
    }

    private static int charAt(String ss, int idx) {
        return (ss.length() <= idx) ? -1 : ss.charAt(idx);
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        if (debugRadix) System.out.println("Index: " + index);

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (String ss: asciis) {
            int c = charAt(ss, index);
            if (c < min) min = c;
            if (c > max) max = c;
        }
        int range = max - min + 1;
        if (debugRadix) System.out.println("Max: " + max + "; " + "Min: " + min + "; " + "Range: " + range);

        // Counts array
        int[] counts = new int[range];
        for (String ss: asciis) {
            int c = charAt(ss, index);
            counts[c - min]++;
        }
        if (debugRadix) System.out.println("Counts: " + Arrays.toString(counts));

        // Index array using cumulative sum
        int cumsum = 0;
        int[] idx = new int[range];
        for (int i = 0; i < counts.length; i++) {
            int c = counts[i];
            idx[i] = cumsum;
            cumsum += c;
        }
        if (debugRadix) System.out.println("Index: " + Arrays.toString(idx));
        if (debugRadix) System.out.println("CumSum: " + cumsum);

        String[] backUp = Arrays.copyOf(asciis, asciis.length);
        for (String ss : backUp) {
            int c = charAt(ss, index);
            asciis[idx[c - min]++] = ss;
        }
        if (debugRadix) System.out.println("LSD: " + Arrays.toString(asciis));
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        if (end - start < 2) return;
        if (debugRadix) System.out.println("Start: " + start + "; " + "End: " + end + "; " + "Index: " + index);

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (int i = start; i < end; i++) {
            String ss = asciis[i];
            int c = charAt(ss, index);
            if (c < min) min = c;
            if (c > max) max = c;
        }
        int range = max - min + 1;
        if (debugRadix) System.out.println("Max: " + max + "; " + "Min: " + min + "; " + "Range: " + range);

        // Counts array
        int[] counts = new int[range];
        for (int i = start; i < end; i++) {
            String ss = asciis[i];
            int c = charAt(ss, index);
            counts[c - min]++;
        }
        if (debugRadix) System.out.println("Counts: " + Arrays.toString(counts));

        // Index array using cumulative sum
        int cumsum = 0;
        int[] idx = new int[range];
        for (int i = 0; i < counts.length; i++) {
            int c = counts[i];
            idx[i] = cumsum;
            cumsum += c;
        }
        if (debugRadix) System.out.println("Index: " + Arrays.toString(idx));
        if (debugRadix) System.out.println("CumSum: " + cumsum);

        // For Recursion.
        int[] startIdx = Arrays.copyOf(idx, idx.length + 1);
        startIdx[idx.length] = cumsum;

        String[] backUp = new String[end - start];
        System.arraycopy(asciis, start, backUp, 0,end - start);
        for (String ss : backUp) {
            int c = charAt(ss, index);
            asciis[idx[c - min]++] = ss;
        }
        if (debugRadix) System.out.println("MSD: " + Arrays.toString(asciis));

        for (int i = 0; i < idx.length; i++) {
            sortHelperMSD(asciis, startIdx[i], startIdx[i + 1], index + 1);
        }
    }

    public static void main(String[] args) {
        String[] asciis = {"dd", "abc", "aaa", "azn"};
        String[] sorted = sort(asciis);
        System.out.println("Sorted: " + Arrays.toString(sorted));
    }
}
