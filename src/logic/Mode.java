package logic;

public class Mode {

    /*
    1. ist getTile == nextTile, dann nextTile = getTIle+1 und berechneTile()
    2. move zu nextTile, dann nextTile = berechneTile


        Tile
           -> Koordinate
           -> move
           getkoor
           setMove
           getMove



     */

    private final Map map;
    private Ghost ghost;

    private final int[] door = {13, 21};
    private int[] targetTile = new int[2];
    private int[] tile = new int[2];
    private int[][] pos;
    private int mode;

    public Mode(Map map) {
        this.map = map;
    }

    public int direction(Ghost ghost) {
        this.ghost = ghost;
        mode = ghost.getMode();
        tile = this.ghost.getTile();
        pos = this.ghost.getPos();
        switch (mode) {
            case 0 -> {
                targetTile = this.ghost.getScatterTile();
                return chooseDirection(possibleDirections());
            }
            case 1 -> {
                targetTile = this.ghost.getChaseTile();
                return chooseDirection(possibleDirections());
            }
            case 2 -> {
                return frightened(possibleDirections());
            }
            case 3 -> {
                targetTile = door;
                return chooseDirection(possibleDirections());
            }
        }
        return -1;
    }

    private boolean[] possibleDirections() {
        boolean[] directions = new boolean[4];
        // The direction numbers are changed here bc that's the priority order: Up, Left, Down, Right
        if (!ghost.teleport() && map.canGoUp(pos[2][0], pos[2][1], pos[3][0], pos[3][1], mode) && ghost.getIllDir() != 0) {
            directions[0] = true;
        }
        if (!ghost.teleport() && map.canGoDown(pos[0][0], pos[0][1], pos[1][0], pos[1][1]) && ghost.getIllDir() != 1) {
            directions[2] = true;
        }
        if ((ghost.teleport() || map.canGoLeft(pos[2][0], pos[2][1], pos[0][0], pos[0][1])) && ghost.getIllDir() != 2) {
            directions[1] = true;
        }
        if ((ghost.teleport() || map.canGoRight(pos[3][0], pos[3][1], pos[1][0], pos[1][1])) && ghost.getIllDir() != 3) {
            directions[3] = true;
        }
        return directions;
    }

    private int chooseDirection(boolean[] directions) {
        int[] nextTile = new int[2];
        int distance = 10000; // Max distance
        int newDistance = 0;
        int direction = -1;
        for (int i = 0; i < 4; i++) {
            if (directions[i]) {
                if (i == 0) {
                    nextTile[0] = tile[0];
                    nextTile[1] = tile[1] + 1;
                    newDistance = calcDistance(nextTile, targetTile);
                }
                if (i == 2) {
                    nextTile[0] = tile[0];
                    nextTile[1] = tile[1] - 1;
                    newDistance = calcDistance(nextTile, targetTile);
                }
                if (i == 1) {
                    nextTile[0] = tile[0] - 1;
                    nextTile[1] = tile[1];
                    newDistance = calcDistance(nextTile, targetTile);
                }
                if (i == 3) {
                    nextTile[0] = tile[0] + 1;
                    nextTile[1] = tile[1];
                    newDistance = calcDistance(nextTile, targetTile);
                }
                if (newDistance < distance) {
                    distance = newDistance;
                    direction = i;
                }
            }
        }
        direction = fitDir(direction);
        ghost.setIllDir(direction);
        return direction;
    }

    private int fitDir(int dir) {
        if (dir == 1) {
            return 2;
        }
        if (dir == 2) {
            return 1;
        }
        if (dir == 0 ) {
            return 0;
        }
        if (dir == 3) {
            return 3;
        }
        return -1;
    }

    private int calcDistance(int[] nextTile, int[] targetTile) {
        int xDif = targetTile[0] - nextTile[0];
        int yDif = targetTile[1] - nextTile[1];
        return (int) (Math.pow(xDif, 2.0f) + Math.pow(yDif, 2.0f));
    }

    private int frightened(boolean[] directions) {
        int direction;
        int ranDir;
        do {
            ranDir = (int)(Math.random()*4.0);
        } while (!directions[ranDir]);
        direction = fitDir(ranDir);
        ghost.setIllDir(direction);
        return direction;
    }

}