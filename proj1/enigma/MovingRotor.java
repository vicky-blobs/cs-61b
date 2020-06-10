package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Vicky Yu
 */
class MovingRotor extends Rotor {

    /** The initial notches on this rotor. */
    private String _initialNotch;

    /** The notches on this rotor. */
    private String _notches;

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _initialNotch = notches;
        _notches = notches;
    }

    /** Moving rotor's notches goes back to the original. */
    public void notchReset() {
        _notches = _initialNotch;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        int next = setting() + 1;
        int wrapped = permutation().wrap(next);
        super.set(wrapped);
    }

    @Override
    boolean atNotch() {
        char currSetting = alphabet().toChar(setting());
        for (int i = 0; i < _notches.length(); i++) {
            if (_notches.charAt(i) == currSetting) {
                return true;
            }
        }
        return false;
    }
}
