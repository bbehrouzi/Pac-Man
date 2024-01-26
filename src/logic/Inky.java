package logic;

public class Inky extends Ghost {

    private final Blinky blinky;

    public Inky(Map map, PacMan pac, Mode mode, Blinky blinky) {
        super(map, pac, mode);
        this.blinky = blinky;
        super.scatterTile[0] = 28;
        oColor[0] = 0.0f;
        oColor[1] = 1.0f;
        oColor[2] = 1.0f;
        this.init();
    }

    @Override
    public void init() {
        setColor(oColor[0], oColor[1], oColor[2]);
        super.startX = 11*factor+(factor/2);
        super.startY = 18*factor;
        up = true;
        roam = true;
        pelletThreshold = 30;
        super.init();
    }

    @Override
    protected void exit() {
        if (pos[0][0] != 13*factor+(factor/2)) {
            changePos(3);
        } else {
            changePos(0);
        }
    }

    @Override
    protected void enter() {
        if (pos[0][1] != startY) {
            changePos(1);
        } else if (pos[0][0] != startX) {
            changePos(2);
        }
    }

    @Override
    public int[] getChaseTile() {
        // Draw a line from 2 tiles ahead of Pac-Man to ghosts.Blinky's tile, then double the length
        int[] targetTile = super.pac.getTile();
        int[] blinkyTile = blinky.getTile();
        int dir = pac.getDirection();
        int disX, disY;
        if (dir == 0) {
            targetTile[1]+=2;
        }
        if (dir == 1) {
            targetTile[1]-=2;
        }
        if (dir == 2) {
            targetTile[0]-=2;
        }
        if (dir == 3) {
            targetTile[0]+=2;
        }
        disX = targetTile[0]-blinkyTile[0];
        disY = targetTile[1]-blinkyTile[1];
        targetTile[0]+=disX;
        targetTile[1]+=disY;
        return targetTile;
    }

}