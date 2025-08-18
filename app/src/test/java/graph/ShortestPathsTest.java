package graph;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.net.URL;
import java.io.FileNotFoundException;

import java.util.LinkedList;
import java.util.stream.Collectors;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShortestPathsTest {

    /* Performs the necessary gradle-related incantation to get the
       filename of a graph text file in the src/test/resources directory at
       test time.*/
    private String getGraphResource(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return resource.getPath();
    }

    /* Returns the Graph loaded from the file with filename fn located in
     * src/test/resources at test time. */
    private Graph loadBasicGraph(String fn) {
        Graph result = null;
        String filePath = getGraphResource(fn);
        try {
          result = ShortestPaths.parseGraph("basic", filePath);
        } catch (FileNotFoundException e) {
          fail("Could not find graph " + fn);
        }
        return result;
    }

    private void printShortestPath (LinkedList<Node> path){
        System.out.print(path.stream()
                .map(Object::toString)
                .collect(Collectors.joining("->")));
        System.out.println();
    }

    /** Dummy test case demonstrating syntax to create a graph from scratch.
     * Write your own tests below. */
    @Test
    public void test00Nothing() {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        g.addEdge(a, b, 1);

        // sample assertion statements:
        assertTrue(true);
        assertEquals(2+2, 4);
    }

    /** Minimal test case to check the path from A to B in Simple0.txt */
    @Test
    public void test01Simple0() {
        Graph g = loadBasicGraph("Simple0.txt");
        System.out.println("-------Test case test01Simple0 Start------- ");
        g.report();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        sp.compute(a);
        Node b = g.getNode("B");
        LinkedList<Node> abPath = sp.shortestPath(b);
        assertEquals(abPath.size(), 2);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(),  b);
        assertEquals(sp.shortestPathLength(b), 1.0, 1e-6);
        printShortestPath(abPath);
    }

    @Test
    public void test02Simple0() {
        Graph g = loadBasicGraph("Simple0.txt");
        System.out.println("-------Test case test01Simple0 Start------- ");
        g.report();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        sp.compute(a);
        Node c = g.getNode("C");
        LinkedList<Node> abPath = sp.shortestPath(c);
        assertEquals(abPath.size(), 2);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(),  c);
        assertEquals(sp.shortestPathLength(c), 2.0, 1e-6);
        printShortestPath(abPath);
    }

    //Test one node graph where origin and destination are the same
    @Test
    public void test03Test1() {
        Graph g = loadBasicGraph("Test1.txt");
        System.out.println("-------Test case test03Test1 Start------- ");
        g.report();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        sp.compute(a);
        LinkedList<Node> abPath = sp.shortestPath(a);
        assertEquals(abPath.size(), 1);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(),  a);
        assertEquals(sp.shortestPathLength(a), 0.0, 1e-6);
        printShortestPath(abPath);
    }

    @Test
    //Test unreachable node as destination
    public void test04Test2() {
        Graph g = loadBasicGraph("Test2.txt");
        System.out.println("-------Test case test04Test2 Start------- ");
        g.report();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        sp.compute(a);
        Node d = g.getNode("D");
        Node e = g.getNode("E");
        LinkedList<Node> abPath = sp.shortestPath(d);
        assertNull(abPath);
        assertEquals(sp.shortestPathLength(d), Double.POSITIVE_INFINITY, 1e-6);
        LinkedList<Node> abPath1 = sp.shortestPath(e);
        assertNull(abPath1);
        assertEquals(sp.shortestPathLength(e), Double.POSITIVE_INFINITY, 1e-6);

    }

    @Test
    //Test where origin and destination are the same with multi-node graph
    public void test05SimpleTest1() {
        Graph g = loadBasicGraph("Simple1.txt");
        System.out.println("-------Test case test05SimpleTest1 Start------- ");
        g.report();
        ShortestPaths sp = new ShortestPaths();
        Node s = g.getNode("S");
        sp.compute(s);
        LinkedList<Node> abPath = sp.shortestPath(s);
        assertEquals(abPath.size(), 1);
        assertEquals(abPath.getFirst(), s);
        assertEquals(abPath.getLast(),  s);
        assertEquals(sp.shortestPathLength(s), 0.0, 1e-6);
        printShortestPath(abPath);
    }

    @Test
    //Test shortest path with multiple path to destination
    public void test06SimpleTest1() {
        Graph g = loadBasicGraph("Simple1.txt");
        System.out.println("-------Test case test06SimpleTest1 Start------- ");
        g.report();
        ShortestPaths sp = new ShortestPaths();
        Node s = g.getNode("S");
        Node d = g.getNode("D");
        sp.compute(s);
        //Test shortest path to D, which is S->C->D length 7
        LinkedList<Node> abPath = sp.shortestPath(d);
        assertEquals(abPath.size(), 3);
        assertEquals(abPath.getFirst(), s);
        assertEquals(abPath.getLast(),  d);
        assertEquals(sp.shortestPathLength(d), 7.0, 1e-6);
        System.out.print("Shortest path from S to D: ");
        printShortestPath(abPath);

        //Test shortest path to B, which is S->C->A->B length 9
        Node b = g.getNode("B");
        LinkedList<Node> abPath1 = sp.shortestPath(b);
        assertEquals(abPath1.size(), 4);
        assertEquals(abPath1.getFirst(), s);
        assertEquals(abPath1.getLast(),  b);
        assertEquals(sp.shortestPathLength(b), 9.0, 1e-6);
        System.out.print("Shortest path from S to B: ");
        printShortestPath(abPath1);
    }

}
