package TwoDMaze;

import java.util.*;

import static TwoDMaze.Utils.*;

class SearchAlgorithms {

    static void BFS(Maze maze) throws InterruptedException {
        ArrayList<Square> expandedSquareList = new ArrayList<>();   // expanded frontier square list

        Queue<ArrayList<Square>> frontier = new LinkedList<>();     // frontier should be FIFO, so it is Queue

        search("Breadth First Search", frontier, maze, expandedSquareList);     // make search on BFS

    }

    static boolean DFS(int depthLimit, Maze maze) throws InterruptedException {
        boolean IDControl = false;      // ID goal state found info
        int algo_chosen = 1;            // DFS algorithm code
        String algorithmName = "Depth First Search";

        ArrayList<Square> expandedSquareList = new ArrayList<>();   // expanded square list
        Stack<ArrayList<Square>> frontier = new Stack<>();          // frontier should be LIFO, so it is Stack

        addStartSquare(maze, frontier);    // initially add start square to start search

        while (!frontier.isEmpty()) {       // keep going until frontier is empty

            ArrayList<Square> path = frontier.pop();    // get node from frontier
            int initialDepth = 0;   // depth of square
            if (depthLimit != Integer.MAX_VALUE) { // ID part to initialize all depth
                algo_chosen = 3;    // ID algorithm code
                algorithmName = "Iterative Deepening";
                for (Square s : path) {   // set depth for all squares in current path
                    maze.getSquares()[s.getRow()][s.getColumn()].setDepth(initialDepth++);
                }
            }

            Square frontierSquare = path.get(path.size() - 1);  // get last expanded square

            if (frontierSquare.getDepth() < depthLimit) {   // current depth must be smaller than depth limit
                checkSquares(maze, frontierSquare, path, frontier, algo_chosen);    // check all neighbor squares on current square
            }

            if (frontierSquare.isExpanded()) continue;  // square is already expanded, keep going
            frontierSquare.setExpanded(true);       // square is expanded now, so set expanded info
            expandedSquareList.add(frontierSquare); // add square to frontier
            if (frontierSquare.getSquareType().equals("g")) {   // if it is a goal state
                IDControl = true;   // ID goal state found info
                maze.getSquares()[frontierSquare.getRow()][frontierSquare.getColumn()].setGoal(true); // for gui
                maze.repaint();
                Thread.sleep(300);
                printResult(maze, algorithmName, path, expandedSquareList); // print solution path
                break;
            }
            maze.repaint();
            Thread.sleep(300);
        }
        return IDControl;
    }

    static void ID(Maze maze) throws InterruptedException {
        boolean IDControl = false;  // ID goal state found info
        int depthLimit = 0;
        while (!IDControl) {    // until goal state found
            IDControl = DFS(depthLimit, maze);  // make DFS with specified depth

            if (!IDControl) {   // if DFS couldn't find a goal state, reset all changes for all squares
                for (int i = 0; i < maze.getRow(); i++) {
                    for (int j = 0; j < maze.getColumn(); j++) {
                        maze.getSquares()[i][j].setExpanded(false);     // reset expanded info of a square
                        maze.getSquares()[i][j].setAdded(false);        // reset added info of a square
                    }
                }
            }
            maze.repaint();
            depthLimit++;   // increase depth
        }
    }


    static void uniformCost(Maze maze) throws InterruptedException {
        ArrayList<Square> expandedSquareList = new ArrayList<>();   // expanded frontier square list

        Queue<ArrayList<Square>> frontier = new PriorityQueue<>(1000, new UniformCostComparator()); //frontier should be PriorityQueue

        search("Uniform Cost Search", frontier, maze, expandedSquareList);      // make search on Uniform Cost Search

    }

    static void greedyBestFirst(Maze maze) throws InterruptedException {
        ArrayList<Square> expandedSquareList = new ArrayList<>();   // expanded frontier square list

        Queue<ArrayList<Square>> frontier = new PriorityQueue<>(1000, new GreedyComparator(maze)); //frontier should be PriorityQueue

        search("Greedy Best First Search", frontier, maze, expandedSquareList);     // make search on Greedy Best First

    }

    static void aStar(Maze maze) throws InterruptedException {
        ArrayList<Square> expandedSquareList = new ArrayList<>();   // expanded frontier square list

        Queue<ArrayList<Square>> frontier = new PriorityQueue<>(1000, new AStarComparator(maze)); //frontier should be PriorityQueue

        search("A* Heuristic Search", frontier, maze, expandedSquareList);      // make search on A* Heuristic

    }
}
