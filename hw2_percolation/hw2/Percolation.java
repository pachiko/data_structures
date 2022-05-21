package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.HashSet;

public class Percolation {
    WeightedQuickUnionUF uf;
    HashSet<Integer> opens;
    int N;

    public Percolation(int N) { // O(N^2)
        if (N <= 0) throw new IllegalArgumentException();

        opens = new HashSet<>();

        int total = N*N + 2;
        uf = new WeightedQuickUnionUF(total); // NxN grid + source + sink

        int sink = total - 1;
        for (int i = 0; i < N; i++) {
            uf.union(0, i + 1);  // connect top row with source
            uf.union(sink, sink - N + i );  // connect bottom row with sink
        }

        this.N = N;
    }

    private boolean validCell(int row, int col) {
        return !(row < 0 || row >= N || col < 0 || col >= N);
    }

    private int cell1D(int row, int col) {
        return N*row + col;
    }

    private void connectIfOpen(int source, int target) {
        if (opens.contains(source)) uf.union(source, target);
    }

    public void open(int row, int col) {
        if (!validCell(row, col)) throw new IndexOutOfBoundsException();
        int newOpen = cell1D(row, col);
        if (opens.contains(newOpen)) return;
        opens.add(newOpen);
        if (validCell(row, col + 1)) connectIfOpen(cell1D(row, col + 1), newOpen);
        if (validCell(row, col - 1)) connectIfOpen(cell1D(row, col - 1), newOpen);
        if (validCell(row + 1, col)) connectIfOpen(cell1D(row + 1, col), newOpen);
        if (validCell(row - 1, col)) connectIfOpen(cell1D(row - 1, col), newOpen);
    }

    public boolean isOpen(int row, int col) {
        if (!validCell(row, col)) throw new IndexOutOfBoundsException();
        return opens.contains(cell1D(row, col));
    }

    public boolean isFull(int row, int col) {
        if (!validCell(row, col)) throw new IndexOutOfBoundsException();
        return isOpen(row, col) && uf.connected(0, cell1D(row, col)); // connected to source AND open
    }

    public int numberOfOpenSites() { // O(1)
        return opens.size();
    }

    public boolean percolates() {
        return uf.connected(0, N*N + 1); // sink connected to source
    }

    public static void main(String[] args) {

    }
}
