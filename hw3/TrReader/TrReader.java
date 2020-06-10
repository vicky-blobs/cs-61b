import org.apache.commons.cli.MissingOptionException;

import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Vicky Yu
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */
    private Reader str;
    private String from;
    private String to;
    public TrReader(Reader str, String from, String to) {
        if (str != null) {
            this.str = str;
        }
        if (from != null) {
            this.from = from;
        }
        if (to != null) {
            this.to = to;
        }
    }
    public void close() throws IOException {
        this.str.close();
    }

    /* TODO: IMPLEMENT ANY MISSING ABSTRACT METHODS HERE
     * NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */
    public char change(char before) {
        if (from.indexOf(before) == -1) {
            return before;
        }
        char after = to.charAt(from.indexOf(before));
        return after;
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        int red = this.str.read(cbuf, off, len);
        for (int ref = off; ref < off + len; ref++) {
            cbuf[ref] = change(cbuf[ref]);
        }
        if (red < len) {
            return red;
        }
        else return len;
    }
}
