package graph;
/*
 * Author: James Luo-Hermanson
 * Date: 08/09/2025
 * Purpose: This min-heap stores values and their associated priorities, where the lower the value of p
 * the higher it's priority. This data structure enables a user to store a set of unique
 * values and their corresponding priorties, add and remove values from the set, confirm if a value exists in the
 * set, and to update a value's priority.
 */
import java.util.NoSuchElementException;

/** An instance is a min-heap of distinct values of type V with
 *  priorities of type P. Since it's a min-heap, the value
 *  with the smallest priority is at the root of the heap. */
public final class Heap<V, P extends Comparable<P>> {

    // following comment:

    /**
     * The contents of c represent a complete binary tree. We use square-bracket
     * shorthand to denote indexing into the AList (which is actually
     * accomplished using its get method. In the complete tree,
     * c[0] is the root; c[2i+1] is the left child of c[i] and c[2i+2] is the
     * right child of i.  If c[i] is not the root, then c[(i-1)/2] (using
     * integer division) is the parent of c[i].
     *
     * Class Invariants:
     *
     *   The tree is complete:
     *     1. `c[0..c.size()-1]` are non-null
     *
     *   The tree satisfies the heap property:
     *     2. if `c[i]` has a parent, then `c[i]`'s parent's priority
     *        is smaller than `c[i]`'s priority
     *
     *   In Phase 3, the following class invariant also must be maintained:
     *     3. The tree cannot contain duplicate *values*; note that dupliate
     *        *priorities* are still allowed.
     *     4. map contains one entry for each element of the heap, so
     *        map.size() == c.size()
     *     5. For each value v in the heap, its map entry contains in
     *        the index of v in c. Thus: map.get(c[i]) = i.
     */
    protected AList<Entry> c;
    protected HashTable<V, Integer> map;

    /** Constructor: an empty heap with capacity 10. */
    public Heap() {
        c = new AList<Entry>(10);
        map = new HashTable<V, Integer>();
    }

    /** An Entry contains a value and a priority. */
    class Entry {
        public V value;
        public P priority;

        /** An Entry with value v and priority p*/
        Entry(V v, P p) {
            value = v;
            priority = p;
        }

        public String toString() {
            return value.toString();
        }
    }

    /** Add v with priority p to the heap.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap. Precondition: p is not null.
     *  In Phase 3 only:
     *  @throws IllegalArgumentException if v is already in the heap.*/
    public void add(V v, P p) throws IllegalArgumentException {
        Entry e = new Entry(v,p);
        //Check if v is already in heap
        if (map.containsKey(v)){
           throw new IllegalArgumentException();
        }
        //Add v to heap and underlying HashTable
        //bubble up v in Heap if needed
        c.append(e);
        int eIdx = c.size()-1;
        map.put(v,eIdx);
        bubbleUp(eIdx);
    }

    /** Return the number of values in this heap.
     *  This operation takes constant time. */
    public int size() {
        return c.size();
    }

    /** Swap c[h] and c[k].
     *  precondition: h and k are >= 0 and < c.size() */
    protected void swap(int h, int k) {
        //Put value at c[h] at c[k]
        //Put value at c[k] at c[h]
        Entry temp = c.get(h);
        c.put(h,c.get(k));
        c.put(k,temp);
        //Keep hash table in sync with changes
        map.put(c.get(h).value,h);
        map.put(c.get(k).value,k);
    }

    /** Bubble c[k] up in heap to its right place.
     *  Precondition: Priority of every c[i] >= its parent's priority
     *                except perhaps for c[k] */
    protected void bubbleUp(int k) {

        while (k > 0) {
            int parentIdx = (k-1)/2;
            Entry parent = c.get(parentIdx);
            Entry entry = c.get(k);

            //Check if c[k]'s parent priority is less (lower) than
            // c[k] priority. If yes, swap with parent
            if (entry.priority.compareTo(parent.priority) < 0){
                swap(k,parentIdx);
                k = parentIdx;
            } else {
                break;
            }

        }
    }

    /** Return the value of this heap with lowest priority. Do not
     *  change the heap. This operation takes constant time.
     *  @throws NoSuchElementException if the heap is empty. */
    public V peek() throws NoSuchElementException {
        if (size() == 0){
            throw new NoSuchElementException();
        }else return c.get(0).value;
    }

    /** Remove and return the element of this heap with lowest priority.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  @throws NoSuchElementException if the heap is empty. */
    public V poll() throws NoSuchElementException {
        if (size() == 0) {
            throw new NoSuchElementException();
        } else {
            //Save lowest priorty entry in Heap
            //Remove it from HashTable
            Entry firstEntry = c.get(0);
            map.remove(firstEntry.value);
            //Pop off and save entry at end of Heap
            //Temporarily remove from Hashtable
            Entry lastEntry = c.pop();
            map.remove(lastEntry.value);

            if (size() > 0){
                //Put last entry in first index in Heap
                //Add back to HashTable with new index
                //Bubble down if there is more than one entry in Heap
                c.put(0,lastEntry);
                map.put(lastEntry.value,0);
                if (size() > 1){
                    bubbleDown(0);
                }
            }
            return firstEntry.value;
        }
    }

    /** Get left child of c[k]
     *  Null returned if right child does not exist
     *  Precondition: c[i] is not null*/
    private Entry getLeftChild(int k){
        int leftChildIdx = k*2 + 1;
        boolean hasLeftChild  = leftChildIdx < size();
        return hasLeftChild ? c.get(leftChildIdx) : null;
    }

    /** Get right child of c[k]
     *  Null returned if right child does not exist
     *  Precondition: c[i] is not null*/
    private Entry getRightChild(int k){
        int rightChildIdx = k*2 + 2;
        boolean hasRightChild  = rightChildIdx < size();
        return hasRightChild ? c.get(rightChildIdx) : null;
    }

    /** Flag to indicate if Entry at c[k] has a left or right child
     *  Null returned if Entry at c[k] has no children*/
    private boolean hasChildren (int k){
        return getLeftChild(k) != null || getRightChild(k) != null;
    }

    /** Bubble c[k] down in heap until it finds the right place.
     *  If there is a choice to bubble down to both the left and
     *  right children (because their priorities are equal), choose
     *  the right child.
     *  Precondition: Each c[i]'s priority <= its childrens' priorities
     *                except perhaps for c[k] */
    protected void bubbleDown(int k) {
        Entry entry = c.get(k);

        while (hasChildren(k)){
            //Get smaller child
            int smallerChildIdx = smallerChild(k);
            Entry smallerChild = c.get(smallerChildIdx);

            //Compare child to entry at c[k].
            //If child priority is higher (i.e., lower value) than entry at c[k],
            //Swap c[k] with child
            if (entry.priority.compareTo(smallerChild.priority) > 0){
                swap(k,smallerChildIdx);
                k = smallerChildIdx;
            } else{
                break;
            }
        }
    }

    /** Return true if the value v is in the heap, false otherwise.
     *  The average case runtime is O(1).  */
    public boolean contains(V v) {
        return map.containsKey(v);
    }

    /** Change the priority of value v to p.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  @throws IllegalArgumentException if v is not in the heap. */
    public void changePriority(V v, P p) throws IllegalArgumentException {
        if (!contains(v)){
            throw new IllegalArgumentException();
        }
        int eIdx = map.get(v);
        Entry e = c.get(eIdx);
        e.priority = p;
        Entry parent = c.get((eIdx-1)/2);

        if (parent.priority.compareTo(e.priority) > 0 ){
            bubbleUp(eIdx);
        } else if (hasChildren(eIdx)) {
            Entry smallerChild = c.get(smallerChild(eIdx));
            if (smallerChild.priority.compareTo(e.priority) < 0){
                bubbleDown(eIdx);
            }
        }
    }

    // Recommended helper method spec:
    /* Return the index of the child of k with smaller priority.
     * if only one child exists, return that child's index
     * Precondition: at least one child exists.*/
    private int smallerChild(int k) {
        //Using 0 based index so use k*2 + 1 or + 2
        int leftChildIdx = k*2 + 1;
        int rightChildIdx = k*2 + 2;
        Entry leftChild = getLeftChild(k);
        Entry rightChild = getRightChild(k);

        if (leftChild == null){
            return rightChildIdx;
        } else if (rightChild == null) {
            return leftChildIdx;
        } else {
            //Tied priority (0) returns right child
            return leftChild.priority.compareTo(rightChild.priority)
                    >= 0 ? rightChildIdx : leftChildIdx;
        }
    }
}
