/**
 * Simple Red-Black tree implementation, where the keys are of type T.
 @ author Vicky Yu
 */
public class RedBlackTree<T extends Comparable<T>> {

    /** Root of the tree. */
    private RBTreeNode<T> root;

    /**
     * Empty constructor.
     */
    public RedBlackTree() {
        root = null;
    }

    /**
     * Constructor that builds this from given BTree (2-3-4) tree.
     *
     * @param tree BTree (2-3-4 tree).
     */
    public RedBlackTree(BTree<T> tree) {
        BTree.Node<T> btreeRoot = tree.root;
        root = buildRedBlackTree(btreeRoot);
    }

    /**
     * Builds a RedBlack tree that has isometry with given 2-3-4 tree rooted at
     * given node r, and returns the root node.
     *
     * @param r root of the 2-3-4 tree.
     * @return root of the Red-Black tree for given 2-3-4 tree.
     */
    RBTreeNode<T> buildRedBlackTree(BTree.Node<T> r) {
        if (r == null) {
            return null;
        }
        if (r.getItemCount() == 1) {
            RBTreeNode toret = new RBTreeNode(true, r.getItemAt(0));
            if (r.getChildrenCount() != 0) {
                toret.right = buildRedBlackTree(r.getChildAt(1));
                toret.left = buildRedBlackTree(r.getChildAt(0));
            }
            return toret;
        } else if (r.getItemCount() == 2) {
            RBTreeNode toret = new RBTreeNode(true, r.getItemAt(0));
            toret.right = new RBTreeNode(false, r.getItemAt(1));
            if (r.getChildrenCount() != 0) {
                toret.right.right = buildRedBlackTree(r.getChildAt(2));
                toret.right.left = buildRedBlackTree(r.getChildAt(1));
                toret.left = buildRedBlackTree(r.getChildAt(0));
            }
            return toret;
        } else if (r.getChildrenCount() == 3) {
            RBTreeNode toret = new RBTreeNode(true, r.getItemAt(1));
            toret.right = new RBTreeNode(false, r.getItemAt(2));
            toret.left = new RBTreeNode(false, r.getItemAt(0));
            if (r.getChildrenCount() != 0) {
                toret.right.right = buildRedBlackTree(r.getChildAt(3));
                toret.right.left = buildRedBlackTree(r.getChildAt(2));
                toret.left.right = buildRedBlackTree(r.getChildAt(1));
                toret.left.left = buildRedBlackTree(r.getChildAt(0));
            }
        }
        return null;
    }

    /**
     * Rotates the (sub)tree rooted at given node to the right, and returns the
     * new root of the (sub)tree. If rotation is not possible somehow,
     * immediately return the input node.
     *
     * @param node root of the given (sub)tree.
     * @return new root of the (sub)tree.
     */
    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {
        RBTreeNode ref = new RBTreeNode<T>(false, node.item, node.left.right, node.right);
        node = new RBTreeNode<T>(node.isBlack, node.left.item, node.left.left, ref);
        return node;
    }

    /**
     * Rotates the (sub)tree rooted at given node to the left, and returns the
     * new root of the (sub)tree. If rotation is not possible somehow,
     * immediately return the input node.
     *
     * @param node root of the given (sub)tree.
     * @return new root of the (sub)tree.
     */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        RBTreeNode ref = new RBTreeNode<T>(false, node.item, node.left, node.right.left);
        node = new RBTreeNode<T>(node.isBlack, node.right.item, ref, node.right.right);
        return node;
    }

    /**
     * Flips the color of the node and its children. Assume that the node has
     * both left and right children.
     *
     * @param node tree node
     */
    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }

    /**
     * Returns whether a given node is red. null nodes (children of leaf) are
     * automatically considered black.
     *
     * @param node node
     * @return node is red.
     */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

    /**
     * Insert given item into this tree.
     *
     * @param item item
     */
    void insert(T item) {
        root = insert(root, item);
        root.isBlack = true;
    }

    /**
     * Recursivelty insert item into this tree. returns the (new) root of the
     * subtree rooted at given node after insertion. node == null implies that
     * we are inserting a new node at the bottom.
     *
     * @param node node
     * @param item item
     * @return (new) root of the subtree rooted at given node.
     */
    private RBTreeNode<T> insert(RBTreeNode<T> node, T item) {

        // Insert (return) new red leaf node.
        if (node == null) {
            node = new RBTreeNode(false, item);
        }
        // Handle normal binary search tree insertion.
        int comp = item.compareTo(node.item);
        if (comp == 0) {
            return node; // do nothing.
        } else if (comp < 0) {
            node.left = insert(node.left, item);
        } else {
            node.right = insert(node.right, item);
        }
        // handle case C and "Right-leaning" situation.
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        // handle case B
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        // handle case A
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }
        return node;
    }

    /** Public accesser method for the root of the tree.*/
    public RBTreeNode<T> graderRoot() {
        return root;
    }


    /**
     * RedBlack tree node.
     *
     * @param <T> type of item.
     */
    static class RBTreeNode<T> {

        /** Item. */
        protected final T item;

        /** True if the node is black. */
        protected boolean isBlack;

        /** Pointer to left child. */
        protected RBTreeNode<T> left;

        /** Pointer to right child. */
        protected RBTreeNode<T> right;

        /**
         * A node that is black iff BLACK, containing VALUE, with empty
         * children.
         */
        RBTreeNode(boolean black, T value) {
            this(black, value, null, null);
        }

        /**
         * Node that is black iff BLACK, contains VALUE, and has children
         * LFT AND RGHT.
         */
        RBTreeNode(boolean black, T value,
                   RBTreeNode<T> lft, RBTreeNode<T> rght) {
            isBlack = black;
            item = value;
            left = lft;
            right = rght;
        }
    }

}