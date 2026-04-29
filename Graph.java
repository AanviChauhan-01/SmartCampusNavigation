import java.util.*;

public class Graph {
    Map<Node, List<Edge>> adjList = new HashMap<>();

    public void addNode(Node n) {
        adjList.putIfAbsent(n, new ArrayList<>());
    }

    public void addEdge(Node s, Node d, int w) {
        adjList.get(s).add(new Edge(s, d, w));
        adjList.get(d).add(new Edge(d, s, w));
    }

    public List<Edge> getEdges(Node n) {
        return adjList.getOrDefault(n, new ArrayList<>());
    }
}
