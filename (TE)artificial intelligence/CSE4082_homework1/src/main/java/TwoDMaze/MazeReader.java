package TwoDMaze;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MazeReader {
    private ArrayList<Node> verticleWalls;      // vertical walls on the maze
    private ArrayList<Node> horizontalWalls;    // horizontal walls on the maze
    private ArrayList<Node> goalSquares;        // goal squares on the maze
    private ArrayList<Node> trapSquares;        // trap squares on the maze
    private Node startSquare;                   // start square on the maze
    private int row, column;                    // size of the maze

    // default constructor
    MazeReader() {
        this.verticleWalls = new ArrayList<>();
        this.horizontalWalls = new ArrayList<>();
        this.goalSquares = new ArrayList<>();
        this.trapSquares = new ArrayList<>();
        this.startSquare = null;
        this.row = 0;
        this.column = 0;
    }

    //read maze file and create nodes
    void readMazeFile(String fileName) {
        String inputLine;
        String[] input;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            inputLine = reader.readLine();
            input = inputLine.split("\\s");
            row = Integer.parseInt(input[0]);       // get row size maze
            column = Integer.parseInt(input[1]);    // get column size maze

            while ((inputLine = reader.readLine()) != null) {
                input = inputLine.split("\\s");     // split line by a space

                switch (input[0]) {     // get the type of the square
                    case "v":           // vertical wall type
                        verticleWalls.add(new Node(Integer.parseInt(input[1]), Integer.parseInt(input[2])));
                        break;

                    case "h":           // horizontal wall type
                        horizontalWalls.add(new Node(Integer.parseInt(input[1]), Integer.parseInt(input[2])));
                        break;

                    case "s":           // start square type
                        startSquare = new Node(Integer.parseInt(input[1]), Integer.parseInt(input[2]));
                        break;

                    case "t":           // trap square type
                        trapSquares.add(new Node(Integer.parseInt(input[1]), Integer.parseInt(input[2])));
                        break;

                    case "g":           // goal square type
                        goalSquares.add(new Node(Integer.parseInt(input[1]), Integer.parseInt(input[2])));
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ArrayList<Node> getVerticleWalls() {
        return verticleWalls;
    }

    ArrayList<Node> getHorizontalWalls() {
        return horizontalWalls;
    }

    ArrayList<Node> getGoalSquares() {
        return goalSquares;
    }

    ArrayList<Node> getTrapSquares() {
        return trapSquares;
    }

    Node getStartSquare() {
        return startSquare;
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "MazeReader{" +
                "verticleWalls=" + verticleWalls +
                ", horizontalWalls=" + horizontalWalls +
                ", goalSquares=" + goalSquares +
                ", trapSquares=" + trapSquares +
                ", startSquare=" + startSquare +
                ", row=" + row +
                ", column=" + column +
                '}';
    }
}
