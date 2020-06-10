import java.awt.dnd.DnDConstants;

/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        int counter = 0;
        DNode yeet =  _front;
        while (yeet != null) {
            counter += 1;
            yeet = yeet._next;
        }
        return counter;
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
     *          and -size <= i <= -1 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        DNode orange = null;
        if (i < 0) {
           orange = _back;
           while (orange != null && i++ != -1 && orange._prev != null) {
               orange = orange._prev;
           }
        }
        else {
            orange = _front;
            while (orange != null && orange._next != null && i-- != 0) {
                orange = orange._next;
            }
        }
        if (orange != null) {
            return orange._val;
        }
        return 0;
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        DNode newf = new DNode(d);
        DNode prevf = _front;
        newf._next = prevf;
        if (prevf != null) {
            prevf._prev = newf;
        }
        _front = newf;
        if (_back == null) {
            _back = newf;
        }
        /* newx._next = _front;
        newx._prev = null;
        if (_front != null) {
            _front._prev = newx;
        }
        _front = newx; */
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        DNode newb = new DNode(d);
        DNode oldb = _back;
        newb._prev = oldb;
        if (oldb != null) {
            oldb._next = newb;
        }
        _back = newb;
        if (_front == null) {
            _front = newb;
        }
        /*DNode newnew = new DNode(d);
        DNode last = _front;
        if (_front == null) {
            newnew._prev = null;
            _front = newnew;
            return;
        }
        while (last._next != null) {
            last = last._next;
        }
        last._next = newnew;
        newnew._prev = last;
        newnew = _back; */
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     *              and -(size+1) <= index <= -1 for negative indices (including insertions at front and back).
     */
    public void insertAtIndex(int d, int index) {
        DNode boop = new DNode(d);
        if (_front == null) {
            _front = boop;
            _back = boop;
        } else if (index == 0) {
            boop._next = _front;
            _front._prev = boop;
            _front = boop;
        } else if (index > 0) {
            DNode ref = _front;
            for (int i = 1; i < index; i++) {
                ref = ref._next;
            }
            if (ref._next == null) {
                _back = boop;
                ref._next = boop;
                boop._prev = ref;
                boop._prev._next = boop;
            } else {
                boop._next = ref._next;
                ref._next = boop;
                boop._prev = ref;
                boop._next._prev = boop;
            }
        } else {
            DNode bref = _back;
            for (int i = -2; i > index; i--) {
                bref = bref._prev;
            }
            if (bref._prev == null) {
                _front = boop;
                bref._prev = boop;
                boop._next = bref;
                boop._next._prev = boop;
            } else if (bref._next == null) {
                if (index == -1) {
                    _back = boop;
                    bref._next = boop;
                    boop._prev = bref;
                    boop._prev._next = boop;
                }
                else {
                    boop._prev = bref._prev;
                    bref._prev  = boop;
                    boop._next = bref;
                    boop._prev._next = boop;
                    boop._next._prev = boop;
                }
            }
            else {
                boop._next = bref._next;
                bref._next = boop;
                boop._prev = bref;
                boop._next._prev = boop;
            }
        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        DNode oldf = _front;
        DNode fronto = _front;
        _front = fronto._next;
        fronto._prev = null;
        return oldf._val;
    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        DNode old = _back;
        DNode newb = _back;
        if (_front == _back) {
            _front = null;
            _back = null;
            return old._val;
        }
        DNode befor = newb._prev;
        _back = befor;
        _back._next = null;
        return old._val;
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices (including deletions at front and back)
     *              and -size <= index <= -1 for negative indices (including deletions at front and back).
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        // FIXME: Implement this method and return correct value
        return 0;
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        if (size() == 0) {
            return "[]";
        }
        String str = "[";
        DNode curr = _front;
        for (; curr._next != null; curr = curr._next) {
            str += curr._val + ", ";
        }
        str += curr._val +"]";
        return str;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
