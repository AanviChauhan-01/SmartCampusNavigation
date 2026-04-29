public class SmartCampus {

    public static void main(String[] args) {
        new LoginUI();
    }

    public static void launchApp() {

        Graph g = new Graph();

        Node A = new Node("A",100,100);
        Node B = new Node("B",250,80);
        Node C = new Node("C",400,120);
        Node D = new Node("D",120,250);
        Node E = new Node("E",300,300);
        Node F = new Node("F",450,250);

        g.addNode(A); g.addNode(B); g.addNode(C);
        g.addNode(D); g.addNode(E); g.addNode(F);

        g.addEdge(A,B,4);
        g.addEdge(B,C,3);
        g.addEdge(A,D,2);
        g.addEdge(D,E,3);
        g.addEdge(E,F,2);
        g.addEdge(C,F,5);
        g.addEdge(B,E,2);

        new UI(g,new CrowdSimulator());
    }
}
