package graph;

import java.util.HashMap;
import java.util.Map;

/** A Node class for a adjacency-list representation of a graph.  Nodes are
 * identified by a unique String identifier and edges are stored as a Neighbor
 * map that associates each neighboring node with the weight of the edge to
 * that node. It is the responsibility of the user of this class to avoid
 * making multiple Nodes with the same unique identifier. */
public class Node {

    private final String id; // unique identifier for this node

    // for each node v that has an edge from this to v, neighbors maps
    //  v -> the weight of the edge
     private HashMap<Node,Double> neighbors;

    /** Constructor: create node with the given id */
    public Node(String id) {
        this.id = id;
        neighbors = new HashMap<Node,Double>();
    }

    /** Return this node's unique identifier */
    public String getId() {
        return id;
    }

    /** Return the map that associates each neighbor with the weight of the
     * edge to that neighbor. */
    public HashMap<Node,Double> getNeighbors() {
        return neighbors;
    }

    /** Add an edge to neighbor with the given weight. If such an edge already
     * existed, update its weight. */
    public void addNeighbor(Node neighbor, double weight) {
        neighbors.put(neighbor, weight);
    }

    /** returns the Node's unique identifier */
    @Override
    public String toString() {
        return id;
    }

    /** equals: two nodes are equal if their unique ids are equal */
    @Override
    public boolean equals(Object ob) {
      if (ob == null || !(ob instanceof Node)) {
        return false;
      }
      return id.equals(((Node)ob).id);
    }

    /** Hashes the unique id */
    @Override
    public int hashCode() {
      return id.hashCode();
    }


//Below for analysis only
    public static void main(String[] args) {
        class PathData1 {
            double distance; // distance of the shortest path from source
            Node previous; // previous node in the path from the source

            /** constructor: initialize distance and previous node */
            public PathData1(double dist, Node prev) {
                distance = dist;
                previous = prev;
            }
        }
        HashMap<Node,PathData1> paths1 = new HashMap<>();

        Node a = new Node ("a");
        Node b = new Node ("b");
        Node c = new Node ("c");
        Node d = new Node ("d");
        a.addNeighbor(b,2);
        a.addNeighbor(c,3);
        a.addNeighbor(d,2);

        paths1.put(a,new PathData1(0,null));
        paths1.put(b,new PathData1(2,a));
        paths1.put(c,new PathData1(5,b));



        System.out.println("a neighbors");
        a.getNeighbors().forEach((n,value) -> {
            System.out.println("Neighbor " + n + " | weight " + value);
        });

        System.out.println("paths");

        paths1.forEach((n, value) -> {
            double distance = value.distance;
            Node bp = value.previous;
            System.out.println(n + " " + distance + " " + bp);
        });

        paths1.get(c).distance = 4;

        System.out.println();

        paths1.forEach((n, value) -> {
            double distance = value.distance;
            Node bp = value.previous;
            System.out.println(n + " " + distance + " " + bp);
        });



    }
}
