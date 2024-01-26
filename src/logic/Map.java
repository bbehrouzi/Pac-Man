package logic;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glRectf;

public class Map {

    private final int width = 28;
    private final int height = 36;
    private final int factor = 100;
    private final int[][] map = new int[width][height];
    private final int[][] grid = new int[width*factor][height*factor];

    public Map() {
        init();
    }

    private void init() {
        addIllegalSpace();
        createGrid();
        addPellets();
    }

    private void addIllegalSpace() {
        // Bottom
        for (int x = 0; x <= 27; x++) {
            for (int y = 0; y <= 2; y++) {
                map[x][y] = 1;
            }
        }
        // Top
        for (int x = 0; x <= 27; x++) {
            for (int y = 32; y <= 35; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 13; x <= 13; x++) {
            for (int y = 28; y <= 31; y++) {
                map[x][y] = 1;
            }
        }
        // Left
        for (int x = 0; x <= 0; x++) {
            for (int y = 19; y <= 31; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 0; x <= 0; x++) {
            for (int y = 3; y <= 17; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 1; x <= 2; x++) {
            for (int y = 7; y <= 8; y++) {
                map[x][y] = 1;
            }
        }
        // Under tunnel
        for (int x = 1; x <= 5; x++) {
            for (int y = 13; y <= 17; y++) {
                map[x][y] = 1;
            }
        }
        // Over tunnel
        for (int x = 1; x <= 5; x++) {
            for (int y = 19; y <= 23; y++) {
                map[x][y] = 1;
            }
        }
        // Top shapes
        for (int x = 10; x <= 13; x++) {
            for (int y = 25; y <= 26; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 13; x <= 13; x++) {
            for (int y = 22; y <= 24; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 2; x <= 5; x++) {
            for (int y = 28; y <= 30; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 7; x <= 11; x++) {
            for (int y = 28; y <= 30; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 2; x <= 5; x++) {
            for (int y = 25; y <= 26; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 7; x <= 8; x++) {
            for (int y = 19; y <= 26; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 9; x <= 11; x++) {
            for (int y = 22; y <= 23; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 7; x <= 8; x++) {
            for (int y = 13; y <= 17; y++) {
                map[x][y] = 1;
            }
        }
        // Prison
        for (int x = 10; x <= 13; x++) {
            for (int y = 16; y <= 20; y++) {
                map[x][y] = 1;
            }
        }
        // Bottom shapes
        for (int x = 10; x <= 13; x++) {
            for (int y = 13; y <= 14; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 13; x <= 13; x++) {
            for (int y = 10; y <= 13; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 2; x <= 5; x++) {
            for (int y = 10; y <= 11; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 4; x <= 5; x++) {
            for (int y = 7; y <= 9; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 7; x <= 11; x++) {
            for (int y = 10; y <= 11; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 2; x <= 11; x++) {
            for (int y = 4; y <= 5; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 7; x <= 8; x++) {
            for (int y = 6; y <= 8; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 10; x <= 13; x++) {
            for (int y = 7; y <= 8; y++) {
                map[x][y] = 1;
            }
        }
        for (int x = 13; x <= 13; x++) {
            for (int y = 4; y <= 6; y++) {
                map[x][y] = 1;
            }
        }
        // Mirror map array
        for(int x = 0; x < width/2; x++) {
            System.arraycopy(map[x], 0, map[width-x-1], 0, height);
        }
    }

    private void createGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int i = x*factor; i < (x+1)*factor; i++) {
                    for (int j = y*factor; j < (y+1)*factor; j++) {
                        grid[i][j] = map[x][y];
                    }
                }
            }
        }
    }

    private void addPellets() {
        for (int x = 0; x < width; x++) {
            for (int y = 24; y < height; y++) {
                if ((x == 1 || x == 26) && y == 29) {
                    map[x][y] = 3; // Power-up
                } else if (map[x][y] == 0) {
                    map[x][y] = 2; // Dot
                }
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < 13; y++) {
                if ((x == 1 || x == 26) && y == 9) {
                    map[x][y] = 3; // Power-up
                } else if (!((x == 13 || x == 14) && y == 9)) {
                    if (map[x][y] == 0) {
                        map[x][y] = 2; // Dot
                    }
                }
            }
        }
        for (int y=12; y<=23; y++) map[6][y] = 2;
        for (int y=12; y<=23; y++) map[21][y] = 2;
    }

    public void addFruit() {
        map[13][15] = 4;
    }

    public void draw() {
        float tileWidth = 2.0f/width;
        float tileHeight = 2.0f/height;
        float xStart = -1.0f;
        float yStart = -1.0f;
        for(int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x][y] == 1) {
                    glColor3f(0.0f, 0.0f, 1.0f);
                    glRectf(xStart+(x*tileWidth), yStart+(y*tileHeight), xStart+((x+1)*tileWidth), yStart+(y+1)*tileHeight);
                }
                // Dots
                if (map[x][y] == 2) {
                    glColor3f(1.0f, 1.0f, 1.0f);
                    glRectf(xStart+((x+0.4f)*tileWidth), yStart+((y+0.4f)*tileHeight),xStart+((x+0.6f)*tileWidth), yStart+(y+0.6f)*tileHeight);
                }
                // Power-ups
                if (map[x][y] == 3) {
                    glColor3f(1.0f, 1.0f, 1.0f);
                    glRectf(xStart+((x+0.2f)*tileWidth), yStart+((y+0.2f)*tileHeight),xStart+((x+0.8f)*tileWidth), yStart+(y+0.8f)*tileHeight);
                }
                if (map[x][y] == 4) {
                    glColor3f(0.8f, 0.0f, 1.0f);
                    glRectf(xStart+((x+0.6f)*tileWidth), yStart+((y+0.1f)*tileHeight),xStart+((x+1.4f)*tileWidth), yStart+(y+0.9f)*tileHeight);
                }
            }
        }
    }

    // For the ghosts
    public boolean canGoUp(int xPosL, int yPosL, int xPosR, int yPosR, int mode) {
        if ((xPosL >= 12* factor && xPosR <= 18* factor -1) && yPosL == 9* factor && mode < 2) {
            return false;
        }
        if ((xPosL >= 12* factor && xPosR <= 16* factor -1) && yPosL == 21* factor && mode < 2) {
            return false;
        }
        return grid[xPosL][yPosL+1] != 1 && grid[xPosR][yPosR+1] != 1;
    }

    public boolean canGoDown(int xPosL, int yPosL, int xPosR, int yPosR) {
        return grid[xPosL][yPosL-1] != 1 && grid[xPosR][yPosR-1] != 1;
    }

    public boolean canGoLeft(int xPosL, int yPosL, int xPosR, int yPosR) {
        return grid[xPosL-1][yPosL] != 1 && grid[xPosR-1][yPosR] != 1;
    }

    public boolean canGoRight(int xPosL, int yPosL, int xPosR, int yPosR) {
        return grid[xPosL+1][yPosL] != 1 && grid[xPosR+1][yPosR] != 1;
    }

    // For PacMan
    public boolean canGoUp(int xPosM, int yPosM) {
        return grid[xPosM][yPosM+1] != 1;
    }

    public boolean canGoDown(int xPosM, int yPosM) {
        return grid[xPosM][yPosM-1] != 1;
    }

    public boolean canGoLeft(int xPosM, int yPosM) {
        return grid[xPosM-1][yPosM] != 1;
    }

    public boolean canGoRight(int xPosM, int yPosM) {
        return grid[xPosM+1][yPosM] != 1;
    }

    public void changeTile(int x, int y, int i) {
        map[x][y] = i;
    }

    public int checkTile(int x, int y) {
        return map[x][y];
    }

    public int checkGrid(int x, int y) {
        return grid[x][y];
    }

    public int getFactor() {
        return factor;
    }

    public int[] getTile(int xPosL, int yPosL) {
        int[] a = new int[2];
        a[0] = (xPosL+factor/2)/factor;
        a[1] = (yPosL+factor/2)/factor;
        return a;
    }

}