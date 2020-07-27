package TwoDMaze;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.LinkedList;

public class Square extends JPanel {


    private final LinkedList<Wall> walls = new LinkedList<>(); // Sqaures' Walls  that will be painted.
    private boolean up;             // wall status on top of the square
    private boolean down;           // wall status on bottom of the square
    private boolean right;          // wall status on right of the square
    private boolean left;           // wall status on top of the square
    private boolean expanded;       // expanded status for gui
    private boolean added;          // added status for frontier list
    private boolean goal;           // goal status for gui
    private boolean path;           // path status for gui
    private int depth;              // depth of the square
    private int row;                // row location of the square
    private int column;             // column location of the square
    private String squareType;      // square type of the square

     Square() {
        this.setSize(50, 50);
    }

    boolean isAdded() {
        return added;
    }

    void setAdded(boolean added) {
        this.added = added;
    }

    boolean isExpanded() {
        return expanded;
    }

    void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    int getDepth() {
        return depth;
    }

    void setDepth(int depth) {
        this.depth = depth;
    }

    boolean isUp() {
        return up;
    }

     void setUp(boolean up) {
        this.up = up;
    }

    boolean isDown() {
        return down;
    }

    void setDown(boolean down) {
        this.down = down;
    }

    boolean isRight() {
        return right;
    }

    void setRight(boolean right) {
        this.right = right;
    }

    boolean isLeft() {
        return left;
    }

    void setLeft(boolean left) {
        this.left = left;
    }

    String getSquareType() {
        return squareType;
    }

    void setSquareType(String squareType) {
        this.squareType = squareType;
    }

    int getRow() {
        return row;
    }

    void setRow(int row) {
        this.row = row;
    }

    int getColumn() {
        return column;
    }

    void setColumn(int column) {
        this.column = column;
    }

    void setGoal(boolean goal) {
        this.goal = goal;
    }

    void setPath(boolean path) {
        this.path = path;
    }


    private void addWall(int x1, int y1, int x2, int y2, Color color) {
        walls.add(new Wall(x1, y1, x2, y2, color));
    }

    // add walls to gui
    void check() {
        if (up) addWall(0, 0, 50, 0, Color.BLACK);
        else addWall(0, 0, 50, 0, Color.LIGHT_GRAY);

        if (down) addWall(0, 50, 50, 50, Color.BLACK);
        else addWall(0, 50, 50, 50, Color.LIGHT_GRAY);

        if (right) addWall(50, 0, 50, 50, Color.BLACK);
        else addWall(50, 0, 50, 50, Color.LIGHT_GRAY);

        if (left) addWall(0, 0, 0, 50, Color.BLACK);
        else addWall(0, 0, 0, 50, Color.LIGHT_GRAY);
        repaint();
    }

    // paint frame
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Font f = new Font("Dialog", Font.PLAIN, 30);
        if (this.goal) g.setColor(Color.RED);                // set goal square color to red
        else if (this.expanded) g.setColor(Color.ORANGE);     // set expanded square color to orange
        else g.setColor(Color.WHITE);                       // set normal square color to white
        if (this.path) g.setColor(Color.GREEN);               // set solution path color to green

        g.fillRect(0, 0, 50, 50);
        if (!this.squareType.equals("n")) {                  // square type is not normal
            g2.setColor(Color.BLACK);                       // set square label color to black
            switch (this.squareType) {
                case "s":                                   // draw label S for start square
                    g2.setFont(f);
                    g2.drawString("S", 15, 35);
                    break;
                case "t":                                   // draw label T for trap squares
                    g2.setFont(f);
                    g2.drawString("T", 15, 35);
                    break;
                case "g":                                   // draw label G for goal squares
                    g2.setFont(f);
                    g2.drawString("G", 15, 35);
                    break;
                default:
            }
        }

        for (Wall wall : walls) {                           // draw wall squares
            g2.setColor(wall.color);
            g2.setStroke(new BasicStroke(5));
            g2.draw(new Line2D.Float(wall.x1, wall.y1, wall.x2, wall.y2));
        }
    }


    @Override
    public String toString() {
        return "(" + row +
                ", " + column +
                ")";
    }
}
