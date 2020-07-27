package TwoDMaze;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

class Utils {

    // chech West Square of the current squre
    private static void checkWestSquare(Maze maze, Square frontierSquare, ArrayList<Square> frontierSquareList, Object frontier, int algo_chosen) {
        ArrayList<Square> expandedFrontierSquareList;       // temp frontier to store current path and expand whenever it needed
        Square westSquare;

        if (!frontierSquare.isLeft()) {                                                                     // check wall on left side of the current square
            expandedFrontierSquareList = new ArrayList<>(frontierSquareList);                               // copy frontier to expanded frontier
            westSquare = maze.getSquares()[frontierSquare.getRow()][frontierSquare.getColumn() - 1];        // get west square of the current square
            expandedFrontierSquareList.add(westSquare);                                                     // expand temp frontier with west square

            switch (algo_chosen) {
                case 1: // DFS
                    if (!westSquare.isExpanded()) {
                        addFrontier(frontier, expandedFrontierSquareList);      // expand frontier with west square
                        westSquare.setAdded(true);                              // square added to frontier
                        westSquare.setDepth(frontierSquare.getDepth() + 1);     // set depth of the square

                    }
                    break;
                case 2: //BFS-UNIFORM-GREEDY-ASTAR
                    if (!westSquare.isAdded()) {
                        addFrontier(frontier, expandedFrontierSquareList);      // expand frontier with west square
                        westSquare.setAdded(true);                              // square added to frontier
                    }
                case 3: //IDS
                    // Check if westSquare is included in the frontierSquareList.
                    boolean include = false;
                    for (Square square : frontierSquareList) {
                        if (square.equals(westSquare)) {
                            include = true;
                            break;
                        }
                    }
                    //if not include,
                    if (!include) {
                        addFrontier(frontier, expandedFrontierSquareList);      // expand frontier with west square
                        westSquare.setAdded(true);                              // square added to frontier
                        westSquare.setDepth(frontierSquare.getDepth() + 1);     // set depth of the square
                    }

                    break;
                default:
                    System.out.println("checkWestSquare");
            }
        }
    }


    private static void checkSouthSquare(Maze maze, Square frontierSquare, ArrayList<Square> frontierSquareList, Object frontier, int algo_chosen) {
        ArrayList<Square> expandedFrontierSquareList;       // temp frontier to store current path and expand whenever it needed
        Square southSquare;

        if (!frontierSquare.isDown()) {                                                                     // check wall on down side of the current square
            expandedFrontierSquareList = new ArrayList<>(frontierSquareList);                               // copy frontier to expanded frontier
            southSquare = maze.getSquares()[frontierSquare.getRow() + 1][frontierSquare.getColumn()];       // get down square of the current square
            expandedFrontierSquareList.add(southSquare);                                                    // expand temp frontier with south square

            switch (algo_chosen) {
                case 1: // DFS
                    if (!southSquare.isExpanded()) {
                        addFrontier(frontier, expandedFrontierSquareList);      // expand frontier with down square
                        southSquare.setAdded(true);                             // square added to frontier
                        southSquare.setDepth(frontierSquare.getDepth() + 1);    // set depth of the square

                    }
                    break;
                case 2: //BFS-UNIFORM-GREEDY-ASTAR
                    if (!southSquare.isAdded()) {
                        addFrontier(frontier, expandedFrontierSquareList);      // expand frontier with down square
                        southSquare.setAdded(true);                             // square added to frontier
                    }
                    break;
                case 3: //IDS
                    // Check if westSquare is included in the frontierSquareList.
                    boolean include = false;
                    for (Square square : frontierSquareList) {
                        if (square.equals(southSquare)) {
                            include = true;
                            break;
                        }
                    }
                    //if not include,
                    if (!include) {
                        addFrontier(frontier, expandedFrontierSquareList);      // expand frontier with down square
                        southSquare.setAdded(true);                             // square added to frontier
                        southSquare.setDepth(frontierSquare.getDepth() + 1);    // set depth of the square
                    }

                    break;
                default:
                    System.out.println("checkSouthSquare");
            }
        }
    }


    private static void checkEastSquare(Maze maze, Square frontierSquare, ArrayList<Square> frontierSquareList, Object frontier, int algo_chosen) {
        ArrayList<Square> expandedFrontierSquareList;       // temp frontier to store current path and expand whenever it needed
        Square eastSquare;

        if (!frontierSquare.isRight()) {                                                                   // check wall on right side of the current square
            expandedFrontierSquareList = new ArrayList<>(frontierSquareList);                              // copy frontier to expanded frontier
            eastSquare = maze.getSquares()[frontierSquare.getRow()][frontierSquare.getColumn() + 1];       // get right square of the current square
            expandedFrontierSquareList.add(eastSquare);                                                    // expand temp frontier with right square

            switch (algo_chosen) {
                case 1: // DFS
                    if (!eastSquare.isExpanded()) {
                        addFrontier(frontier, expandedFrontierSquareList);     // expand frontier with right square
                        eastSquare.setAdded(true);                             // square added to frontier
                        eastSquare.setDepth(frontierSquare.getDepth() + 1);    // set depth of the square

                    }
                    break;
                case 2: //BFS-UNIFORM-GREEDY-ASTAR
                    if (!eastSquare.isAdded()) {
                        addFrontier(frontier, expandedFrontierSquareList);     // expand frontier with right square
                        eastSquare.setAdded(true);                             // square added to frontier
                    }
                    break;
                case 3: //IDS
                    // Check if westSquare is included in the frontierSquareList.
                    boolean include = false;
                    for (Square square : frontierSquareList) {
                        if (square.equals(eastSquare)) {
                            include = true;
                            break;
                        }
                    }
                    //if not include,
                    if (!include) {
                        addFrontier(frontier, expandedFrontierSquareList);     // expand frontier with right square
                        eastSquare.setAdded(true);                             // square added to frontier
                        eastSquare.setDepth(frontierSquare.getDepth() + 1);    // set depth of the square
                    }

                    break;
                default:
                    System.out.println("checkEastSquare");
            }
        }
    }


    private static void checkNorthSquare(Maze maze, Square frontierSquare, ArrayList<Square> frontierSquareList, Object frontier, int algo_chosen) {
        ArrayList<Square> expandedFrontierSquareList;       // temp frontier to store current path and expand whenever it needed
        Square northSquare;

        if (!frontierSquare.isUp()) {                                                                       // check wall on up side of the current square
            expandedFrontierSquareList = new ArrayList<>(frontierSquareList);                               // copy frontier to expanded frontier
            northSquare = maze.getSquares()[frontierSquare.getRow() - 1][frontierSquare.getColumn()];       // get up square of the current square
            expandedFrontierSquareList.add(northSquare);                                                    // expand temp frontier with north square

            switch (algo_chosen) {
                case 1: // DFS
                    if (!northSquare.isExpanded()) {
                        addFrontier(frontier, expandedFrontierSquareList);      // expand frontier with up square
                        northSquare.setAdded(true);                             // square added to frontier
                        northSquare.setDepth(frontierSquare.getDepth() + 1);    // set depth of the square

                    }
                    break;
                case 2: //BFS-UNIFORM-GREEDY-ASTAR
                    if (!northSquare.isAdded()) {
                        addFrontier(frontier, expandedFrontierSquareList);      // expand frontier with up square
                        northSquare.setAdded(true);                             // square added to frontier
                    }
                    break;
                case 3: //IDS
                    // Check if westSquare is included in the frontierSquareList.
                    boolean include = false;
                    for (Square square : frontierSquareList) {
                        if (square.equals(northSquare)) {
                            include = true;
                            break;
                        }
                    }
                    //if not include,
                    if (!include) {
                        addFrontier(frontier, expandedFrontierSquareList);      // expand frontier with up square
                        northSquare.setAdded(true);                             // square added to frontier
                        northSquare.setDepth(frontierSquare.getDepth() + 1);    // set depth of the square
                    }

                    break;
                default:
                    System.out.println("checkNorthSquare");
            }
        }
    }

    // print the result to src/output/output.txt
    static void printResult(Maze maze, String algorithmName, ArrayList<Square> frontierSquareList, ArrayList<Square> expandedSquareList) {
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        try {
            FileWriter fileWriter = new FileWriter("src/output/output.txt", true); //Set true for append mode
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.println("===============================================================================" +
                    "===============================================================================");
            printWriter.println("Date " + timeStamp);
            printWriter.println(algorithmName + "\n");

            printWriter.println("Solution Path : ");
            for (int i = 0; i < frontierSquareList.size(); i++) {
                frontierSquareList.get(i).setPath(true);
                maze.repaint();
                Thread.sleep(300);

                if (i == frontierSquareList.size() - 1) {
                    printWriter.print(frontierSquareList.get(i));
                    break;
                }
                printWriter.print(frontierSquareList.get(i) + " => ");
            }
            printWriter.print("\n\n");

            printWriter.println("The List of Expanded Nodes : ");
            for (int i = 0; i < expandedSquareList.size(); i++) {
                if (i == expandedSquareList.size() - 1) {
                    printWriter.print(expandedSquareList.get(i));
                    break;
                }
                printWriter.print(expandedSquareList.get(i) + " => ");
            }
            printWriter.print("\n\n");

            printWriter.println("The cost : " + calculatePathCost(frontierSquareList) + "\n");


            printWriter.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    // calculate total cost
    private static int calculatePathCost(ArrayList<Square> path) {
        int pathCost = -1;          // total cost
        for (Square s : path) {     // all states in path including start square
            if (s.getSquareType().equals("t")) pathCost += 7;   // cost for trap squares
            else pathCost++;                                    // cost for other squares
        }
        return pathCost;
    }

    @SuppressWarnings("unchecked")
    private static void addFrontier(Object frontier, ArrayList<Square> squareArrayList) {   // expand frontier depending on its type
        if (frontier instanceof Stack) {
            ((Stack) frontier).push(squareArrayList);
        } else if (frontier instanceof Queue) {
            ((Queue) frontier).add(squareArrayList);
        }

    }

    @SuppressWarnings("unchecked")
    private static ArrayList<Square> removeFrontier(Object frontier) {      // remove square from frontier depending on its type
        ArrayList<Square> path;
        if (frontier instanceof Stack) {
            path = (ArrayList<Square>) ((Stack) frontier).pop();
        } else if (frontier instanceof Queue) {
            path = (ArrayList<Square>) ((Queue) frontier).remove();
        } else {
            path = (ArrayList<Square>) ((PriorityQueue) frontier).poll();
        }
        return path;
    }

    static void addStartSquare(Maze maze, Object frontier) {        // initially add start square to the frontier
        ArrayList<Square> startNodeList = new ArrayList<>();        // to find path, squares should be inside of array list
        startNodeList.add(maze.getSquares()[maze.getStartSquare().getRow()][maze.getStartSquare().getColumn()]); // set start node with start square
        addFrontier(frontier, startNodeList);   // add start node to frontier
        maze.getSquares()[maze.getStartSquare().getRow()][maze.getStartSquare().getColumn()].setAdded(true);    // start node added to frontier
        maze.getSquares()[maze.getStartSquare().getRow()][maze.getStartSquare().getColumn()].setDepth(0);       // start node has depth 0

    }

    static void checkSquares(Maze maze, Square frontierSquare, ArrayList<Square> path, Object frontier, int algo_chosen) {

        checkEastSquare(maze, frontierSquare, path, frontier, algo_chosen); // check square's right side
        checkSouthSquare(maze, frontierSquare, path, frontier, algo_chosen);// check square's down side
        checkWestSquare(maze, frontierSquare, path, frontier, algo_chosen); // check square's left side
        checkNorthSquare(maze, frontierSquare, path, frontier, algo_chosen);// check square's up side
    }

    // implementing BFS, Uniform Cost Search, Greedy Best First Search, A* Heuristic Search algorithms
    static void search(String algorithmName, Queue<ArrayList<Square>> frontier, Maze maze, ArrayList<Square> expandedSquareList) throws InterruptedException {

        addStartSquare(maze, frontier);     // start square should be added first

        while (!frontier.isEmpty()) {       // until frontier is empty
            ArrayList<Square> path = removeFrontier(frontier);      // get first array list from frontier
            Square frontierSquare = path.get(path.size() - 1);      // get first square from array list


            checkSquares(maze, frontierSquare, path, frontier, 2);

            frontierSquare.setExpanded(true);           // all neighbor squares are checked
            expandedSquareList.add(frontierSquare);     // current square is expanded
            if (frontierSquare.getSquareType().equals("g")) {   // if current square is a goal square
                maze.getSquares()[frontierSquare.getRow()][frontierSquare.getColumn()].setGoal(true);   // goal found info for gui
                maze.repaint();
                Thread.sleep(300);
                printResult(maze, algorithmName, path, expandedSquareList);     // print path
                break;
            }
            maze.repaint();
            Thread.sleep(300);

        }
    }

    // calculate cost
    static int costFunc(ArrayList<Square> path) {
        int cost = -1; // total cost
        for (Square square : path) {// all states in path including start square
            cost++;// cost for other squares
            if (square.getSquareType().equals("t")) {// cost for trap squares
                cost += 6;
            }
        }
        return cost;
    }

    // calculate heuristic cost: manhattan distance
    static int heuristicFunc(Maze maze, ArrayList<Square> path) {
        int cost = Integer.MAX_VALUE;
        Square frontierSquare = path.get(path.size() - 1);      // get first square from path
        for (Node node : maze.getGoalSquares()) {               // calculate manhattan distance from current square to all goal states
            int distance = Math.abs(node.getRow() - frontierSquare.getRow()) + Math.abs(node.getColumn() - frontierSquare.getColumn());
            if (distance < cost) {  // calculate the closest goal state distance
                cost = distance;
            }
        }
        return cost;
    }
}