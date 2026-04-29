import java.util.*;

public class Dijkstra {
    static Map<Node, Integer> dist;
    static Map<Node, Node> prev;

    public static void compute(Graph g, Node src) {
        dist = new HashMap<>();
        prev = new HashMap<>();

        PriorityQueue<Node> pq =
                new PriorityQueue<>(Comparator.comparingInt(dist::get));

        for (Node n : g.adjList.keySet()) {
            dist.put(n, Integer.MAX_VALUE);
        }

        dist.put(src, 0);
        pq.add(src);

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            for (Edge e : g.getEdges(current)) {
                int newDist = dist.get(current) + e.getEffectiveWeight();

                if (newDist < dist.get(e.destination)) {
                    dist.put(e.destination, newDist);
                    prev.put(e.destination, current);
                    pq.add(e.destination);
                }
            }
        }
    }

    public static List<Node> getPath(Node target) {
        List<Node> path = new ArrayList<>();
        Node step = target;

        while (step != null) {
            path.add(step);
            step = prev.get(step);
        }

        Collections.reverse(path);
        return path;
    }
}
