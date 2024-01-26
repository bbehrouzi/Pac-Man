package game;

import logic.*;

// This class is the central hub for all changing variables in the actors
public class Controller {

    private final Ghost[] ghosts = new Ghost[4];
    private final PacMan pac;
    private final Map map;
    private final int[] duration = {7, 20, 7, 20, 5, 20, 5}; // The amount of seconds the ghost stay in scatter and chase
    private int bracket = 0; // The current mode and also the index in duration
    private int modeTimer = 0; // Counts for how long a ghost is already in chase or scatter
    private int lastMode = -1;
    private int killsInSuccession = 0; // The amount of ghosts Pac-Man killed with one power-up
    private int exitTimer = 0; // Counts for how much time passed since Pac-Man ate a pellet
    private int pFreeze, gFreeze; // Will make Pac-Man (and ghosts) freeze when he eats something
    private int lastGhostKilled = -1;
    private boolean power; // Pac-Man power-up
    private int pTimer; // Power-up timer
    private int fTimer; // Fruit timer
    private boolean fruit; //

    public Controller(Map map) {
        this.map = map;
        pac = new PacMan(map);
        Mode mode = new Mode(map);
        ghosts[0] = new Blinky(map, pac, mode);
        ghosts[1] = new Pinky(map, pac, mode);
        ghosts[2] = new Inky(map, pac, mode, (Blinky) ghosts[0]);
        ghosts[3] = new Clyde(map, pac, mode);
    }

    public void update() {
        move();
        if (!pac.teleport()) {
            eat();
        }
        changeMode();
        if (Game.pellets <= 20 && !ghosts[3].roam) {
            ((Blinky)ghosts[0]).setElroy(1);
            if(Game.pellets <= 10){
                ((Blinky)ghosts[0]).setElroy(2);
            }
        } else {
            ((Blinky)ghosts[0]).setElroy(0);
        }
    }

    public void draw() {
        for (int i = 3; i >= 0; i--) {
            ghosts[i].draw();
        }
        pac.draw();
    }

    private void move() {
        int speed = 17;
        // PacMan
        if (pFreeze == 0) {
            for (int i = 0; i < speed; i++) {
                if (!pacDead()) {
                    pac.move();
                } else {
                    Game.life--;
                }
            }
        } else {
            if (pacDead()) {
                Game.life--;
            }
            pFreeze--;
        }
        // Ghosts
        if (exitTimer == 4*60) {
            forceExit();
            exitTimer = 0;
        }
        for (int i = 0; i < ghosts.length; i++) {
            if (ghosts[i].getMode() != 2) {
                speed = 16;
            } else {
                speed = 9;
            }
            if (ghosts[i].inTunnel()) {
                speed = 7;
            }
            if (i == 0) {
                if (((Blinky)ghosts[i]).getElroy() == 1 ) {
                    speed = 17;
                }
                if (((Blinky)ghosts[i]).getElroy() == 2) {
                    speed = 18;
                }
            }
            if (gFreeze == 0 || (ghosts[i].getMode() == 3 && i != lastGhostKilled)) {
                for (int j = 0; j < speed; j++) {
                    ghosts[i].move();
                    ghosts[i].prison(lastMode);
                }
            }
        }
        if(gFreeze != 0){
            gFreeze--;
        } else {
            lastGhostKilled = -1;
        }
        exitTimer++;
    }

    private int collision() {
        int[] pTile = pac.getTile();
        int[] gTile;
        for (int i = 0; i < ghosts.length; i++) {
            gTile = ghosts[i].getTile();
            if (pTile[0] == gTile[0] && pTile[1] == gTile[1] && ghosts[i].getMode() != 3) {
                return i+1;
            }
        }
        return 0;
    }

    private boolean pacDead() {
        int num = collision();
        if (num > 0 && ghosts[num-1].getMode() != 2) {
            bracket = 0;
            modeTimer = 0;
            exitTimer = 0;
            for (Ghost ghost : ghosts) {
                ghost.init();
            }
            ghosts[1].setThreshold(7);
            ghosts[2].setThreshold(17);
            ghosts[3].setThreshold(32);
            pac.init();
            return true;
        }
        return false;
    }

    private void killGhost() {
        int[] ghostScore = {200, 400, 800, 1600};
        for (int i = 0; i < ghosts.length; i++) {
            if (collision() == i+1 && ghosts[i].mode == 2) {
                pFreeze = 30;
                gFreeze = 30;
                ghosts[i].mode = 3;
                ghosts[i].setColor(1.0f, 1.0f, 1.0f);
                Game.score = ghostScore[killsInSuccession];
                killsInSuccession++;
                lastGhostKilled = i;
            }
        }
    }

    private void forceExit() {
        for (int i = 1; i < ghosts.length; i++) {
            if (ghosts[i].roam) {
                ghosts[i].setTimeElapsed();
                break;
            }
        }
    }

    private void eat() {
        int[] tile = pac.getTile();
        pellets(tile);
        powerUp(tile);
        fruit(tile);
        killGhost();
    }

    private void pellets(int[] tile) {
        if (map.checkTile(tile[0], tile[1]) == 2) {
            map.changeTile(tile[0], tile[1], 0);
            Game.score += 10;
            Game.pellets--;
            pFreeze = 1;
            exitTimer = 0;
        }
    }

    private void powerUp(int[] tile) {
        if (map.checkTile(tile[0], tile[1]) == 3) {
            map.changeTile(tile[0], tile[1], 0);
            Game.pellets--;
            power = true;
            pTimer = 0;
            exitTimer = 0;
            for (Ghost ghost : ghosts) {
                if (ghost.getMode() != 3) {
                    if (ghost.getMode() != 2) {
                        ghost.spin();
                    }
                    ghost.setMode(2); // Go into frightened
                    ghost.setColor(0.0f, 0.0f, 0.7f);
                }
            }
            modeTimer = 0; // Resets the time bracket for switchMode
            killsInSuccession = 0;
        }
        if (power && pTimer < 20*60) {
            pTimer++;
        }
        if (pTimer == 20*60) {
            power = false;
            pTimer = 0;
            for (Ghost ghost : ghosts) {
                if (ghost.getMode() == 2) {
                    ghost.setMode(lastMode);
                    ghost.setColor(ghost.oColor[0], ghost.oColor[1], ghost.oColor[2]);
                }
            }
        }
    }

    private void fruit(int[] tile){
        if (Game.pellets == 174) {
            fTimer = 540 + (int) (Math.random()*61);
            map.addFruit();
            fruit = true;
        }

        if (Game.pellets == 74) {
            fTimer = 540 + (int) (Math.random()*61);
            map.addFruit();
            fruit = true;
        }

        if (map.checkTile(tile[0], tile[1]) == 4) {
            map.changeTile(tile[0], tile[1], 0);
            Game.score += 100;
            pFreeze = 1;
            fruit = false;
        }
        if (fruit) {
            fTimer--;
        }
        if (fTimer <= 0 || pacDead()) {
            map.changeTile(13, 15, 0);
            fruit = false;
        }
    }

    private void changeMode() {
        if (bracket < 7 && !power) {
            if (modeTimer == duration[bracket]*60) {
                bracket++;
                for (Ghost ghost : ghosts) {
                    if (ghost.getMode() != 3) {
                        ghost.spin();
                        ghost.setMode(bracket%2);
                    }
                }
                modeTimer = 0;
            }
            lastMode = bracket%2;
            modeTimer++;
        }
    }

}