import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MapPanel extends JPanel {
    Graph graph;
    java.util.List<Node> path = new ArrayList<>();
    Node currentUser = null;

    public MapPanel(Graph g) {
        graph = g;
    }

    public void setPath(java.util.List<Node> p) {
        path = p;
        animateUser();
    }

    private void animateUser() {
        new Thread(() -> {
            try {
                for (Node n : path) {
                    currentUser = n;
                    repaint();
                    Thread.sleep(700);
                }
            } catch (Exception ignored) {}
        }).start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // EDGES with FIXED COLORS
        for (java.util.List<Edge> edges : graph.adjList.values()) {
            for (Edge e : edges) {

                if (e.crowd > 7) g.setColor(Color.RED);        // HIGH
                else if (e.crowd > 3) g.setColor(Color.ORANGE); // MEDIUM
                else g.setColor(Color.GREEN);                  // LOW

                g.drawLine(e.source.x, e.source.y,
                        e.destination.x, e.destination.y);
            }
        }

        // MAIN PATH
        g.setColor(Color.BLUE);
        for (int i = 0; i < path.size() - 1; i++) {
            Node a = path.get(i);
            Node b = path.get(i + 1);
            g.drawLine(a.x, a.y, b.x, b.y);
        }

        // NODES
        for (Node n : graph.adjList.keySet()) {
            g.setColor(Color.BLACK);
            g.fillOval(n.x - 8, n.y - 8, 16, 16);
            g.drawString(n.name, n.x - 5, n.y - 12);
        }

        // USER
        if (currentUser != null) {
            g.setColor(Color.MAGENTA);
            g.fillOval(currentUser.x - 6, currentUser.y - 6, 12, 12);
        }
    }
}