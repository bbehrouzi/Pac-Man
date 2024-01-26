package logic;

public class Pinky extends Ghost {

    public Pinky(Map map, PacMan pac, Mode mode) {
        super(map, pac, mode);
        super.scatterTile[0] = 2;
        super.scatterTile[1] = 35;
        oColor[0] = 1.0f;
        oColor[1] = 0.75f;
        oColor[2] = 0.79f;
        this.init();
    }

    @Override
    public void init() {
        setColor(oColor[0], oColor[1], oColor[2]);
        super.startX = 13*factor+(factor/2);
        super.startY = 18*factor;
        up = false;
        roam = true;
        pelletThreshold = 0;
        super.init();
    }

    @Override
    protected void exit() {
        changePos(0);
    }

    @Override
    protected void enter() {
        changePos(1);
    }

    @Override
    public int[] getChaseTile() {
        int[] targetTile = super.pac.getTile();
        int dir = pac.getDirection();
        if (dir == 0) {
            targetTile[1]+=4;
        }
        if (dir == 1) {
            targetTile[1]-=4;
        }
        if (dir == 2) {
            targetTile[0]-=4;
        }
        if (dir == 3) {
            targetTile[0]+=4;
        }
        return targetTile;
    }

}