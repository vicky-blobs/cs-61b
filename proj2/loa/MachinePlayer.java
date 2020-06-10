/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.ArrayList;

import static loa.Piece.*;

/** An automated Player.
 *  @author Vicky Yu
 */
class MachinePlayer extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;

    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** Machine's side. */
    private static Piece _side;

    /** Machine's depth. */
    private int _depth;

    /** A new MachinePlayer with no piece or controller (intended to produce
     *  a template). */
    MachinePlayer() {
        this(null, null);
    }

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
        _side = side;
    }

    @Override
    String getMove() {
        Move choice;

        assert side() == getGame().getBoard().turn();
        int depth;
        choice = searchForMove();
        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private Move searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert side() == work.turn();
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        return _foundMove;
    }

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        if (depth == 0 || getBoard().gameOver()) {
            return eval(board);
        }
        int bestScore = 0;
        for (Move moves: board.legalMoves()) {
            Board ref = new Board();
            ref.copyFrom(board);
            ref.makeMove(moves);
            int score = findMove(ref, depth - 1, false,
                    -1 * sense, alpha, beta);
            if (saveMove) {
                saveMove = false;
                if (score >= bestScore) {
                    bestScore = score;
                    _foundMove = moves;
                    _scores.add(score);
                }
                if (sense == 1) {
                    alpha = Math.max(score, alpha);
                } else {
                    beta = Math.min(score, beta);
                }
                if (alpha >= beta) {
                    break;
                }
            }
            if (depth == _depth) {
                saveMove = true;
            }
            if (_foundMove == null) {
                _foundMove = moves;
            }
        }
        return bestScore;
    }

    /** Returns an integer as an evaluation of the board.
     * @return int
     * @param board */
    private static int eval(Board board) {
        int diff = board.contSize(_side) - board.contSize(_side.opposite());
        return diff;
    }

    /** Return a search depth for the current position. */
    private int chooseDepth() {
        _depth = 1;
        return _depth;
    }

    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

    /** Arraylist of psuedo-scores found during loop. */
    private ArrayList<Integer> _scores = new ArrayList<>();

    /** Arraylist of legal moves. */
    private ArrayList<Move> _legalM = new ArrayList<>();


}
