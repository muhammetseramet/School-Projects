package TwoDMaze;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // read text file
        MazeReader mazeReader = new MazeReader();
        mazeReader.readMazeFile("src/input/maze.txt");
        
        String[] options = {"Depth First Search",
                "Breadth First Search",
                "Iterative Deepening",
                "Uniform Cost Search",
                "Greedy Best First Search",
                "A* Heuristic Search"};

        // create dialog and get selected algorithm
        String input = (String) JOptionPane.showInputDialog(null, "Which algorithm would you like to use?",
                null, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        while (true) {
            // create maze
            Maze maze = new Maze(mazeReader);
            maze.createMazeSquares();

            // cancel pressed, exit program
            if (input == null) {
                maze.setVisible(false);
                maze.dispose();
                break;
            }

            switch (input) {
                case "Depth First Search":
                    SearchAlgorithms.DFS(Integer.MAX_VALUE, maze);

                    break;

                case "Breadth First Search":
                    SearchAlgorithms.BFS(maze);

                    break;

                case "Iterative Deepening":
                    SearchAlgorithms.ID(maze);
                    break;

                case "Uniform Cost Search":
                    SearchAlgorithms.uniformCost(maze);

                    break;

                case "Greedy Best First Search":
                    SearchAlgorithms.greedyBestFirst(maze);

                    break;

                case "A* Heuristic Search":
                    SearchAlgorithms.aStar(maze);

                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + input);
            }

            maze.repaint();
            Thread.sleep(300);

            // repeat process
            input = (String) JOptionPane.showInputDialog(null, "Which algorithm would you like to use?",
                    null, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            maze.setVisible(false);
            maze.dispose();
        }
    }
}
