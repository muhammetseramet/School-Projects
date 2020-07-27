package TwoDMaze;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Maze extends JFrame {
    private Square[][] squares;                 // all squares on the maze
    private ArrayList<Node> verticalWalls;      // vertical walls on the maze
    private ArrayList<Node> horizontalWalls;    // horizontal walls on the maze
    private ArrayList<Node> goalSquares;        // goal squares on the maze
    private ArrayList<Node> trapSquares;        // trap squares on the maze
    private Node startSquare;                   // start square on the maze
    private int row, column;                    // size of the maze

    // construct maze with given text input
    Maze(MazeReader mazeReader) {
        this.verticalWalls = mazeReader.getVerticleWalls();     // set vertical walls of maze
        this.horizontalWalls = mazeReader.getHorizontalWalls(); // set horizontal walls of maze
        this.goalSquares = mazeReader.getGoalSquares();         // set goal squares of maze
        this.trapSquares = mazeReader.getTrapSquares();         // set trap squares of maze
        this.startSquare = mazeReader.getStartSquare();         // set start square of maze
        this.row = mazeReader.getRow();                         // set row size of the maze
        this.column = mazeReader.getColumn();                   // set column size of the maze
    }


    void createMazeSquares() {

        // parameters of MAZE frame
        this.setPreferredSize(new Dimension(this.column * 54, this.row * 54));
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setTitle("MAZE");
        this.setLocation((int) (screenSize.getWidth() - this.row * 50) / 2, (int) (screenSize.getHeight() - this.column * 50) / 2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(this.row, this.column));

        // set square size
        this.squares = new Square[this.row][this.column];

        // initialize maze with normal type squares
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.column; j++) {
                this.squares[i][j] = new Square();
                this.squares[i][j].setRow(i);
                this.squares[i][j].setColumn(j);
                this.squares[i][j].setSquareType("n");
            }
        }

        squares[startSquare.getRow()][startSquare.getColumn()].setSquareType("s");  // set starts to maze

        for (Node node : goalSquares) {
            squares[node.getRow()][node.getColumn()].setSquareType("g");            // set goals to maze
        }

        for (Node node : trapSquares) {
            squares[node.getRow()][node.getColumn()].setSquareType("t");            // set traps to maze
        }

        for (Node node : verticalWalls) {                                           // set vertical walls to maze
            squares[node.getRow()][node.getColumn()].setRight(true);                // current square has a wall on its right
            squares[node.getRow()][node.getColumn() + 1].setLeft(true);             // right next square has a wall on its left
        }

        for (Node node : horizontalWalls) {                                         // set horizontal walls to maze
            squares[node.getRow()][node.getColumn()].setDown(true);                 // current square has a wall on its bottom
            squares[node.getRow() + 1][node.getColumn()].setUp(true);               // lower square has a wall on its top
        }

        for (int i = 0; i < this.column; i++) {                                     // set walls on top and bottom edges
            this.squares[0][i].setUp(true);                                         // set walls on top
            this.squares[this.row - 1][i].setDown(true);                            // set walls on bottom
        }
        for (int i = 0; i < this.row; i++) {                                        // set walls on left and right corner
            this.squares[i][0].setLeft(true);                                       // set walls on left
            this.squares[i][this.column - 1].setRight(true);                        // set walls on right
        }

        // set square settings for frame
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.column; j++) {
                squares[i][j].check();
                this.add(squares[i][j]);
            }
        }
        this.pack();
        this.setVisible(true);
    }


    Square[][] getSquares() {
        return squares;
    }

    ArrayList<Node> getGoalSquares() {
        return goalSquares;
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
        return "Maze{\n" +
                "squares=" + Arrays.toString(squares) +
                "\n verticleWalls=" + verticalWalls +
                "\n horizontalWalls=" + horizontalWalls +
                "\n goalSquares=" + goalSquares +
                "\n trapSquares=" + trapSquares +
                "\n startSquare=" + startSquare +
                "\n row=" + row +
                "\n column=" + column +
                '}';
    }
}

