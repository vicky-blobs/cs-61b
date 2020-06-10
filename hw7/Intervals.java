import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

/** HW #7, Sorting ranges.
 *  @author Vicky Yu
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        intervals.sort(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });
        int top = Integer.MIN_VALUE;
        int bottom = Integer.MIN_VALUE;
        int dist = 0;
        for (int[] pair : intervals) {
            if (pair[0] > top) {
                dist += (top - bottom);
                bottom = pair[0];
                top = pair[1];
            } else if (pair[1] > top) {
                top = pair[1];
            }
        }
        dist += (top - bottom);
        return dist;
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
