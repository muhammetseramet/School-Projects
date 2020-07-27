package main.java.GeneticAlgorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class GraphReader {

    private BufferedReader reader;
    private int numOfNodes; // number of node on the graph
    @SuppressWarnings("unused")
    private int numOfEdges; // number of edge on the graph
    private double[] weightOfNodes; // weights of nodes
    private double[] weightRatioOfNodes; // Ratio of node weight to number of edges
    private ArrayList<Edge> edgeList = new ArrayList<>(); // edges of graph

    GraphReader() {
        this.numOfNodes = 0;
        this.numOfEdges = 0;
        this.reader = null;
    }

    void readGraphFile(String fileName) {
        String line;
        // first two line of the file is number of nodes and number of edges,
        // respectively.
        try {
            reader = new BufferedReader(new FileReader(fileName));
            numOfNodes = (int) Double.parseDouble(reader.readLine());
            numOfEdges = (int) Double.parseDouble(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        weightOfNodes = new double[numOfNodes];
        int[] edgeCountofNodes = new int[numOfNodes];
        try {
            for (int i = 0; i < numOfNodes; i++) { // read node weights from file
                line = reader.readLine();
                String nodeNumber = line.split("\\s")[0];
                String weightOfNode = line.split("\\s")[1];

                weightOfNodes[Integer.parseInt(nodeNumber)] = Double.parseDouble(weightOfNode.replaceAll(",", "."));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while ((line = reader.readLine()) != null) { // read edges from file
                int addEdgeFrom = Integer.parseInt(line.split("\\s")[0]);
                int addEdgeTo = Integer.parseInt(line.split("\\s")[1]);

                if (!isEdgeAdded(addEdgeFrom, addEdgeTo)) {
                    edgeList.add(new Edge(addEdgeFrom, addEdgeTo));
                    edgeCountofNodes[addEdgeFrom]++; // count edges of nodes
                    edgeCountofNodes[addEdgeTo]++;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //A ratio is determined for each node based on the ratio of weight to the number of edges.
        weightRatioOfNodes = new double[numOfNodes];
        for (int i = 0; i < numOfNodes; i++) {
            double optimum = weightOfNodes[i] / edgeCountofNodes[i];
            weightRatioOfNodes[i] = optimum;
        }

    }

    private boolean isEdgeAdded(int edgeFrom, int edgeTo) { // graph is undirected, just add the edge once

        for (Edge edge : edgeList) {
            if (((edge.getEdge_from() == edgeFrom) && (edge.getEdge_to() == edgeTo))
                    || ((edge.getEdge_from() == edgeTo) && (edge.getEdge_to() == edgeFrom)))
                return true;
        }

        return false;
    }

    int getNumberOfVertex() {
        return numOfNodes;
    }

    double[] getWeights() {
        return weightOfNodes;
    }

    ArrayList<Edge> getEdges() {
        return edgeList;
    }

    double[] getWeighted_adjacency() {
        return weightRatioOfNodes;
    }
}
