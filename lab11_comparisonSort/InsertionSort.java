import java.util.Arrays;

public class InsertionSort {
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void sort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arr[j] < arr[j - 1]) {
                    swap(arr, j - 1, j);
                } else break;
            }
        }
    }

    public static void main(String[] args) {
//        int[] arr = {32, 15, 2, 17, 19, 26, 41, 17, 17};
        int[] arr = {1, 2, 3, 5, 6, 7, 8, 9, 4};
        InsertionSort.sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
