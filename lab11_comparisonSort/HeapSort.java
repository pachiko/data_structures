import java.util.Arrays;

public class HeapSort {
    private static int leftChild(int i) { return 2*i + 1; }

    private static int rightChild(int i) { return 2*i + 2; }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static int compareAndSwap(int[] arr, int a, int b) {
        if (arr[b] > arr[a]) {
            swap(arr, a, b);
            return b;
        }
        return a;
    }

    private static void sink(int[] arr, int i, int hi) {
        while (true) {
            int l = leftChild(i);
            int r = rightChild(i);

            boolean leftExists = l < hi;
            boolean rightExists = r < hi;
            if (!leftExists && !rightExists) break;

            if (rightExists) l = (arr[l] > arr[r]) ? l : r; // Get max child index
            r = compareAndSwap(arr, i, l); // Get max subtree index
            if (i == r) break; // Break if can't sink further
            i = r; // Continue sinking
        }
    }

    // Bottom-up level-order max-heapification
    private static void heapify(int[] arr) {
        int n = arr.length;
        while (n >= 0) {
            sink(arr, n, arr.length);
            n--;
        }
    }

    public static void sort(int[] arr) {
        heapify(arr);
        for (int i = arr.length - 1; i >= 0; i--) {
            swap(arr, 0, i);
            sink(arr, 0, i);
        }
    }

    public static void main(String[] args) {
        int[] arr = {32, 15, 2, 17, 19, 26, 41, 17, 17};
        HeapSort.sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
