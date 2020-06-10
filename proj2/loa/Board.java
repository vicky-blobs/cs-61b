/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author Vicky Yu
 */
class Board {

    /** Default number of moves for each side that results in a draw. */
    static final int DEFAULT_MOVE_LIMIT = 60;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this();
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        _moves.clear();
        int dim = contents.length;
        for (int r = 0; r < dim; r += 1) {
            for (int c = 0; c < dim; c += 1) {
                set(Square.sq(c, r), contents[r][c]);
            }
        }
        _turn = side;
        _moveLimit = DEFAULT_MOVE_LIMIT;
    }

    /** Set me to the initial configuration. */
    void clear() {
        _moves.clear();
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }
        _moves.clear();
        _moves.addAll(board._moves);
        _turn = board._turn;
        int size = board._board.length;
        for (int i = 0; i < size; i += 1) {
            _board[i] = board._board[i];
        }
    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        _board[sq.index()] = v;
        if (next != null) {
            _turn = next;
        }
    }

    /** Set the square at SQ to V, without modifying the side that
     *  moves next. */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /** Set limit on number of moves by each side that results in a tie to
     *  LIMIT, where 2 * LIMIT > movesMade(). */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = 2 * limit;
    }

    /** Assuming isLegal(MOVE), make MOVE. This function assumes that
     *  MOVE.isCapture() will return false.  If it saves the move for
     *  later retraction, makeMove itself uses MOVE.captureMove() to produce
     *  the capturing move. */
    void makeMove(Move move) {
        assert isLegal(move);
        _turn = _turn.opposite();
        _capturing.add(false);
        _captured.add(null);
        _moves.add(move);
        Piece start = get(move.getFrom());
        Piece dest = get(move.getTo());
        if (dest != EMP) {
            _capturing.add(true);
            _captured.add(dest);
            move.captureMove();
        }
        set(move.getFrom(), EMP);
        set(move.getTo(), start);
        _subsetsInitialized = false;
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        if (movesMade() < 1) {
            return;
        }
        _turn = _turn.opposite();
        Move prev = _moves.remove(_moves.size() - 1);
        Boolean capture = _capturing.remove(_capturing.size() - 1);
        Piece placed = get(prev.getTo());
        if (capture) {
            Piece putBack = _captured.get(_captured.size() - 1);
            set(prev.getTo(), putBack);
            set(prev.getFrom(), placed);
            return;
        }
        set(prev.getTo(), EMP);
        set(prev.getFrom(), placed);
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        if (from == null || to == null) {
            return false;
        }
        int counter = 1;
        int steps = from.distance(to);
        int forDir = from.direction(to);
        int revDir = (forDir + 4) % 8;
        for (int i = 1; i < _board.length; i++) {
            Square check = from.moveDest(forDir, i);
            if (check == null) {
                break;
            } else if (get(check) != EMP) {
                counter++;
            }
        }
        for (int i = 1; i < _board.length; i++) {
            Square check = from.moveDest(revDir, i);
            if (check == null) {
                break;
            } else if (get(check) != EMP) {
                counter++;
            }
        }
        if (blocked(from, to)) {
            return false;
        } else {
            return counter == steps;
        }
    }

    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /** Return a sequence of all legal moves from this position. */
    List<Move> legalMoves() {
        ArrayList<Move> legalMoves = new ArrayList<>();
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                Square on = Square.sq(col, row);
                Piece color = get(on);
                if (color == _turn) {
                    for (int dir = 0; dir < 9; dir++) {
                        for (int steps = 1; steps < _board.length; steps++) {
                            Square dest = on.moveDest(dir, steps);
                            if (dest != null && isLegal(on, dest)) {
                                Move toAdd = Move.mv(on, dest);
                                legalMoves.add(toAdd);
                            }
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    /** Return true iff the game is over (either player has all his
     *  pieces continguous or there is a tie). */
    boolean gameOver() {
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        return getRegionSizes(side).size() == 1;
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!_winnerKnown) {
            _winnerKnown = true;
            if (piecesContiguous(BP)) {
                _winner = BP;
            }
            if (piecesContiguous(WP)) {
                _winner = WP;
            }
            if (_moves.size() >= _moveLimit) {
                _winner = EMP;
            }
        }
        if (_winner == null) {
            _winnerKnown = false;
        }
        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        Piece me = get(from);
        Piece oppo = me.opposite();
        int dir = from.direction(to);
        int steps = from.distance(to);
        if (get(to) == me) {
            return true;
        } else if (from.isValidMove(to)) {
            for (int i = 1; i < steps; i++) {
                Square check = from.moveDest(dir, i);
                if (check == null) {
                    return true;
                } else if (get(check) == oppo) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    private int numContig(Square sq, boolean[][] visited, Piece p) {
        int col = sq.col();
        int row = sq.row();
        int counter = 1;
        if (p == EMP) {
            return 0;
        }
        if (get(sq) != p) {
            return 0;
        }
        if (visited[col][row]) {
            return 0;
        }
        visited[col][row] = true;
        Square[] toVisit = sq.adjacent();
        for (Square visiting : toVisit) {
            counter += numContig(visiting, visited, p);
            visited[visiting.col()][visiting.row()] = true;
        }
        return counter;
    }

    /** Get size of biggest contiguous piece for player.
     * @return int continuity score
     * @param piece */
    public int contSize(Piece piece) {
        computeRegions();
        int size = 1;
        int total = 0;
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                Square on = Square.sq(col, row);
                Piece color = get(on);
                if (color == piece) {
                    total++;
                }
            }
        }
        if (piece == BP) {
            for (int max : _blackRegions) {
                if (max > size) {
                    size = max;
                }
            }
        }
        if (piece == WP) {
            for (int max : _whiteRegions) {
                if (max > size) {
                    size = max;
                }
            }
        }
        size = (int) (size * 100) / total;
        return size;
    }

    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();
        _whiteRegions.clear();
        _blackRegions.clear();
        int blackP = 0;
        int whiteP = 0;
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row++) {
                Square on = Square.sq(col, row);
                Piece color = get(on);
                if (color == BP) {
                    blackP++;
                    boolean[][] visited = new boolean[8][8];
                    int blackeo = numContig(on, visited, color);
                    _blackRegions.add(blackeo);
                }
                if (color == WP) {
                    whiteP++;
                    boolean[][] visited = new boolean[8][8];
                    int whiteo = numContig(on, visited, color);
                    _whiteRegions.add(whiteo);
                }
            }
        }
        if (_blackRegions.contains(blackP)) {
            _blackRegionSizes.add(1);
        }
        if (_whiteRegions.contains(whiteP)) {
            _whiteRegionSizes.add(1);
        }
        winner();
        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
        _subsetsInitialized = true;
    }

    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }


    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** Holds if that move was capturing or not. */
    private ArrayList<Boolean> _capturing = new ArrayList<>();

    /** Holds captured pieces. */
    private ArrayList<Piece> _captured = new ArrayList<>();

    /** Current contents of the board.  Square S is at _board[S.index()]. */
    private final Piece[] _board = new Piece[BOARD_SIZE  * BOARD_SIZE];

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();

    /** Current side on move. */
    private Piece _turn;

    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit = DEFAULT_MOVE_LIMIT;

    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown;

    /** Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;

    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of sizes of continguous clusters. */
    private ArrayList<Integer>
            _whiteRegions = new ArrayList<>(),
            _blackRegions = new ArrayList<>();

    /** List of the sizes of continguous clusters of pieces, by color. */
    private final ArrayList<Integer>
        _whiteRegionSizes = new ArrayList<>(),
        _blackRegionSizes = new ArrayList<>();
}
