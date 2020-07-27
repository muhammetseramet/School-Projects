package TwoDMaze;

import java.util.ArrayList;
import java.util.Comparator;

import static TwoDMaze.Utils.costFunc;
import static TwoDMaze.Utils.heuristicFunc;


class UniformCostComparator implements Comparator<ArrayList<Square>> {

    @Override
    public int compare(ArrayList<Square> path1, ArrayList<Square> path2) {
        return Integer.compare(costFunc(path1), costFunc(path2));
    }
}

class GreedyComparator implements Comparator<ArrayList<Square>> {
    private Maze maze;

    GreedyComparator(Maze maze) {
        this.maze = maze;
    }

    @Override
    public int compare(ArrayList<Square> path1, ArrayList<Square> path2) {
        return Integer.compare(heuristicFunc(this.maze, path1), heuristicFunc(this.maze, path2));
    }
}

class AStarComparator implements Comparator<ArrayList<Square>> {
    private Maze maze;

    AStarComparator(Maze maze) {
        this.maze = maze;
    }

    @Override
    public int compare(ArrayList<Square> path1, ArrayList<Square> path2) {
        return Integer.compare(costFunc(path1) + heuristicFunc(this.maze, path1), costFunc(path2) + heuristicFunc(this.maze, path2));
    }
}

