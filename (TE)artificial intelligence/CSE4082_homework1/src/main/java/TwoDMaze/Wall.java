package TwoDMaze;

import java.awt.*;

public class Wall {
    final int x1;
    final int y1;
    final int x2;
    final int y2;
    final Color color;

    public Wall(int x1, int y1, int x2, int y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }
}
