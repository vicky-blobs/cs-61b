import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        assertEquals(MultiArr.maxValue(new int[][] {{1, 2, 3}, {1, 4, 5}}), 5);
        assertEquals(MultiArr.maxValue(new int[][] {{1, 2, 3, 4}, {1, 4, 6}}), 6);
        assertEquals(MultiArr.maxValue(new int[][] {{1}, {1, 4, 5, 6, 8}}), 8);
    }

    @Test
    public void testAllRowSums() {
        assertArrayEquals(MultiArr.allRowSums(new int[][] {{1, 2, 3}, {1, 4, 5}}), new int[] {6, 10});
        assertArrayEquals(MultiArr.allRowSums(new int[][] {{1, 2, 3, 4}, {1, 4, 6}}), new int[] {10, 11});
        assertArrayEquals(MultiArr.allRowSums(new int[][] {{1}, {1, 3, 5}}), new int[] {1, 9});
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
