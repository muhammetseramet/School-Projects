package main.java.GeneticAlgorithm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // prompt for the file name
        System.out.print("Enter file name(without .txt): ");
        String fileName = scanner.next();

        // prompt for the execution selection
        System.out.println("1) Run all combinations \n" + "2) Run with given inputs");
        System.out.print("Enter selection: ");
        int selection = scanner.nextInt();

        //if selection is 1, algorithm is executed with all combinations in the project pdf for a given input file
        if (selection == 1) {
            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();

            try {
                geneticAlgorithm.runAllCombinations(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //if selection is 2, algorithm is executed with entered parameters for a given input file
        } else if (selection == 2) {

            GraphReader graphReader = new GraphReader();
            graphReader.readGraphFile("src/input/" + fileName + ".txt");

            System.out.print("Number of generations: ");
            int numOfGenerations = scanner.nextInt();

            System.out.print("Population size: ");
            int populationSize = scanner.nextInt();

            System.out.print("Crossover probability: ");
            double crossoverProb = scanner.nextDouble();

            System.out.print("Mutation probability(-1 for 1/n): ");
            double mutationProb = scanner.nextDouble();
            if (mutationProb == -1.0) {
                mutationProb = 1.0 / graphReader.getNumberOfVertex();
            }

            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(graphReader.getNumberOfVertex(),
                    numOfGenerations, populationSize, crossoverProb, mutationProb, graphReader.getWeights(),
                    graphReader.getEdges(), graphReader.getWeighted_adjacency());
            
            geneticAlgorithm.runGeneticAlgorithm();
            BufferedWriter writer;
            try {
                writer = new BufferedWriter(new FileWriter("src/output/" + fileName + "_txt_" + numOfGenerations + "_" + populationSize + "_" + crossoverProb + "_" + mutationProb + ".txt"));
                writer.write(geneticAlgorithm.getFileOutput());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        scanner.close();

    }
}
