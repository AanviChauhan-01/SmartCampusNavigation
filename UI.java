import javax.swing.*;
import java.awt.*;
import java.util.*;

public class UI {

    JFrame frame;
    JComboBox<Node> destBox;
    JTextArea output, crowdArea, timetableArea;

    Graph graph;
    CrowdSimulator sim;
    Timetable tt = new Timetable();
    MapPanel map;

    TimeSlot lastAuto = null;

    Map<TimeSlot, Boolean> beforeNotified = new HashMap<>();
    Map<TimeSlot, Boolean> afterNotified = new HashMap<>();

    public UI(Graph g, CrowdSimulator s) {

        graph = g;
        sim = s;

        frame = new JFrame("Smart Campus System");
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        destBox = new JComboBox<>(g.adjList.keySet().toArray(new Node[0]));

        JButton routeBtn = new JButton("Find Route");
        JButton addBtn = new JButton("Add Class");
        JButton selectBtn = new JButton("Select Class");

        JPanel top = new JPanel();
        top.add(destBox);
        top.add(routeBtn);
        top.add(addBtn);
        top.add(selectBtn);

        map = new MapPanel(graph);

        output = new JTextArea(5, 40);
        timetableArea = new JTextArea();
        crowdArea = new JTextArea();

        timetableArea.setEditable(false);
        crowdArea.setEditable(false);

        JPanel right = new JPanel(new GridLayout(2,1));
        right.add(new JScrollPane(timetableArea));
        right.add(new JScrollPane(crowdArea));

        frame.add(top, BorderLayout.NORTH);
        frame.add(map, BorderLayout.CENTER);
        frame.add(right, BorderLayout.EAST);
        frame.add(new JScrollPane(output), BorderLayout.SOUTH);

        routeBtn.addActionListener(e -> findRoute());
        addBtn.addActionListener(e -> addClass());
        selectBtn.addActionListener(e -> selectClass());

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tt.loadFromDB();

        new javax.swing.Timer(1000, e -> autoUpdate()).start();
    }

    // 🔥 FIX: get correct node from graph
    private Node getNodeByName(String name) {
        for (Node n : graph.adjList.keySet()) {
            if (n.name.equals(name)) return n;
        }
        return null;
    }

    private void speak(String text) {
        try {
            Runtime.getRuntime().exec(
                    "powershell -Command \"Add-Type -AssemblyName System.Speech;" +
                            "(New-Object System.Speech.Synthesis.SpeechSynthesizer).Speak('" + text + "');\""
            );
        } catch (Exception ignored) {}
    }

    private void autoUpdate() {
        Calendar c = Calendar.getInstance();
        int now = c.get(Calendar.HOUR_OF_DAY) * 60 +
                c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);

        TimeSlot current = tt.getCurrent(now);
        TimeSlot next = tt.getNext(now);

        timetableArea.setText("=== TIMETABLE ===\n");

        for (TimeSlot t : tt.list) {
            timetableArea.append(t.toString() + "\n");
        }

        if (current != null) {
            timetableArea.append("\n🟢 NOW: " + current.subject + " @ " + current.room);
            sim.update(graph, getNodeByName(current.room.name)); // FIX
        }

        if (next != null) {
            timetableArea.append("\n🔮 NEXT: " + next.toString());
        }

        int nowSec = now * 60 + sec;

        for (TimeSlot t : tt.list) {
            int startSec = t.start * 60;

            if (startSec - nowSec <= 5 && startSec - nowSec > 0 &&
                    !beforeNotified.getOrDefault(t, false)) {
                JOptionPane.showMessageDialog(frame,
                        "⏰ Class starting now: " + t.subject);
                speak("Class starting now");
                beforeNotified.put(t, true);
            }

            if ((nowSec / 60) >= t.start + 5 && (nowSec / 60) < t.end &&
                    !afterNotified.getOrDefault(t, false)) {
                JOptionPane.showMessageDialog(frame,
                        "⚠ Class started 5 minutes ago");
                speak("Class started 5 minutes ago");
                afterNotified.put(t, true);
            }
        }

        if (current != null && current != lastAuto) {
            Node correctNode = getNodeByName(current.room.name); // FIX
            if (correctNode != null) {
                destBox.setSelectedItem(correctNode);
                autoNavigate(correctNode);
            }
            lastAuto = current;
        }

        updateCrowd();
    }

    private void autoNavigate(Node dest) {
        Node src = (Node) destBox.getItemAt(0);

        Dijkstra.compute(graph, src);
        java.util.List<Node> path = Dijkstra.getPath(dest);

        map.setPath(path);

        output.setText("🚀 AUTO ROUTE\n" + path +
                "\nCost: " + Dijkstra.dist.get(dest));

        speak("Auto navigating");
    }

    private void updateCrowd() {
        crowdArea.setText("=== CROWD STATUS ===\n");

        Set<String> visited = new HashSet<>();

        for (java.util.List<Edge> edges : graph.adjList.values()) {
            for (Edge e : edges) {

                String key = e.source + "-" + e.destination;
                String reverse = e.destination + "-" + e.source;

                if (visited.contains(key) || visited.contains(reverse))
                    continue;

                String status;

                if (e.crowd > 7) status = "HIGH 🔴";
                else if (e.crowd > 3) status = "MEDIUM 🟡";
                else status = "LOW 🟢";

                crowdArea.append(e.source + " → " + e.destination + " : " + status + "\n");

                visited.add(key);
            }
        }
    }

    private void selectClass() {
        if (tt.list.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No classes!");
            return;
        }

        TimeSlot selected = (TimeSlot) JOptionPane.showInputDialog(
                frame, "Select Class",
                "Timetable",
                JOptionPane.PLAIN_MESSAGE,
                null,
                tt.list.toArray(),
                null
        );

        if (selected != null) {
            Node correctNode = getNodeByName(selected.room.name); // FIX
            if (correctNode != null) {
                destBox.setSelectedItem(correctNode);
            }
        }
    }

    private void addClass() {
        try {
            String sub = JOptionPane.showInputDialog("Subject:");
            String st = JOptionPane.showInputDialog("Start HH:MM");
            String en = JOptionPane.showInputDialog("End HH:MM");

            String[] s = st.split(":");
            String[] e = en.split(":");

            int sh = Integer.parseInt(s[0]);
            int sm = Integer.parseInt(s[1]);
            int eh = Integer.parseInt(e[0]);
            int em = Integer.parseInt(e[1]);

            int start = sh * 60 + sm;
            int end = eh * 60 + em;

            int minTime = 8 * 60;
            int maxTime = 18 * 60;

            if (start < minTime || end > maxTime) {
                JOptionPane.showMessageDialog(frame,
                        "❌ Time must be between 08:00 and 18:00");
                return;
            }

            if (start >= end) {
                JOptionPane.showMessageDialog(frame,
                        "❌ End time must be after start time");
                return;
            }

            Node room = (Node) JOptionPane.showInputDialog(
                    frame, "Room", "Select",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    graph.adjList.keySet().toArray(),
                    null
            );

            tt.add(sh, sm, eh, em, room, sub);

            Node correctNode = getNodeByName(room.name); // FIX
            if (correctNode != null) {
                destBox.setSelectedItem(correctNode);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Invalid Input!");
        }
    }

    private void findRoute() {

        Node src = (Node) JOptionPane.showInputDialog(
                frame, "Select Source",
                "Source",
                JOptionPane.PLAIN_MESSAGE,
                null,
                graph.adjList.keySet().toArray(),
                null
        );

        Node dest = (Node) destBox.getSelectedItem();

        sim.update(graph, dest);

        Dijkstra.compute(graph, src);
        java.util.List<Node> mainPath = Dijkstra.getPath(dest);

        map.setPath(mainPath);

        output.setText("🚀 BEST ROUTE\n" + mainPath +
                "\nCost: " + Dijkstra.dist.get(dest));

        for (java.util.List<Edge> edges : graph.adjList.values()) {
            for (Edge e : edges) {
                e.weight += 2;
            }
        }

        Dijkstra.compute(graph, src);
        java.util.List<Node> altPath = Dijkstra.getPath(dest);

        for (java.util.List<Edge> edges : graph.adjList.values()) {
            for (Edge e : edges) {
                e.weight -= 2;
            }
        }

        output.append("\n\n🔁 ALT ROUTE\n" + altPath);

        speak("Navigating route");

        updateCrowd();
    }
}