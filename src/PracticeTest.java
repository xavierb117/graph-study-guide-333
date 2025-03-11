import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

public class PracticeTest {

  // --- Helper method to build the complex graph (Vertex-based) ---
  // The graph structure is:
  //    v3  = 3
  //    v7  = 7
  //    v12 = 12
  //    v34 = 34
  //    v56 = 56
  //    v78 = 78
  //    v91 = 91
  //    v45 = 45
  //    v23 = 23
  //    v67 = 67  (not connected from v3)
  //
  // Neighbors are assigned as follows:
  //    v3.neighbors  = [v7, v34]
  //    v7.neighbors  = [v12, v45, v34, v56]
  //    v12.neighbors = [v7, v56, v78]
  //    v34.neighbors = [v34, v91]    // self-loop on v34
  //    v56.neighbors = [v78]
  //    v78.neighbors = [v91]
  //    v91.neighbors = [v56]         // cycle between v91 and v56
  //    v45.neighbors = [v23]
  //    v23.neighbors = []
  //    v67.neighbors = [v91]         // v67 is isolated from the rest (not reachable from v3)
  private Vertex<Integer>[] createComplexGraph() {
    Vertex<Integer> v3  = new Vertex<>(3);
    Vertex<Integer> v7  = new Vertex<>(7);
    Vertex<Integer> v12 = new Vertex<>(12);
    Vertex<Integer> v34 = new Vertex<>(34);
    Vertex<Integer> v56 = new Vertex<>(56);
    Vertex<Integer> v78 = new Vertex<>(78);
    Vertex<Integer> v91 = new Vertex<>(91);
    Vertex<Integer> v45 = new Vertex<>(45);
    Vertex<Integer> v23 = new Vertex<>(23);
    Vertex<Integer> v67 = new Vertex<>(67);

    v3.neighbors  = new ArrayList<>(List.of(v7, v34));
    v7.neighbors  = new ArrayList<>(List.of(v12, v45, v34, v56));
    v12.neighbors = new ArrayList<>(List.of(v7, v56, v78));
    v34.neighbors = new ArrayList<>(List.of(v34, v91)); 
    v56.neighbors = new ArrayList<>(List.of(v78));
    v78.neighbors = new ArrayList<>(List.of(v91));
    v91.neighbors = new ArrayList<>(List.of(v56));
    v45.neighbors = new ArrayList<>(List.of(v23));
    v23.neighbors = new ArrayList<>();
    v67.neighbors = new ArrayList<>(List.of(v91));

    // Return an array with v3 as the starting vertex
    return new Vertex[]{v3, v7, v12, v34, v56, v78, v91, v45, v23, v67};
  }

  // --- Tests for oddVertices(Vertex<Integer> starting) ---

  @Test
  public void testOddVertices_NullInput() {
    // When the starting vertex is null, the count should be 0.
    assertEquals(0, Practice.oddVertices(null));
  }

  @Test
  public void testOddVertices_SimpleGraph() {
    // Create a simple graph:
    //   5 --> 4
    //   |     |
    //   v     v
    //   8 --> 7
    //          \
    //           v
    //           9
    // Expected odd vertices from 5: 5, 7, and 9 => count = 3.
    Vertex<Integer> v5 = new Vertex<>(5);
    Vertex<Integer> v4 = new Vertex<>(4);
    Vertex<Integer> v8 = new Vertex<>(8);
    Vertex<Integer> v7 = new Vertex<>(7);
    Vertex<Integer> v9 = new Vertex<>(9);
    
    v5.neighbors.add(v4);
    v5.neighbors.add(v8);
    v4.neighbors.add(v7);
    v8.neighbors.add(v7);
    v8.neighbors.add(v9);

    assertEquals(3, Practice.oddVertices(v5));
  }

  @Test
  public void testOddVertices_ComplexGraph() {
    // Using the complex graph created above.
    // Reachable vertices from v3:
    //   v3 (3, odd)
    //   v7 (7, odd)
    //   v12 (12, even)
    //   v34 (34, even)
    //   v45 (45, odd)
    //   v56 (56, even)
    //   v78 (78, even)
    //   v91 (91, odd)
    //   v23 (23, odd)
    // (v67 is not reachable from v3)
    // Thus, the odd vertices are: 3, 7, 45, 91, and 23 (total count = 5).
    Vertex<Integer>[] vertices = createComplexGraph();
    Vertex<Integer> v3 = vertices[0];
    assertEquals(5, Practice.oddVertices(v3));
  }

  // --- Tests for sortedReachable(Vertex<Integer> starting) ---
  @Test
  public void testSortedReachable_NullInput() {
    List<Integer> result = Practice.sortedReachable(null);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testSortedReachable_SimpleGraph() {
    // Graph structure:
    //         5
    //        / \
    //      8a   8b
    //       |
    //       v
    //       2
    // DFS should collect: 5, 8, 2, 8.
    // Sorted order: [2, 5, 8, 8]
    Vertex<Integer> v5 = new Vertex<>(5);
    Vertex<Integer> v8a = new Vertex<>(8);
    Vertex<Integer> v8b = new Vertex<>(8);
    Vertex<Integer> v2 = new Vertex<>(2);
    
    v5.neighbors.add(v8a);
    v5.neighbors.add(v8b);
    v8a.neighbors.add(v2);

    List<Integer> expected = Arrays.asList(2, 5, 8, 8);
    assertEquals(expected, Practice.sortedReachable(v5));
  }

  @Test
  public void testSortedReachable_ComplexGraph() {
    // Using the complex graph:
    // Reachable values from v3: 3, 7, 12, 34, 45, 56, 78, 91, 23.
    // Sorted ascending: [3, 7, 12, 23, 34, 45, 56, 78, 91]
    Vertex<Integer>[] vertices = createComplexGraph();
    Vertex<Integer> v3 = vertices[0];
    List<Integer> expected = Arrays.asList(3, 7, 12, 23, 34, 45, 56, 78, 91);
    assertEquals(expected, Practice.sortedReachable(v3));
  }

  // --- Tests for sortedReachable(Map<Integer, Set<Integer>> graph, int starting) ---
  @Test
  public void testSortedReachable_MapGraph_StartingPresent() {
    // Simple map graph:
    // 1 -> {2, 3}
    // 2 -> {4}
    // 3 -> {}
    // 4 -> {}
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new LinkedHashSet<>(Arrays.asList(2, 3)));
    graph.put(2, new LinkedHashSet<>(Arrays.asList(4)));
    graph.put(3, new LinkedHashSet<>());
    graph.put(4, new LinkedHashSet<>());
    
    List<Integer> expected = Arrays.asList(1, 2, 3, 4);
    assertEquals(expected, Practice.sortedReachable(graph, 1));
  }

  @Test
  public void testSortedReachable_MapGraph_StartingNotPresent() {
    // Same graph as above; starting vertex 5 is not a key.
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new LinkedHashSet<>(Arrays.asList(2, 3)));
    graph.put(2, new LinkedHashSet<>(Arrays.asList(4)));
    graph.put(3, new LinkedHashSet<>());
    graph.put(4, new LinkedHashSet<>());
    
    List<Integer> result = Practice.sortedReachable(graph, 5);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  // --- Tests for twoWay(Vertex<T> v1, Vertex<T> v2) ---
  @Test
  public void testTwoWay_BothNull() {
    assertFalse(Practice.twoWay(null, null));
  }

  @Test
  public void testTwoWay_SameVertex() {
    // A vertex is always reachable from itself.
    Vertex<Integer> v = new Vertex<>(10);
    assertTrue(Practice.twoWay(v, v));
  }

  @Test
  public void testTwoWay_DirectCycle() {
    // Two vertices with a direct two-way connection.
    Vertex<Integer> v1 = new Vertex<>(1);
    Vertex<Integer> v2 = new Vertex<>(2);
    v1.neighbors.add(v2);
    v2.neighbors.add(v1);
    assertTrue(Practice.twoWay(v1, v2));
  }

  @Test
  public void testTwoWay_OneWayConnection() {
    // One vertex can reach the other, but not vice versa.
    Vertex<Integer> v1 = new Vertex<>(1);
    Vertex<Integer> v2 = new Vertex<>(2);
    v1.neighbors.add(v2);
    assertFalse(Practice.twoWay(v1, v2));
  }

  @Test
  public void testTwoWay_IndirectConnection() {
    // Indirect cycle: v1 -> v2 -> v3 -> v1.
    Vertex<Integer> v1 = new Vertex<>(1);
    Vertex<Integer> v2 = new Vertex<>(2);
    Vertex<Integer> v3 = new Vertex<>(3);
    v1.neighbors.add(v2);
    v2.neighbors.add(v3);
    v3.neighbors.add(v1);
    assertTrue(Practice.twoWay(v1, v3));
  }

  // --- Tests for positivePathExists(Map<Integer, Set<Integer>> graph, int starting, int ending) ---
  /**
   * Test that a vertex is always reachable from itself if it is positive.
   */
  @Test
  public void testSelfPath() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    // Graph contains a single positive vertex 5 with no neighbors.
    graph.put(5, new HashSet<>());
    assertTrue(Practice.positivePathExists(graph, 5, 5));
  }

  /**
   * Test a small graph with a valid positive path:
   *  3 -> {4}
   *  4 -> {5}
   *  5 -> {}
   * Expected: There is a valid positive path from 3 to 5.
   */
  @Test
  public void testValidPositivePath_SmallGraph() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(3, new HashSet<>(Arrays.asList(4)));
    graph.put(4, new HashSet<>(Arrays.asList(5)));
    graph.put(5, new HashSet<>());
    assertTrue(Practice.positivePathExists(graph, 3, 5));
  }

  /**
   * Test that if the ending vertex is missing from the graph, the result is false.
   */
  @Test
  public void testMissingEndingVertex() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(3, new HashSet<>(Arrays.asList(4)));
    graph.put(4, new HashSet<>(Arrays.asList(5)));
    graph.put(5, new HashSet<>());
    // Ending vertex 10 is not present.
    assertFalse(Practice.positivePathExists(graph, 3, 10));
  }

  /**
   * Test a graph where the only available path includes a negative vertex.
   * Graph:
   *   3 -> {-4}
   *   -4 -> {5}
   *   5 -> {}
   * Even though there is a path from 3 to 5, it includes -4 (non-positive),
   * so the method should return false.
   */
  @Test
  public void testPathIncludesNegative() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(3, new HashSet<>(Arrays.asList(-4)));
    graph.put(-4, new HashSet<>(Arrays.asList(5)));
    graph.put(5, new HashSet<>());
    assertFalse(Practice.positivePathExists(graph, 3, 5));
  }

  /**
   * Test a graph with a cycle where all vertices are positive.
   * Graph:
   *   1 -> {2}
   *   2 -> {3}
   *   3 -> {1, 4}
   *   4 -> {}
   * There is a valid cycle and a positive path from 1 to 4.
   */
  @Test
  public void testCycleValid() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new HashSet<>(Arrays.asList(2)));
    graph.put(2, new HashSet<>(Arrays.asList(3)));
    graph.put(3, new HashSet<>(Arrays.asList(1, 4)));
    graph.put(4, new HashSet<>());
    assertTrue(Practice.positivePathExists(graph, 1, 4));
  }

  /**
   * Test a graph with a cycle that forces the use of a negative vertex.
   * Graph:
   *   1 -> {-2}
   *   -2 -> {3}
   *   3 -> {1, 4}
   *   4 -> {}
   * The only available path from 1 to 4 includes -2, so the result should be false.
   */
  @Test
  public void testCycleInvalidDueToNegative() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new HashSet<>(Arrays.asList(-2)));
    graph.put(-2, new HashSet<>(Arrays.asList(3)));
    graph.put(3, new HashSet<>(Arrays.asList(1, 4)));
    graph.put(4, new HashSet<>());
    assertFalse(Practice.positivePathExists(graph, 1, 4));
  }

  /**
   * Test a graph with multiple paths from the starting vertex to the ending vertex.
   * One path is valid while another includes a negative vertex.
   * Graph:
   *   3 -> {4, -2}
   *   4 -> {9}
   *   -2 -> {9}
   *   9 -> {}
   * Although the path 3->(-2)->9 is invalid, 3->4->9 is valid.
   */
  @Test
  public void testMultiplePathsOneValid() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(3, new HashSet<>(Arrays.asList(4, -2)));
    graph.put(4, new HashSet<>(Arrays.asList(9)));
    graph.put(-2, new HashSet<>(Arrays.asList(9)));
    graph.put(9, new HashSet<>());
    assertTrue(Practice.positivePathExists(graph, 3, 9));
  }

  /**
   * Test that if the starting vertex is not positive, the method returns false.
   * Graph:
   *   -3 -> {4}
   *   4 -> {5}
   *   5 -> {}
   */
  @Test
  public void testStartingNotPositive() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(-3, new HashSet<>(Arrays.asList(4)));
    graph.put(4, new HashSet<>(Arrays.asList(5)));
    graph.put(5, new HashSet<>());
    assertFalse(Practice.positivePathExists(graph, -3, 5));
  }

  /**
   * Test that if the ending vertex is not positive, the method returns false.
   * Graph:
   *   3 -> {4}
   *   4 -> {-5}
   *   -5 -> {}
   */
  @Test
  public void testEndingNotPositive() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(3, new HashSet<>(Arrays.asList(4)));
    graph.put(4, new HashSet<>(Arrays.asList(-5)));
    graph.put(-5, new HashSet<>());
    assertFalse(Practice.positivePathExists(graph, 3, -5));
  }

  /**
   * Test a disconnected graph where the starting and ending vertices belong to different components.
   * Graph:
   *   Component 1: 1 -> {2}, 2 -> {}
   *   Component 2: 3 -> {4}, 4 -> {}
   * There is no path from 1 to 4.
   */
  @Test
  public void testDisconnectedGraph() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(1, new HashSet<>(Arrays.asList(2)));
    graph.put(2, new HashSet<>());
    graph.put(3, new HashSet<>(Arrays.asList(4)));
    graph.put(4, new HashSet<>());
    assertFalse(Practice.positivePathExists(graph, 1, 4));
  }

  /**
   * Test a complex graph with cycles and multiple valid paths.
   * Graph structure:
   *   10 -> {20, 30, 40}
   *   20 -> {50, 60}
   *   30 -> {20, 70}
   *   40 -> {80}
   *   50 -> {90}
   *   60 -> {30, 90}  // cycle: 30 -> 20 -> 60 -> 30
   *   70 -> {}
   *   80 -> {90}
   *   90 -> {}
   * A valid positive path exists from 10 to 90 (e.g., 10 -> 20 -> 50 -> 90).
   */
  @Test
  public void testComplexGraphValid() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(10, new HashSet<>(Arrays.asList(20, 30, 40)));
    graph.put(20, new HashSet<>(Arrays.asList(50, 60)));
    graph.put(30, new HashSet<>(Arrays.asList(20, 70)));
    graph.put(40, new HashSet<>(Arrays.asList(80)));
    graph.put(50, new HashSet<>(Arrays.asList(90)));
    graph.put(60, new HashSet<>(Arrays.asList(30, 90)));
    graph.put(70, new HashSet<>());
    graph.put(80, new HashSet<>(Arrays.asList(90)));
    graph.put(90, new HashSet<>());
    assertTrue(Practice.positivePathExists(graph, 10, 90));
  }

  /**
   * Test a complex graph where every path from the starting vertex to the ending vertex includes a negative vertex.
   * Modified graph structure:
   *   10 -> {20, 30}
   *   20 -> {-50}     // now, 20 only leads to -50
   *   30 -> {20, 70}
   *   -50 -> {90}
   *   70 -> {}
   *   90 -> {}
   * Every path from 10 to 90 must pass through 20 then -50.
   */
  @Test
  public void testComplexGraphInvalid() {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(10, new HashSet<>(Arrays.asList(20, 30)));
    graph.put(20, new HashSet<>(Arrays.asList(-50)));
    graph.put(30, new HashSet<>(Arrays.asList(20, 70)));
    graph.put(-50, new HashSet<>(Arrays.asList(90)));
    graph.put(70, new HashSet<>());
    graph.put(90, new HashSet<>());
    assertFalse(Practice.positivePathExists(graph, 10, 90));
  }

  // --- Tests for hasExtendedConnectionAtCompany(Professional person, String companyName) ---
  @Test
  public void testHasExtendedConnectionAtCompany_NullPerson() {
    assertFalse(Practice.hasExtendedConnectionAtCompany(null, "Acme Corp"));
  }

  @Test
  public void testHasExtendedConnectionAtCompany_SelfWorks() {
    // A professional who works for the company should return true.
    Professional alice = new Professional("Alice", "Acme Corp", 5, new HashSet<>());
    assertTrue(Practice.hasExtendedConnectionAtCompany(alice, "Acme Corp"));
  }

  @Test
  public void testHasExtendedConnectionAtCompany_DirectConnection() {
    // Alice does not work for Acme Corp but her direct connection Bob does.
    Professional bob = new Professional("Bob", "Acme Corp", 10, new HashSet<>());
    Set<Professional> aliceConnections = new HashSet<>();
    aliceConnections.add(bob);
    Professional alice = new Professional("Alice", "Other Corp", 5, aliceConnections);
    assertTrue(Practice.hasExtendedConnectionAtCompany(alice, "Acme Corp"));
  }

  @Test
  public void testHasExtendedConnectionAtCompany_IndirectConnection() {
    // Create a chain: Alice -> Charlie -> Bob,
    // where Bob works at Acme Corp.
    Professional bob = new Professional("Bob", "Acme Corp", 8, new HashSet<>());
    Set<Professional> charlieConnections = new HashSet<>();
    charlieConnections.add(bob);
    Professional charlie = new Professional("Charlie", "Other Corp", 6, charlieConnections);
    
    Set<Professional> aliceConnections = new HashSet<>();
    aliceConnections.add(charlie);
    Professional alice = new Professional("Alice", "Other Corp", 5, aliceConnections);
    assertTrue(Practice.hasExtendedConnectionAtCompany(alice, "Acme Corp"));
  }

  @Test
  public void testHasExtendedConnectionAtCompany_NoConnection() {
    // Create a network where none work at Acme Corp.
    Professional bob = new Professional("Bob", "Other Corp", 8, new HashSet<>());
    Professional charlie = new Professional("Charlie", "Other Corp", 6, new HashSet<>());
    Set<Professional> aliceConnections = new HashSet<>();
    aliceConnections.add(bob);
    aliceConnections.add(charlie);
    Professional alice = new Professional("Alice", "Other Corp", 5, aliceConnections);
    assertFalse(Practice.hasExtendedConnectionAtCompany(alice, "Acme Corp"));
  }
}
