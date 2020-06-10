package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author Vicky Yu
 */

public class ArraysTest {
    @Test
    public void testCatenate() {
        int[] testy = new int[] {3, 4, 1, 2};
        int[] nexty = new int[] {1, 2};
        int[] finaly = new int[] {3, 4, 1, 2, 1, 2};
        assertArrayEquals(finaly, Arrays.catenate(testy, nexty));
    }
    @Test
    public void testRemove() {
        int[] testy = new int[] {3, 4, 1, 2};
        int[] finaly = new int[] {3};
        assertArrayEquals(finaly, Arrays.remove(testy, 3, 3));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
