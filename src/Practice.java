import java.util.List;
import java.util.Map;
import java.util.Set;

public class Practice {

  // return a count of how many vertices with odd values can be reached 
  // from a given starting vertex (include the start in the count iff it is odd).
  // Return 0 if starting vertex is null 
  public static int oddVertices(Vertex<Integer> starting) {
    return 0;
  }

  // return a sorted list of all values reachable from the starting vertex
  // (including the starting vertex itself)
  // If there is duplicate vertex data, copies should be present
  // in the output. For example
  //    5 -- > 8
  //    |      |
  //    v      v
  //    8 -- > 2
  // Should return the below when starting from 5:
  // [2, 5, 8, 8]
  // if the starting vertex is null, return an empty List
  public static List<Integer> sortedReachable(Vertex<Integer> starting) {
    return null;
  }

  // return a sorted list of all values reachable from the starting vertex
  // (including the starting vertex itself)
  // You can assume there are no duplicate vertices.
  // For example:
  //  TODO: Make a map and expected output
  // if the starting vertex is not a key in the map, return an empty List
  public static List<Integer> sortedReachable(Map<Integer, Set<Integer>> graph, int starting) {
    return null;
  }

  // Return true iff it is possible both to reach v2 starting from v1
  // AND to reach v1 starting from v2. Return false if either or both
  // cannot be done or v1 or v2 is null
  // Consider it always possible for a vertex to reach itself.
  public static <T> boolean twoWay(Vertex<T> v1, Vertex<T> v2) {
    return false;
  }
}