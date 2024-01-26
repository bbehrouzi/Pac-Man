package logic;

import game.Game;

public abstract class Ghost extends Actor {

    protected final PacMan pac;
    protected final Mode modes;
    public float[] oColor = new float[3];
    public boolean enter, exit, roam;
    public int mode;
    protected int illDir = -1;
    protected boolean up;
    protected final int[] scatterTile = new int[2];
    protected int pelletThreshold;
    private boolean timeElapsed;
    private int pellets;

    public Ghost(Map map, PacMan pac, Mode mode) {
        super(map);
        this.pac = pac;
        this.modes = mode;
    }

    public void init() {
        super.init();
        mode = 0;
        exit = false;
        enter = false;
        pellets = Game.pellets;
    }

    @Override
    public void move() {
        if (exit) {
            exit();
        } else if (enter) {
            enter();
        } else if (roam) {
            roam();
        } else {
            changePos(modes.direction(this));
        }
    }

    public void spin() {
        int oldIllDir = getIllDir();
        setIllDir(oldIllDir);
    }

    public void prison(int lastMode) {
        if (pos[0][0] == 13*factor+(factor/2) && pos[0][1] == 21*factor) {
            exit = false;
            if (mode == 3) {
                enter = true; // Enter house after being eaten
            }
        }
        if (pos[0][0] == startX && pos[0][1] == startY) {
            if ((pellets- Game.pellets) >= pelletThreshold || timeElapsed) {
                roam = false;
                exit = true;
                timeElapsed = false;
            }
            enter = false;
            if (mode == 3) {
                exit = true;
                mode = lastMode;
                setColor(oColor[0], oColor[1], oColor[2]);
            }
        }
    }

    protected void roam() {
        if (up) {
            changePos(0);
        }
        if (!up) {
            changePos(1);
        }
        if (pos[0][1] == 19*factor) {
            up = false;
        }
        if (pos[0][1] == 17*factor) {
            up = true;
        }
    }

    public boolean inTunnel() {
        int[] a = map.getTile(pos[0][0], pos[0][1]);
        if (a[0] > 21 && a[1] == 17) {
            return true;
        }
        return a[0] < 6 && a[1] == 17;
    }

    public void setIllDir(int direction) {
        if (direction == 0) {
           illDir = 1;
        }
        if (direction == 1) {
            illDir = 0;
        }
        if (direction == 2) {
            illDir = 3;
        }
        if (direction == 3) {
            illDir = 2;
        }
    }

    public int getIllDir() {
        return illDir;
    }

    public int getMode() {
        return mode;
    }

    public void setTimeElapsed() {
        timeElapsed = true;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setThreshold(int value) {
        pelletThreshold = value;
    }

    public int[] getScatterTile() {
        return scatterTile;
    }

    public abstract int[] getChaseTile();

    protected abstract void exit();

    protected abstract void enter();

}