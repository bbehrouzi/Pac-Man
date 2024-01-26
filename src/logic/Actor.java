package logic;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glRectf;

// This class contains all methods that both Pac-Man and the ghosts use
public abstract class Actor {

    protected Map map;
    protected final int[][] pos = new int[4][2];
    protected float[] colors = new float[3];
    protected int startX, startY;
    protected final int factor; // The factor the grid is greater than the map
    protected int direction; // The direction the actors is going in

    public Actor(Map map) {
        this.map = map;
        factor = map.getFactor();
    }

    protected void init() {
        // Lower left corner
        pos[0][0] = startX;
        pos[0][1] = startY;
        // Lower right corner
        pos[1][0] = startX+factor-1;
        pos[1][1] = startY;
        // Upper left corner
        pos[2][0] = startX; // x
        pos[2][1] = startY+factor-1; // y
        // Upper right corner
        pos[3][0] = startX+factor-1;
        pos[3][1] = startY+factor-1;
        direction = -1; // No direction
        setColor(colors[0], colors[1], colors[2]);
    }

    protected abstract void move();

    protected void changePos(int value) {
        // Up
        if (value == 0) {
            pos[0][1]++;
            pos[1][1]++;
            pos[2][1]++;
            pos[3][1]++;
        }
        // Down
        if (value == 1) {
            pos[0][1]--;
            pos[1][1]--;
            pos[2][1]--;
            pos[3][1]--;
        }
        // Left
        if (value == 2) {
            pos[0][0]--;
            pos[1][0]--;
            pos[2][0]--;
            pos[3][0]--;
        }
        // Right
        if (value == 3) {
            pos[0][0]++;
            pos[1][0]++;
            pos[2][0]++;
            pos[3][0]++;
        }
    }

    // When the actor leaves the map on either side his position will be changed to be on the opposing side
    public boolean teleport() {
        // Right side
        if (pos[0][0] >= 27*factor) {
            if (pos[0][0] == 29*factor) {
                pos[0][0] = -factor*2+1;
                pos[1][0] = pos[0][0]+factor-1;
                pos[2][0] = pos[0][0];
                pos[3][0] = pos[0][0]+factor-1;
            }
            return true;
        }
        // Left side
        if (pos[0][0] <= 0) {
            if (pos[0][0] == -2*factor) {
                pos[0][0] = 29*factor-1;
                pos[1][0] = pos[0][0]+factor-1;
                pos[2][0] = pos[0][0];
                pos[3][0] = pos[0][0]+factor-1;
            }
            return true;
        }
        return false;
    }

    public void draw() {
        float tileWidth = 2.0f/(28*factor);
        float tileHeight = 2.0f/(36*factor);
        float xStart = -1.0f;
        float yStart = -1.0f;
        glColor3f(colors[0], colors[1], colors[2]);
        glRectf(xStart+pos[0][0]*tileWidth, yStart+pos[0][1]*tileHeight, xStart+(pos[3][0]+1)*tileWidth, yStart+(pos[3][1]+1)*tileHeight);
    }

    public void setColor(float r, float g, float b) {
        colors[0] = r;
        colors[1] = g;
        colors[2] = b;
    }

    public int[] getTile() {
        return map.getTile(pos[0][0], pos[0][1]);
    }

    public int[][] getPos() {
        return pos;
    }

}