package main.java.GeneticAlgorithm;

public class Edge {
    private int edge_from;
    private int edge_to;

    public Edge(int edge_from, int edge_to) {
        this.edge_from = edge_from;
        this.edge_to = edge_to;
    }

    public int getEdge_from() {
        return edge_from;
    }

    public int getEdge_to() {
        return edge_to;
    }

    public String toString() {
        return "Edge from (" + edge_from + ", to " + edge_to + ").";
    }

}
