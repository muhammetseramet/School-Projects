package main.java.GeneticAlgorithm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

class GeneticAlgorithm {

    private int numOfNodes; // bir satır
    private int populationSize; // toplamda kaç satır
    private double crossoverProb; // crossover probability
    private double mutationProb; // mutation probability
    private int numberOfGeneration;
    private double[] weightOfNodes; // ağırlıkları
    private double[] weightRatioOfNodes;
    private ArrayList<Edge> edgeList; // tüm graphın edgeleri
    private ArrayList<String> populationList = new ArrayList<>(); // bütün satırlar, yani popülasyonun ta kendisi

    private String fileOutput = "";
    private double totalWeightValue;

    private double minWeightValue = Double.MAX_VALUE;
    private double generalAverage;
    private int numberOfLevel;

    GeneticAlgorithm() {
    }

    GeneticAlgorithm(int numberOfNode, int numberOfGeneration, int populationSize, double crossover, double mutation,
                     double[] weightArray, ArrayList<Edge> edges, double[] weightRatioOfNodes) {
        this.numOfNodes = numberOfNode;
        // kaç kere generate etcek
        this.populationSize = populationSize;
        this.crossoverProb = crossover;
        this.mutationProb = mutation;
        this.numberOfGeneration = numberOfGeneration;
        this.weightOfNodes = weightArray;
        this.edgeList = edges;
        this.weightRatioOfNodes = weightRatioOfNodes;
    }

    void runGeneticAlgorithm() {
        generatePopulation(); // initialize et

        for (numberOfLevel = 0; numberOfLevel < numberOfGeneration; numberOfLevel++) {
            repair();
            binaryTournament();
            Collections.shuffle(populationList);
            crossover();
            mutation();
        }
        fileOutput = fileOutput.concat("\nMinimum Weighted Vertex : " + minWeightValue);//get results
        fileOutput = fileOutput
                .concat("\nGeneral Average : " + String.format("%.3f", generalAverage / numberOfGeneration));
        System.out.println("Process is completed.");

    }

    //generate random states, called population
    private void generatePopulation() {
        for (int i = 0; i < populationSize; i++) {
            String state = "";
            for (int j = 0; j < numOfNodes; j++) {
                int stateBit = (int) (Math.random() * 2);
                state = state.concat(String.valueOf(stateBit));
            }
            populationList.add(state);
        }
    }

    private void repair() {
        ArrayList<String> temp = new ArrayList<>();
        double minWeight = Double.MAX_VALUE;
        for (String state : populationList) {
            System.out.println("State =>" + state);
            if (isFeasible(state)) { //if state is feasible, then add list directly
                temp.add(state);
                System.out.println("Feasible =>" + state);
            } else {
                ArrayList<Integer> zeroIndex = new ArrayList<>();
                for (int i = 0; i < state.length(); i++) {
                    if (state.charAt(i) == '0') {
                        zeroIndex.add(i);
                    }
                }
                int index = 0;
                double tempOptimum = Double.MAX_VALUE;
                for (int i = 0; i < zeroIndex.size(); i++) {
                    if (tempOptimum < weightRatioOfNodes[i]) {
                        index = i;
                    }
                }
                while (!isFeasible(state)) {

                    //int index = ((int) (Math.random() * zeroIndex.size()));

                    if (state.charAt(zeroIndex.get(index)) == '0') {
                        state = state.substring(0, zeroIndex.get(index)) + '1' + state.substring(zeroIndex.get(index) + 1);
                    }
                    zeroIndex.remove(index);
                }
                temp.add(state);

            }
        }

        populationList = temp;

        for (String state : temp) { //get minimum and total weight
            double weight = findWeight(state);
            totalWeightValue += weight;
            if (weight < minWeight) {
                minWeight = weight;
            }
        }
        if (minWeight < minWeightValue) {
            minWeightValue = minWeight;
        }

        generalAverage += totalWeightValue / this.populationSize; //get state results
        fileOutput = fileOutput.concat(numberOfLevel + ".level\t Min Weight: " + String.format("%.3f", minWeight)
                + " \tAverage :" + String.format("%.3f", totalWeightValue / this.populationSize) + "\n");
        totalWeightValue = 0;

    }
    
    //When the edges of nodes with value 1 are removed, state is not feasible if there are still edge
    private boolean isFeasible(String state) {
        ArrayList<Edge> tempedgeList = new ArrayList<>(edgeList);
        for (int i = 0; i < state.length(); i++) {
            if (state.charAt(i) == '1') {
                for (Edge e : edgeList) {
                    if (e.getEdge_from() == i || e.getEdge_to() == i) {
                        tempedgeList.remove(e);
                    }
                    if (tempedgeList.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private double findWeight(String chromosome) {
        double totalWeight = 0;
        char[] temp = chromosome.toCharArray();
        for (int i = 0; i < chromosome.length(); i++) {                             // for each bit in string
            if (temp[i] == '1')                                                     // if vertex is already added
                totalWeight += weightOfNodes[i];                                    // add vertex weight to total weight
        }

        return totalWeight;                                                         // return calculated total weight
    }

    private void binaryTournament() {
        ArrayList<String> tempPopulation = new ArrayList<>();

        for (int pop = 0; pop < populationSize; pop++) {
            String best = null;
            for (int i = 1; i <= 2; i++) {                                          // works twice
                int randomChromosome = (int) (Math.random() * populationSize);      // generate random number
                String chromosome = populationList.get(randomChromosome);           // get random string from population
                if ((best == null) || (findWeight(chromosome) < findWeight(best)))  // calculate weights and find minimum weight value from two strings
                    best = chromosome;
            }
            tempPopulation.add(pop, best);                                          // add minimum weighted string to the list
        }
        populationList = tempPopulation;                                            // change the global list
    }

    private void crossover() {

        int getPopulation = 0;
        while (getPopulation < populationSize / 2) {
            double randomCrossoverProbability = Math.random();                          // generate random number
            if (randomCrossoverProbability >= crossoverProb) {
                int point = (int) (Math.random() * numOfNodes - 1) + 1;                 // generate random crossover point

                // find sequential strings
                String parent1 = populationList.get(2 * getPopulation);                 // first string,
                String parent2 = populationList.get(2 * getPopulation + 1);             // second string,

                String crossoverParent1 = parent1.substring(point);                     // tail of the first string
                String crossoverParent2 = parent2.substring(point, parent1.length());   // tail of the second string

                populationList.set(2 * getPopulation, parent1.substring(0, point).concat(crossoverParent2));        // tail swapped, first string
                populationList.set(2 * getPopulation + 1, parent2.substring(0, point).concat(crossoverParent1));    // tail swapped, second string
            }
            getPopulation++;
        }

    }

    private void mutation() {

        int getPopulation = 0;
        while (getPopulation < populationSize) {
            double randomMutationProbability = Math.random();                          // generate random number
            if (randomMutationProbability <= mutationProb) {
                String chromosome = populationList.get(getPopulation);                 // get random string from population
                int kthBit = (int) (Math.random() * numOfNodes);                       // get random bit from the string
                String temp = chromosome.substring(kthBit, kthBit + 1);
                if (temp.equals("0"))                                                  // flip the value of random bit
                    temp = "1";
                else
                    temp = "0";

                chromosome = chromosome.substring(0, kthBit) + temp + chromosome.substring(kthBit + 1);     // rebuild string with flipped bit

                populationList.set(getPopulation, chromosome);                          // save changes to the global list
            }
            getPopulation++;
        }

    }

    String getFileOutput() {
        return fileOutput;
    }

    void runAllCombinations(String fileName) throws IOException {
        GraphReader fileReader = new GraphReader();
        fileReader.readGraphFile("src/input/" + fileName + ".txt");
        System.out.println("file read");
        GeneticAlgorithm genetic;
        BufferedWriter writer;
        String fileOutput = "src/output " + fileName + "/";

        double mutation = 1.0 / fileReader.getNumberOfVertex();
        //genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 100, 100, 0.5, mutation, fileReader.getWeights(),
        //        fileReader.getEdges(), fileReader.getWeighted_adjacency());
        //genetic.runGeneticAlgorithm();
        //writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_100_100_05_" + mutation + ".txt"));
        //writer.write(genetic.getFileOutput());
        //writer.close();
        //System.out.println(fileName + "_txt_100_100_05_" + mutation);
////
        //genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 100, 100, 0.5, 0.05, fileReader.getWeights(),
        //        fileReader.getEdges(), fileReader.getWeighted_adjacency());
        //genetic.runGeneticAlgorithm();
        //writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_100_100_05_005.txt"));
        //writer.write(genetic.getFileOutput());
        //writer.close();
        //System.out.println(fileName + "_txt_100_100_05_005");
////
        //genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 100, 100, 0.9, mutation, fileReader.getWeights(),
        //        fileReader.getEdges(), fileReader.getWeighted_adjacency());
        //genetic.runGeneticAlgorithm();
        //writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_100_100_09_" + mutation + ".txt"));
        //writer.write(genetic.getFileOutput());
        //writer.close();
        //System.out.println(fileName + "_txt_100_100_09_" + mutation);

        genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 100, 100, 0.9, 0.05, fileReader.getWeights(),
                fileReader.getEdges(), fileReader.getWeighted_adjacency());
        genetic.runGeneticAlgorithm();
        writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_100_100_09_005.txt"));
        writer.write(genetic.getFileOutput());
        writer.close();
        System.out.println(fileName + "_txt_100_100_09_005");

        genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 100, 200, 0.5, mutation, fileReader.getWeights(),
                fileReader.getEdges(), fileReader.getWeighted_adjacency());
        genetic.runGeneticAlgorithm();
        writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_100_200_05_" + mutation + ".txt"));
        writer.write(genetic.getFileOutput());
        writer.close();
        System.out.println(fileName + "_txt_100_200_05_" + mutation);

        genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 100, 200, 0.5, 0.05, fileReader.getWeights(),
                fileReader.getEdges(), fileReader.getWeighted_adjacency());
        genetic.runGeneticAlgorithm();
        writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_100_200_05_005.txt"));
        writer.write(genetic.getFileOutput());
        writer.close();
        System.out.println(fileName + "_txt_100_200_05_005");

        genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 100, 200, 0.9, mutation, fileReader.getWeights(),
                fileReader.getEdges(), fileReader.getWeighted_adjacency());
        genetic.runGeneticAlgorithm();
        writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_100_200_09_" + mutation + ".txt"));
        // writer = new BufferedWriter(new FileWriter(fileOutput + "/100_200_09.txt"));
        // TODO eski halinde burada mutasyon eklenmemi� output file lar�nda isimlere dikkat et
        writer.write(genetic.getFileOutput());
        writer.close();
        System.out.println(fileName + "_txt_100_200_09_" + mutation);

        genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 100, 200, 0.9, 0.05, fileReader.getWeights(),
                fileReader.getEdges(), fileReader.getWeighted_adjacency());
        genetic.runGeneticAlgorithm();
        writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_100_200_09_005.txt"));
        writer.write(genetic.getFileOutput());
        writer.close();
        System.out.println(fileName + "_txt_100_200_09_005");

        genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 400, 100, 0.5, mutation, fileReader.getWeights(),
                fileReader.getEdges(), fileReader.getWeighted_adjacency());
        genetic.runGeneticAlgorithm();
        writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_400_100_05_" + mutation + ".txt"));
        writer.write(genetic.getFileOutput());
        writer.close();
        System.out.println(fileName + "_txt_400_100_05_" + mutation);

        genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 400, 100, 0.5, 0.05, fileReader.getWeights(),
                fileReader.getEdges(), fileReader.getWeighted_adjacency());
        genetic.runGeneticAlgorithm();
        writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_400_100_05_005.txt"));
        writer.write(genetic.getFileOutput());
        writer.close();
        System.out.println(fileName + "_txt_400_100_05_005");

        genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 400, 100, 0.9, mutation, fileReader.getWeights(),
                fileReader.getEdges(), fileReader.getWeighted_adjacency());
        genetic.runGeneticAlgorithm();
        writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_400_100_09_" + mutation + ".txt"));
        writer.write(genetic.getFileOutput());
        writer.close();
        System.out.println(fileName + "_txt_400_100_09_" + mutation);

        genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 400, 100, 0.9, 0.05, fileReader.getWeights(),
                fileReader.getEdges(), fileReader.getWeighted_adjacency());
        genetic.runGeneticAlgorithm();
        writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_400_100_09_005.txt"));
        writer.write(genetic.getFileOutput());
        writer.close();
        System.out.println(fileName + "_txt_400_100_09_005");

        genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 400, 200, 0.5, mutation, fileReader.getWeights(),
                fileReader.getEdges(), fileReader.getWeighted_adjacency());
        genetic.runGeneticAlgorithm();
        writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_400_200_05_" + mutation + ".txt"));
        writer.write(genetic.getFileOutput());
        writer.close();
        System.out.println(fileName + "_txt_400_200_05_" + mutation);

        genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 400, 200, 0.5, 0.05, fileReader.getWeights(),
                fileReader.getEdges(), fileReader.getWeighted_adjacency());
        genetic.runGeneticAlgorithm();
        writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_400_200_05_005.txt"));
        writer.write(genetic.getFileOutput());
        writer.close();
        System.out.println(fileName + "_txt_400_200_05_005");

        genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 400, 200, 0.9, mutation, fileReader.getWeights(),
                fileReader.getEdges(), fileReader.getWeighted_adjacency());
        genetic.runGeneticAlgorithm();
        writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_400_200_09_" + mutation + ".txt"));
        writer.write(genetic.getFileOutput());
        writer.close();
        System.out.println(fileName + "_txt_400_200_09_" + mutation);

        genetic = new GeneticAlgorithm(fileReader.getNumberOfVertex(), 400, 200, 0.9, 0.05, fileReader.getWeights(),
                fileReader.getEdges(), fileReader.getWeighted_adjacency());
        genetic.runGeneticAlgorithm();
        writer = new BufferedWriter(new FileWriter(fileOutput + fileName + "_txt_400_200_09_005.txt"));
        writer.write(genetic.getFileOutput());
        writer.close();
        System.out.println(fileName + "_txt_400_200_09_005");

    }

}
