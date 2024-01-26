package logic;

// Red ghost
public class Blinky extends Ghost {

    private int elroy; // Aggressive behaviour

    public Blinky(Map map, PacMan pac, Mode mode) {
        super(map, pac, mode);
        super.scatterTile[0] = 25;
        super.scatterTile[1] = 35;
        oColor[0] = 1.0f;
        oColor[1] = 0.0f;
        oColor[2] = 0.0f;
        this.init();
    }

    @Override
    public void init() {
        setColor(oColor[0], oColor[1], oColor[2]);
        super.startX = 13*factor+(factor/2);
        super.startY = 21*factor;
        up = true;
        roam = false;
        pelletThreshold = 0;
        elroy = 0;
        super.init();
    }

    // Gets overridden because blinky's start position is outside of the prison
    @Override
    public void prison(int lastMode) {
        if (pos[0][0] == startX && pos[0][1] == startY) {
            exit = false;
            if (mode == 3) {
                enter = true; // Enter house after being eaten
            }
        }
        if (pos[0][0] == startX && pos[0][1] == 17*factor) {
            enter = false;
            exit = true;
            if (mode == 3) {
                mode = lastMode;
                setColor(oColor[0], oColor[1], oColor[2]);
            }
        }
    }

    @Override
    public void exit() {
        changePos(0);
    }

    @Override
    public void enter() {
        changePos(1);
    }

    @Override
    public int[] getChaseTile() {
        return super.pac.getTile();
    }

    @Override
    public int[] getScatterTile() {
        // ghosts.Blinky keeps chasing in scatter mode when 20 pellets remain
        if (elroy == 0) {
            return scatterTile;
        } else {
            return getChaseTile();
        }
    }

    public void setElroy(int stage) {
        elroy = stage;
    }

    public int getElroy(){
        return elroy;
    }

}