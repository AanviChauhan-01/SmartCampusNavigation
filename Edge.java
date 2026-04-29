public class Edge {
    Node source, destination;
    int weight;
    int crowd;

    public Edge(Node s, Node d, int w) {
        source = s;
        destination = d;
        weight = w;
        crowd = 0;
    }

    public int getEffectiveWeight() {
        return weight + crowd;
    }
}
