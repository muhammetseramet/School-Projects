package TwoDMaze;

public class Node {
    private int row;            // row position of node
    private int column;         // column position of node

    Node(int row, int column) {
        this.row = row;
        this.column = column;
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Node{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}