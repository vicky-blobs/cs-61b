import java.util.ArrayList;

/** A Generic heap class. Unlike Java's priority queue, this heap doesn't just
 * store Comparable objects. Instead, it can store any type of object
 * (represented by type T) and an associated priority value.
 * @author Vicky Yu */
public class ArrayHeap<T> {

    /* DO NOT CHANGE THESE METHODS. */

    /** An ArrayList that stores the nodes in this binary heap. */
    private ArrayList<Node> contents;

    /** A constructor that initializes an empty ArrayHeap. */
    public ArrayHeap() {
        contents = new ArrayList<>();
        contents.add(null);
    }

    /** Returns the number of elements in the priority queue. */
    public int size() {
        return contents.size() - 1;
    }

    /** Returns the node at index INDEX. */
    private Node getNode(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.get(index);
        }
    }

    /** Sets the node at INDEX to N */
    private void setNode(int index, Node n) {
        // In the case that the ArrayList is not big enough
        // add null elements until it is the right size
        while (index + 1 > contents.size()) {
            contents.add(null);
        }
        contents.set(index, n);
    }

    /** Returns and removes the node located at INDEX. */
    private Node removeNode(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.remove(index);
        }
    }

    /** Swap the nodes at the two indices. */
    private void swap(int index1, int index2) {
        Node node1 = getNode(index1);
        Node node2 = getNode(index2);
        this.contents.set(index1, node2);
        this.contents.set(index2, node1);
    }

    /** Prints out the heap sideways. Use for debugging. */
    @Override
    public String toString() {
        return toStringHelper(1, "");
    }

    /** Recursive helper method for toString. */
    private String toStringHelper(int index, String soFar) {
        if (getNode(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = getRightOf(index);
            toReturn += toStringHelper(rightChild, "        " + soFar);
            if (getNode(rightChild) != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + getNode(index) + "\n";
            int leftChild = getLeftOf(index);
            if (getNode(leftChild) != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    /** A Node class that stores items and their associated priorities. */
    public class Node {
        private T _item;
        private double _priority;

        private Node(T item, double priority) {
            this._item = item;
            this._priority = priority;
        }

        public T item() {
            return this._item;
        }

        public double priority() {
            return this._priority;
        }

        public void setPriority(double priority) {
            this._priority = priority;
        }

        @Override
        public String toString() {
            return this._item.toString() + ", " + this._priority;
        }
    }

    /* FILL IN THE METHODS BELOW. */

    /** Returns the index of the left child of the node at i. */
    private int getLeftOf(int i) {
        return 2*i;
    }

    /** Returns the index of the right child of the node at i. */
    private int getRightOf(int i) {
        return (2*i) + 1;
    }

    /** Returns the index of the node that is the parent of the
     *  node at i. */
    private int getParentOf(int i) {
        return i/2;
    }

    /** Returns the index of the node with smaller priority. If one
     * node is null, then returns the index of the non-null node.
     * Precondition: at least one of the nodes is not null. */
    private int min(int index1, int index2) {
        if (getNode(index1) != null) {
            if (getNode(index2) != null) {
                double one = getNode(index1)._priority;
                double two = getNode(index2)._priority;
                if (one < two) {
                    return index1;
                }
                else {
                    return index2;
                }
            }
            return index1;
        }
        return index2;
    }

    /** Returns the item with the smallest priority value, but does
     * not remove it from the heap. If multiple items have the minimum
     * priority value, returns any of them. Returns null if heap is
     * empty. */
    public T peek() {
        if (getNode(1) != null) {
            return getNode(1)._item;
        }
        return null;
    }

    /** Bubbles up the node currently at the given index until no longer
     *  needed. */
    private void bubbleUp(int index) {
        if (index == 1) {
            return;
        }
        int me = (int) getNode(index)._priority;
        int parent = (int) getNode(getParentOf(index))._priority;
        if (me < parent) {
            swap(index, getParentOf(index));
        }
        bubbleUp(getParentOf(index));
    }

    /** Bubbles down the node currently at the given index until no longer
     *  needed. */
    private void bubbleDown(int index) {
        int me = (int) getNode(index)._priority;
        int toswap = min(getLeftOf(index), getRightOf(index));
        if (getNode(toswap) == null) {
            return;
        }
        if (me > getNode(toswap)._priority) {
            swap(index, toswap);
        }
        bubbleDown(toswap);
    }

    /** Inserts an item with the given priority value. Assume that item is
     * not already in the heap. Same as enqueue, or offer. */
    public void insert(T item, double priority) {
         Node toIns = new Node(item, priority);
         int pos = 1;
         while (getNode(pos) != null) {
             pos += 1;
         }
         setNode(pos, toIns);
         bubbleUp(pos);
    }

    /** Returns the element with the smallest priority value, and removes
     * it from the heap. If multiple items have the minimum priority value,
     * removes any of them. Returns null if the heap is empty. Same as
     * dequeue, or poll. */
    public T removeMin() {
        if (getNode(1) == null) {
            return null;
        }
        int last = 1;
        while (getNode(last) != null) {
            last += 1;
        }
        swap(1, last-1);
        T min = getNode(last - 1)._item;
        removeNode(last - 1);
        if (last == 2) {
            return min;
        }
        bubbleDown(1);
        return min;
    }

    /** Changes the node in this heap with the given item to have the given
     * priority. You can assume the heap will not have two nodes with the
     * same item. Does nothing if the item is not in the heap. Check for
     * item equality with .equals(), not == */
    public void changePriority(T item, double priority) {
        int check = 1;
        Node toins = new Node(item, priority);
        while (!getNode(check)._item.equals(item)) {
            check += 1;
        }
        setNode(check, toins);
        int parent = getParentOf(check);
        int children = min(getLeftOf(check), getRightOf(check));
        if (getNode(children) != null) {
            if (getNode(check)._priority > getNode(children)._priority) {
                bubbleDown(check);
            }
        }
        else if (getNode(check)._priority < getNode(parent)._priority) {
            bubbleUp(check);
        }
    }
}
