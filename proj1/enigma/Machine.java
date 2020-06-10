package enigma;

import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Vicky Yu
 */
class Machine {
    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** The number of rotors. */
    private int _numRotors;

    /** The number of pawls. */
    private int _pawls;

    /** All the rotors of machine. */
    private Collection<Rotor> _allRotors;

    /** The rotors that will be used. */
    private Rotor[] _setRotors;

    /** Plugboard setting. */
    private Permutation _plugboard;

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _setRotors = new Rotor[numRotors];
    }


    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            String uno = rotors[i].toUpperCase();
            for (Rotor curr : _allRotors) {
                String name = curr.name().toUpperCase();
                if (uno.equals(name)) {
                    _setRotors[i] = curr;
                }
            }
        }
        Rotor reflect = _setRotors[0];
        if (!(reflect instanceof Reflector)) {
            throw new EnigmaException("Where is reflector?");
        }
        if (_setRotors.length != rotors.length) {
            throw new EnigmaException("Wrong size!");
        }
        for (int i = 0; i < _numRotors - 1; i++) {
            if (_setRotors[i] == null) {
                throw new EnigmaException("Bad rotor name.");
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != _numRotors - 1) {
            throw new EnigmaException("The sizes don't match!");
        }
        for (int i = 1; i < _numRotors; i++) {
            char chara = setting.charAt(i - 1);
            if (!_alphabet.contains(chara)) {
                throw new EnigmaException("My setting is strange");
            }
            _setRotors[i].set(chara);
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] notched = new boolean[_numRotors];
        boolean[] advanced = new boolean[_numRotors];
        int rightmost = _numRotors - 1;
        advanced[rightmost] = true;
        for (int i = rightmost; i >= 0; i--) {
            Rotor check = _setRotors[i];
            notched[i] = check.atNotch();
        }
        for (int i = rightmost; i > 1; i--) {
            Rotor meRotor = _setRotors[i];
            Rotor tomeLeft = _setRotors[i - 1];
            if (tomeLeft instanceof FixedRotor) {
                continue;
            } else if (!(advanced[i - 1]) && notched[i]) {
                tomeLeft.advance();
                advanced[i - 1] = true;
                if (!advanced[i]) {
                    meRotor.advance();
                    advanced[i] = true;
                }
            }
        }
        _setRotors[rightmost].advance();
        int converted = c;
        if (_plugboard != null) {
            converted = _plugboard.permute(converted);
        }
        for (int i = _setRotors.length - 1; i >= 0; i--) {
            Rotor curRotor = _setRotors[i];
            converted = curRotor.convertForward(converted);
        }
        for (int i = 1; i < _setRotors.length; i++) {
            Rotor curRotor = _setRotors[i];
            converted = curRotor.convertBackward(converted);
        }
        if (_plugboard != null) {
            converted = _plugboard.permute(converted);
        }
        return converted;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        msg = msg.replaceAll(" ", "");
        String convertedString = "";
        for (int i = 0; i < msg.length(); i++) {
            char ye = msg.charAt(i);
            if (!_alphabet.contains(ye)) {
                throw new EnigmaException("Char not in alphabet!");
            }
            int index = _alphabet.toInt(ye);
            char converted = _alphabet.toChar(convert(index));
            convertedString += converted;
        }
        return convertedString;
    }
}

