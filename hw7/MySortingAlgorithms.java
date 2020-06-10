import java.util.Arrays;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k; i ++) {
                for (int j = i; j > 0; j--) {
                    if (array[j] < array[j-1]) {
                        swap(array, j-1, j);
                    } else {
                        break;
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k; i++) {
                int min_index = i;
                for (int j = i + 1; j < k; j++) {
                    min_index = array[min_index] > array[j] ? min_index = j : min_index;
                }
                swap(array, min_index, i);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            k = Math.min(k,array.length);
            mSort(array, 0, k, new int[k]);
        }

        private void mSort(int[] array, int lefty, int righto, int[] portion) {
            int middle = (righto + lefty) / 2;
            if (righto - lefty > 1) {
                mSort(array, middle, righto, portion);
                mSort(array, lefty, middle, portion);
                merge(array, lefty, middle, righto, portion);
                System.arraycopy(portion, lefty, array, lefty, righto - lefty);
            }
        }

        private void merge(int[] array, int lefty, int mid, int righto,
                           int[] portion) {
            for (int myleft = lefty, center = mid; myleft < mid || center < righto;) {
                if ( (center == righto)
                        || (myleft < mid && array[myleft] < array[center]) ) {
                    portion[myleft + center - mid] = array[myleft];
                    myleft++;
                } else if( (myleft == mid)
                        || (center < righto && array[myleft] >= array[center]) ) {
                    portion[myleft + center -mid] = array[center];
                    center++;
                }
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            int[] tosort = new int[k];
            if (k + 1 >= 0) System.arraycopy(a, 0, tosort, 0, k);
            k = Math.min(k, tosort.length);
            int min = tosort[0];
            int max = tosort[0];
            for (int i = 1; i < tosort.length; i++) {
                if (tosort[i] < min) {
                    min = tosort[i];
                } else if (tosort[i] > max) {
                    max = tosort[i];
                }
            }
            int expo = 1;
            while (((max - min) / expo) >= 1) {
                xbitSort(tosort, k, expo, min);
                expo *= k;
            }
            System.arraycopy(tosort, 0, a, 0, k);
        }

        private void xbitSort(int[] a, int radix, int exponent, int min) {
            int bucket_i;
            int[] buckets = new int[radix];
            int[] toret = new int[a.length];
            for (int i = 0; i < radix; i++) {
                buckets[i] = 0;
            }
            for (int value : a) {
                bucket_i = Math.abs(((value - min) / exponent) % radix);
                buckets[bucket_i]++;
            }
            for (int i = 1; i < radix; i++) {
                buckets[i] += buckets[i-1];
            }
            for (int i = a.length - 1; i >= 0; i--) {
                bucket_i = Math.abs((((a[i] - min) / exponent) % radix));
                toret[--buckets[bucket_i]] = a[i];
            }
            System.arraycopy(toret,
                    0, a, 0, a.length);
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
