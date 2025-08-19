package graph;

/** A hash table modeled after java.util.Map. It uses chaining for collision
 * resolution and grows its underlying storage by a factor of 2 when the load
 * factor exceeds 0.8. */
public class HashTable<K,V> {

    protected Pair[] buckets; // array of list nodes that store K,V pairs
    protected int size; // how many items currently in the map


    /** class Pair stores a key-value pair and a next pointer for chaining
     * multiple values together in the same bucket, linked-list style*/
    public class Pair {
        protected K key;
        protected V value;
        protected Pair next;

        /** constructor: sets key and value */
        public Pair(K k, V v) {
            key = k;
            value = v;
            next = null;
        }

        /** constructor: sets key, value, and next */
        public Pair(K k, V v, Pair nxt) {
            key = k;
            value = v;
            next = nxt;
        }

        /** returns (k, v) String representation of the pair */
        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }

    /** constructor: initialize with default capacity 17 */
    public HashTable() {
        this(17);
    }

    /** constructor: initialize the given capacity */
    public HashTable(int capacity) {
        buckets = createBucketArray(capacity);
    }

    /** Return the size of the map (the number of key-value mappings in the
     * table) */
    public int getSize() {
        return size;
    }

    /** Return the current capacity of the table (the size of the buckets
     * array) */
    public int getCapacity() {
        return buckets.length;
    }

    /** Return the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     * Runtime: average case O(1); worst case O(size) */
    public V get(K key) {
        //Hash V value into a key
        //Hash to index of bucket in HashTable using
        // mod of key hash and table size
        int keyHash = key.hashCode();
        int keyIdx = Math.absExact(keyHash % getCapacity());
        Pair node = buckets[keyIdx];
        boolean containsNode = node != null;

        // Search through each node in bucket
        // to determine if any has matching key.
        //Return value of matching key (which is index of value in Heap) if found
        //Otherwise return null (value is not in HashTable or Heap)
        if (containsNode) {
            while (node != null) {
                if (node.key.equals(key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    /** Associate the specified value with the specified key in this map. If
     * the map previously contained a mapping for the key, the old value is
     * replaced. Return the previous value associated with key, or null if
     * there was no mapping for key. If the load factor exceeds 0.8 after this
     * insertion, grow the array by a factor of two and rehash.
     * Precondition: val is not null.
     * Runtime: average case O(1); worst case O(size + a.length)*/
    public V put(K key, V val) {
        //Hash V value into a key
        //Hash to index of bucket in HashTable using
        // mod of key hash and table size
        int keyHash = key.hashCode();
        int keyIdx = Math.absExact(keyHash % getCapacity());
        Pair putNode = new Pair(key, val);
        boolean containsNode = buckets[keyIdx] != null;

        // When nodes exist, search through each node in bucket to determine if any has matching key.
        //If  matching key is found, update existing node's value with val
        //Else, chain the (key , val) pair to the last node in the bucket
        if (containsNode) {
            Pair node = buckets[keyIdx];
            while (node != null) {
                if (node.key.equals(key)) {
                    V oldValue = node.value;
                    node.value = val;
                    return oldValue;
                } else if (node.next == null) {
                    node.next = putNode;
                    size++;
                    growIfNeeded();
                    return null;
                }
                node = node.next;
            }
        }
        // If no nodes exist in bucket, add key, value as first node.
        buckets[keyIdx] = putNode;
        size++;
        growIfNeeded();
        return null;
    }

    /** Return true if this map contains a mapping for the specified key.
     *  Runtime: average case O(1); worst case O(size) */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /** Remove the mapping for the specified key from this map if present.
     *  Return the previous value associated with key, or null if there was no
     *  mapping for key.
     *  Runtime: average case O(1); worst case O(size)*/
    public V remove(K key) {
        //Check if key (value) in the HashTable
        if (containsKey(key)){
            //Get index of key (value)
            int keyHash = key.hashCode();
            int keyIdx = Math.absExact(keyHash % getCapacity());
            Pair node = buckets[keyIdx];

            //Search through each node in bucket to find matching key.
            //Set the node to null (i.e., remove it)
            //Update node's next pointer if next pointer not null
            //Reduce size of table
            if (node.key.equals(key)){
                V oldValue = node.value;
                buckets[keyIdx] = node.next != null ?
                        node.next : null;
                size--;
                return oldValue;
            }else {
                while (node.next != null) {
                    if (node.next.key.equals(key)) {
                        V oldValue = node.next.value;
                        node.next = node.next.next != null ?
                                node.next.next : null;
                        size--;
                        return oldValue;
                    }
                    node = node.next;
                }
            }
        }
        return null;
    }

    // suggested helper method:
    /* check the load factor; if it exceeds 0.8, double the capacity 
     * and rehash values from the old array to the new array */
    private void growIfNeeded() {
        double loadFactor = (double) getSize() / getCapacity();
        boolean exceedMaxLoadFactor = loadFactor > 0.8;

        //Temporarily store current buckets key/value pairs
        //Double capacity of buckets
        if (exceedMaxLoadFactor){
            Pair[] oldBuckets = buckets;
            int newCapacity = getCapacity() * 2;
            buckets = createBucketArray(newCapacity);
            size = 0;

            //Rehash all entries from old buckets back into
            //New, larger buckets HashTable
            for (Pair bucket : oldBuckets) {
                Pair node = bucket;
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    /* useful method for debugging - prints a representation of the current
     * state of the hash table by traversing each bucket and printing the
     * key-value pairs in linked-list representation */
    protected void dump() {
        System.out.println("Table size: " + getSize() + " capacity: " +
                getCapacity());
        for (int i = 0; i < buckets.length; i++) {
            System.out.print(i + ": --");
            Pair node = buckets[i];
            while (node != null) {
                System.out.print(">" + node + "--");
                node = node.next;

            }
            System.out.println("|");
        }
    }

    /*  Create and return a bucket array with the specified size, initializing
     *  each element of the bucket array to be an empty LinkedList of Pairs.
     *  The casting and warning suppression is necessary because generics and
     *  arrays don't play well together.*/
    @SuppressWarnings("unchecked")
    protected Pair[] createBucketArray(int size) {
        return (Pair[]) new HashTable<?,?>.Pair[size];
    }
}
