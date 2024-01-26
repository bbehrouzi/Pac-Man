package logic;

// Orange ghost
public class Clyde extends Ghost {

    public Clyde(Map map, PacMan pac, Mode mode) {
        super(map, pac, mode);
        super.scatterTile[1] = 0;
        oColor[0] = 1.0f;
        oColor[1] = 0.65f;
        oColor[2] = 0.0f;
        this.init();
    }

    @Override
    public void init() {
        setColor(oColor[0], oColor[1], oColor[2]);
        super.startX = 15*factor+(factor/2);
        super.startY = 18*factor;
        up = true;
        roam = true;
        pelletThreshold = 90;
        super.init();
    }

    @Override
    public void exit() {
        if (pos[0][0] != 13*factor+(factor/2)) {
            changePos(2);
        }
        else {
            changePos(0);
        }
    }

    @Override
    public void enter() {
        if (pos[0][1] != startY) {
            changePos(1);
        } else if (pos[0][0] != startX) {
            changePos(3);
        }
    }

    /*
    Clyde will target Pac-Man directly until he less than 8 tiles away. Then his target will change
    to his scatter tile until his is 8 or more tiles away.
     */
    @Override
    public int[] getChaseTile() {
        int[] targetTile = pac.getTile();
        int dis, disX, disY;
        disX = targetTile[0]-getTile()[0];
        disY = targetTile[1]-getTile()[1];
        dis = (int)Math.sqrt((Math.pow(disX, 2)+Math.pow(disY, 2)));
        if (dis >= 8) {
            return pac.getTile();
        } else {
            return scatterTile;
        }
    }

}
