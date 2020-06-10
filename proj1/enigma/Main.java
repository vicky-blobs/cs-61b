package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Vicky Yu
 */
public final class Main {
    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Our enigma machine. */
    private Machine enigma;

    /** Name of rotor. */
    private String rotName;

    /** String of the next configuration line. */
    private String line;

    /** String for the
     *  cycles. */
    private String permutation;

    /** ArrayList of rotors. */
    private ArrayList<Rotor> rotorList = new ArrayList<>();

    /** Current rotor notch. */
    private String curNotch;

    /** Current setting string. */
    private String _setting;

    /** The amount of rotors specified in setting. */
    private int _amountofrotors;

    /** The previous string in input. */
    private String _prev = "";

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        enigma = readConfig();
        while (_input.hasNextLine()) {
            String next = _input.nextLine();
            setUp(enigma, _setting);
            if (_prev.contains("*") && !(_prev.equals(_setting))) {
                setUp(enigma, _prev);
            }
            while (!(next.contains("*"))) {
                String ret = enigma.convert(next.replaceAll(" ", ""));
                if (!(next.isEmpty())) {
                    printMessageLine(ret);
                }
                if (next.equals("")) {
                    _output.println();
                }
                if (!_input.hasNext()) {
                    next = "hehe *";
                } else {
                    next = (_input.nextLine());
                    _prev = next;
                }
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String beingRead = _config.next();
            if (beingRead.contains("*")) {
                throw new EnigmaException("Alphabet format wrong");
            }
            _alphabet = new Alphabet(beingRead);
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Wrong amount of rotors");
            }
            _amountofrotors = _config.nextInt();
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Wrong amount of pawls");
            }
            int pawls = _config.nextInt();
            _setting = _input.nextLine();
            if (_setting.charAt(0) != '*') {
                throw new EnigmaException("Must be star");
            }
            line = (_config.next()).toUpperCase();
            while (_config.hasNext()) {
                rotName = line;
                curNotch = _config.next().toUpperCase();
                rotorList.add(readRotor());
            }
            return new Machine(_alphabet, _amountofrotors, pawls, rotorList);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            permutation = "";
            line = (_config.next());
            while (_config.hasNext() && line.contains("(")) {
                permutation = permutation.concat(line + " ");
                line = (_config.next());
            }
            if (!_config.hasNext()) {
                permutation = permutation.concat(line + " ");
            }
            char rotortype = curNotch.charAt(0);
            Permutation newperm = new Permutation(permutation, _alphabet);
            if (rotortype == 'M') {
                return new MovingRotor(rotName, newperm, curNotch.substring(1));
            }
            if (rotortype == 'N') {
                return new FixedRotor(rotName, newperm);
            }
            if (rotortype == 'R') {
                return new Reflector(rotName, newperm);
            } else {
                throw new EnigmaException("Rotor type not specified");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        _setting = _setting.trim();
        String[] split = settings.split(" ");
        if (split.length - 1 < M.numRotors()) {
            throw new EnigmaException("Please add more.");
        }
        String[] rotorsToUse = new String[M.numRotors()];
        if (M.numRotors() + 1 - 1 >= 0) {
            System.arraycopy(split, 1,
                    rotorsToUse, 0, M.numRotors() + 1 - 1);
        }
        for (int i = 0; i < rotorsToUse.length - 1; i++) {
            for (int j = i + 1; j < rotorsToUse.length; j++) {
                if (rotorsToUse[i].equals(rotorsToUse[j])) {
                    throw new EnigmaException("Woah! Don't reuse rotors.");
                }
            }
        }
        String swappy = "";
        for (int i = _amountofrotors + 2;
             i < split.length; i++) {
            swappy = swappy.concat(split[i] + " ");
        }
        if (rotorsToUse.length != _amountofrotors) {
            throw new EnigmaException("Wrong amount of rotors");
        }
        if (M.numRotors() + 2 > split.length) {
            throw new EnigmaException("Not enough rotors");
        }
        M.insertRotors(rotorsToUse);
        if (split[M.numRotors() + 1].length() != _amountofrotors - 1) {
            throw new EnigmaException("Setting length is wrong.");
        }
        M.setRotors(split[M.numRotors() + 1]);
        M.setPlugboard(new Permutation(swappy, _alphabet));
    }


    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        int len = msg.length();
        for (int i = 0; i < len; i += 5) {
            int stop = len - i;
            if (stop <= 5) {
                _output.println(msg.substring(i, i + stop));
            } else {
                _output.print((msg.substring(i, i + 5) + " "));
            }
        }
    }
}
