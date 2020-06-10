package enigma;

/** import java.util.HashSet; */

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Vicky Yu
 */
class Permutation {

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles of this permutation. */
    private String[] _cycles;

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored.
     *  Throw error for trying to make a cycle w/o it being in the alphabet. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        String changed = cycles.replaceAll(" ", "");
        changed = changed.replace(")(", " ");
        changed = changed.replace(")", "");
        changed = changed.replace("(", "");
        String[] toAdd = changed.split(" ");
        for (String s : toAdd) {
            for (int j = 0; j < s.length(); j++) {
                char inCycle = s.charAt(j);
                if (!alphabet.contains(inCycle)) {
                    throw new EnigmaException("Cycle character "
                            + "not in alphabet.");
                }
            }
        }
        _cycles = toAdd;
        for (int i = 0; i < alphabet.size(); i++) {
            String inAlpha = Character.toString(alphabet.toChar(i));
            if (!changed.contains(inAlpha)) {
                addCycle(inAlpha);
            }
        }
    }

    /** Returns the cycle as String list. */
    String[] cycles() {
        return _cycles;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        String[] add = new String[_cycles.length + 1];
        System.arraycopy(_cycles, 0, add, 0, _cycles.length);
        add[_cycles.length] = cycle;
        _cycles = add;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return alphabet().size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int real = wrap(p);
        char hi = _alphabet.toChar(real);
        char permuted = permute(hi);
        return _alphabet.toInt(permuted);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int real = wrap(c);
        char hi = _alphabet.toChar(real);
        char inverted = invert(hi);
        return _alphabet.toInt(inverted);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        char toRet = p;
        for (String cycle : _cycles) {

            for (int j = 0; j < cycle.length(); j++) {
                char find = cycle.charAt(j);
                if (p == find) {
                    toRet = cycle.charAt(Math.floorMod(j + 1, cycle.length()));
                }
            }
        }
        return toRet;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        char toRet = c;
        for (String cycle : _cycles) {
            for (int j = 0; j < cycle.length(); j++) {
                char find = cycle.charAt(j);
                if (c == find) {
                    toRet = cycle.charAt(Math.floorMod(j - 1, cycle.length()));
                }
            }
        }
        return toRet;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (String cycle : _cycles) {
            if (cycle.length() == 1) {
                return false;
            }
        }
        return true;
    }

}
