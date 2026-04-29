import java.util.*;

public class CrowdSimulator {
    Random rand = new Random();

    public void update(Graph g, Node active) {

        Set<String> visited = new HashSet<>();

        for (List<Edge> edges : g.adjList.values()) {
            for (Edge e : edges) {

                String key = e.source.name + "-" + e.destination.name;
                String reverse = e.destination.name + "-" + e.source.name;

                if (visited.contains(key) || visited.contains(reverse))
                    continue;

                int value;

                if (active != null &&
                        (e.source.equals(active) || e.destination.equals(active))) {
                    value = 6 + rand.nextInt(5); // more crowd near active node
                } else {
                    value = rand.nextInt(10);
                }

                // apply same value both directions
                e.crowd = value;

                for (Edge back : g.getEdges(e.destination)) {
                    if (back.destination.equals(e.source)) {
                        back.crowd = value;
                    }
                }

                visited.add(key);
            }
        }
    }
}
