package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

public class PercolationStats {
    // perform T independent experiments on an N-by-N grid
    int N;
    int T;
    PercolationFactory pf;
    double[] fractions;
    double mean;
    double stddev;
    double low;
    double high;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) throw new IllegalArgumentException();
        this.N = N;
        this.T = T;
        this.pf = pf;
        fractions = new double[T];

        for (int t = 0; t < T; t++) {
            Percolation p = pf.make(N);

            int[] cells = IntStream.range(0, N*N - 1).toArray();
            StdRandom.shuffle(cells);
            int i = 0;
            while (!p.percolates()) {
                int cell = cells[i];
                p.open(cell/N, cell%N);
                i++;
            }
            fractions[t] = p.numberOfOpenSites()/((double) N*N);
        }

        mean = StdStats.mean(fractions);
        stddev = StdStats.stddev(fractions);
        double rootT = Math.sqrt(T);
        low = mean - 1.96*stddev/rootT;
        high = mean + 1.96*stddev/rootT;
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return low;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return high;
    }

    public static void main(String[] args) {
        int N = parseInt(args[0]);
        int T = parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T, new PercolationFactory());
        System.out.println("Mean: " + stats.mean());
        System.out.println("Std Dev: " + stats.stddev());
        System.out.println("95% CI: " + "[" + stats.confidenceLow() + ", " + stats.confidenceHigh() + "]");
    }
}
