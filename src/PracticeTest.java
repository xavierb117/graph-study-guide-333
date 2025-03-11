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
  @Test
  public void testPositivePathExists_ValidPath() {
    // Simple map graph:
    // 3 -> {4}
    // 4 -> {5}
    // 5 -> {}
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(3, new LinkedHashSet<>(Arrays.asList(4)));
    graph.put(4, new LinkedHashSet<>(Arrays.asList(5)));
    graph.put(5, new LinkedHashSet<>());
    
    List<Integer> expected = Arrays.asList(3, 4, 5);
    assertEquals(expected, Practice.positivePathExists(graph, 3, 5));
  }

  @Test
  public void testPositivePathExists_NoPath() {
    // Graph with no valid path from 3 to 5.
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(3, new LinkedHashSet<>(Arrays.asList(4)));
    graph.put(4, new LinkedHashSet<>());
    graph.put(5, new LinkedHashSet<>());
    
    List<Integer> result = Practice.positivePathExists(graph, 3, 5);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testPositivePathExists_InvalidInput() {
    // Graph as before; invalid when starting or ending is not positive or not present.
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(3, new LinkedHashSet<>(Arrays.asList(4)));
    graph.put(4, new LinkedHashSet<>(Arrays.asList(5)));
    graph.put(5, new LinkedHashSet<>());
    
    List<Integer> result1 = Practice.positivePathExists(graph, -3, 5);
    assertNotNull(result1);
    assertTrue(result1.isEmpty());
    
    List<Integer> result2 = Practice.positivePathExists(graph, 3, 10);
    assertNotNull(result2);
    assertTrue(result2.isEmpty());
  }

  @Test
  public void testPositivePathExists_ComplexGraph() {
    // Create a complex map graph using a structure similar to the complex Vertex graph.
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    graph.put(3, new LinkedHashSet<>(Arrays.asList(7, 34)));
    graph.put(7, new LinkedHashSet<>(Arrays.asList(12, 45, 34, 56)));
    graph.put(12, new LinkedHashSet<>(Arrays.asList(7, 56, 78)));
    graph.put(34, new LinkedHashSet<>(Arrays.asList(34, 91)));
    graph.put(56, new LinkedHashSet<>(Arrays.asList(78)));
    graph.put(78, new LinkedHashSet<>(Arrays.asList(91)));
    graph.put(91, new LinkedHashSet<>(Arrays.asList(56)));
    graph.put(45, new LinkedHashSet<>(Arrays.asList(23)));
    graph.put(23, new LinkedHashSet<>());
    graph.put(67, new LinkedHashSet<>(Arrays.asList(91)));

    // Assuming insertion order, a valid DFS path from 3 to 91 might be:
    // 3 -> 7 -> 12 -> 56 -> 78 -> 91
    List<Integer> expected = Arrays.asList(3, 7, 12, 56, 78, 91);
    assertEquals(expected, Practice.positivePathExists(graph, 3, 91));
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
