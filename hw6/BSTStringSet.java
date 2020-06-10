
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Vicky Yu
 */
public class BSTStringSet implements SortedStringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        _root = put(s, _root);
    }

    /** put helper */
    private Node put(String s, Node node) {
        if (_root == null) {
            return new Node(s);
        }
        if (s.compareTo(node.s) > 0) {
            if (node.right == null) {
                node.right = new Node(s);
            }
            else {
                node.right = put(s, node.right);
            }
        }
        if (s.compareTo(node.s) < 0) {
            if (node.left == null) {
                node.left = new Node(s);
            }
            else {
                node.left = put(s, node.left);
            }
        }
        return node;
    }

    @Override
    public boolean contains(String s) {
        return contains(s, _root);
    }

    /** contains helper */
    private boolean contains(String s, Node r) {
        if (r == null) {
            return false;
        }
        if (s.compareTo(r.s) > 0) {
            return contains(s, r.right);
        }
        if (s.compareTo(r.s) <0) {
            return contains(s, r.left);
        }
        return true;
    }


    @Override
    public List<String> asList() {
        ArrayList<String> toret = new ArrayList<>();
        for (String each : this) {
            toret.add(each);
        }
        return toret;
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new NullPointerException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    @Override
    public Iterator<String> iterator(String low, String high) {
        return new imIterator(low, high, _root);
    }

    private static class imIterator implements Iterator<String> {

        private String l;
        private String h;
        private Node n;
        private Stack<Node> add = new Stack<>();

        imIterator(String low, String high, Node node) {
            l = low;
            h = high;
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !add.isEmpty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new Error();
            }
            Node next = add.pop();
            addTree(next.right);
            return next.s;
        }

        private void addTree(Node n) {
            if (n != null) {
                int comparel = n.s.compareTo(l);
                int compareh = n.s.compareTo(h);
                if (comparel < 0) {
                    addTree(n.right);
                }
                else if (compareh >= 0) {
                    addTree(n.left);
                }
                else {
                    add.push(n);
                    addTree(n.left);
                }
            }
        }
    }

    /** Root node of the tree. */
    private Node _root;
}
