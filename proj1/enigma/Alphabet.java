package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Vicky Yu
 */
class Alphabet {

    /** The correct Alphabet w/o repeats. */
    private String letters;

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        int len = chars.length();
        letters = "";
        for (int i = 0; i < len; i++) {
            String current = Character.toString(chars.charAt(i));
            if (!letters.contains(current)) {
                letters += current;
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return letters.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        String check = Character.toString(ch);
        return letters.contains(check);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        assert (index >= 0);
        assert (index < size());
        return letters.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        String check = Character.toString(ch);
        assert letters.contains(check);
        return letters.indexOf(check);
    }
}
