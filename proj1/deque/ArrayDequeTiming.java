package deque;
import edu.princeton.cs.algs4.Stopwatch;

public class ArrayDequeTiming {
    private static void printTimingTable(int[] Ns, double[] times, int[] opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.length; i += 1) {
            int N = Ns[i];
            double time = times[i];
            int opCount = opCounts[i];
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAdd(true);
        timeRemove(true);
        timeAdd(false);
        timeRemove(false);
        timeGet();
    }

    /*
     * Time Remove
     * */
    private static double timeRemove(int numElements, int numOps, boolean first) {
        ArrayDeque<Integer> array = new ArrayDeque<>();

        for (int i = 0; i < numElements; i++) {
            if (first) array.addFirst(i);
            else array.addLast(i);
        }

        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < numOps; i++) {
            if(first) array.removeFirst();
            else array.removeLast();
        }

        return sw.elapsedTime();
    }
    public static void timeRemove(boolean first) {
        System.out.println("Timing Remove " + (first? "First" : "Last"));
        int numTests = 11;

        int[] Ns = new int[numTests];
        double[] times = new double[numTests];
        int[] ops = new int[numTests];

        int numOps = 1000000;
        for (int i = 0; i < numTests; i++) {
            int elems = (int) (Math.pow(2, i)*1000);
            Ns[i] = elems;
            times[i] = timeRemove(elems, numOps, first);
            ops[i] = numOps;
        }
        printTimingTable(Ns, times, ops);
    }

    /*
     * Time Add
     * */
    private static double timeAdd(int numElements, boolean first) {
        ArrayDeque<Integer> array = new ArrayDeque<>();

        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < numElements; i++) {
            if(first) array.addFirst(i);
            else array.addLast(i);
        }

        return sw.elapsedTime();
    }
    public static void timeAdd(boolean first) {
        System.out.println("Timing Add " + (first? "First" : "Last"));
        int numTests = 11;

        int[] Ns = new int[numTests];
        double[] times = new double[numTests];
        int[] ops = new int[numTests];

        for (int i = 0; i < numTests; i++) {
            int reps = (int) (Math.pow(2, i)*1000);
            Ns[i] = reps;
            times[i] = timeAdd(reps, first);
            ops[i] = reps;
        }
        printTimingTable(Ns, times, ops);
    }


    /*
     * Time get and getRecursive methods
     * */
    private static void timeGet(ArrayDeque<Integer> array) {
        int numElements = array.size();

        Stopwatch sw = new Stopwatch();

        int first = array.get(0);
        double getStart = sw.elapsedTime();

        int last = array.get(numElements - 1);
        double getEnd = sw.elapsedTime() - getStart;

        int midLeft = array.get(numElements/2 - 1);
        double getMidLeft = sw.elapsedTime() - getEnd;

        int midRight = array.get(numElements/2);
        double getMidRight = sw.elapsedTime() - getMidLeft;

        System.out.println("First value: " + first);
        System.out.println("Last value: " + last);
        System.out.println("Mid-left value: " + midLeft);
        System.out.println("Mid-right  value: " + midRight);
        System.out.println("Time taken to get first element: " + getStart);
        System.out.println("Time taken to get last element: " + getEnd);
        System.out.println("Time taken to get mid-left element: " + getMidLeft);
        System.out.println("Time taken to get mid-right element: " + getMidRight);
    }
    public static void timeGet() {
        int numElements = 1000000;
        ArrayDeque<Integer> array = new ArrayDeque<>();
        for (int i = 0; i < numElements; i++) {
            array.addLast(i);
        }

        System.out.println("Timing Get");
        timeGet(array);
    }
}
