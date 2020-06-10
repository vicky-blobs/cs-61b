package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;


import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Vicky Yu
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkCycle() {
        Alphabet six = new Alphabet("ABCDEFGHH");
        Permutation check = new Permutation("(ABC) (DEF) (H)", six);
    }

    @Test
    public void checkSize() {
        Alphabet six = new Alphabet("ABCDEFGHH");
        Permutation check = new Permutation("(ABC) (DEF) (H)", six);
        assertEquals(8, check.size());
    }

    @Test
    public void checkPermute() {
        Alphabet six = new Alphabet("ABCDEFGHH");
        Permutation check = new Permutation("(ABC) (DEF) (H)", six);
        assertEquals('C', check.permute('B'));
        assertEquals('A', check.permute('C'));
        assertEquals('G', check.permute('G'));
        assertEquals('H', check.permute('H'));
        assertEquals(2, check.permute(1));

        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation c = new Permutation("(12)(34)", new Alphabet("1234"));
        Permutation a = new Permutation("(BAECD)", new Alphabet("ABCDE"));
        assertEquals('B', p.permute('D'));
        assertEquals('1', c.permute('2'));
        assertEquals('E', a.permute('A'));
        assertEquals(1, p.permute(3));
        assertEquals(1, p.permute(-1));
        assertEquals(1, p.permute(7));
    }

    @Test
    public void checkInverse() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation g = new Permutation("(1EH)", new Alphabet("HE1"));
        Permutation h = new Permutation("(B)", new Alphabet("B"));
        assertEquals('B', h.invert('B'));
        assertEquals('H', g.invert('1'));
        assertEquals('B', p.invert('A'));
        assertEquals('D', p.invert('B'));
        assertEquals(0, p.invert(6));
        assertEquals(1, p.invert(0));
    }

    @Test
    public void checkDerangement() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        Permutation h = new Permutation("(B)", new Alphabet("B"));
        assertTrue(p.derangement());
        assertFalse(h.derangement());
    }
}
