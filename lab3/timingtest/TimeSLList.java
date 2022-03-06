package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static double timeGetLast(int numElements, int numGetLast) {
        SLList<Integer> sList = new SLList<>();

        for (int i = 0; i < numElements; i++) {
            sList.addLast(i);
        }

        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < numGetLast; i++) {
            sList.getLast();
        }
        return sw.elapsedTime();
    }

    public static void timeGetLast() {
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> ops = new AList<>();

        int numGetLast = 10000;

        for (int i = 1000; i < 256000; i*=2) {
            Ns.addLast(i);
            times.addLast(timeGetLast(i, numGetLast));
            ops.addLast(numGetLast);
        }
        printTimingTable(Ns, times, ops);
    }

}
