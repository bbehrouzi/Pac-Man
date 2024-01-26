package logic;

import util.Keyboard;

import static org.lwjgl.glfw.GLFW.*;

public class PacMan extends Actor {

    private int moveX, moveY;
    private int oldMove = -1;
    private boolean corner;
    private boolean input;

    public PacMan(Map map) {
        super(map);
        init();
    }

    public void init() {
        startX = 13*factor+(factor/2);
        startY = 9*factor;
        colors[0] = 1.0f;
        colors[1] = 1.0f;
        colors[2] = 0.0f;
        super.init();
    }

    @Override
    public void move() {
        // Lose control when corner is in wall
        if (!teleport()) {
            input = map.checkGrid(pos[0][0], pos[0][1]) != 1 && map.checkGrid(pos[1][0], pos[1][1]) != 1 && map.checkGrid(pos[2][0], pos[2][1]) != 1 && map.checkGrid(pos[3][0], pos[3][1]) != 1;
        }
        // User input
        if (input && !teleport()) {
            if (!(Keyboard.isKeyPressed(GLFW_KEY_W) && Keyboard.isKeyPressed(GLFW_KEY_S))) {
                if (Keyboard.isKeyPressed(GLFW_KEY_W)) {
                    moveY = 1;
                }
                if (Keyboard.isKeyPressed(GLFW_KEY_S)) {
                    moveY = -1;
                }
            }
            if (!(Keyboard.isKeyPressed(GLFW_KEY_A) && Keyboard.isKeyPressed(GLFW_KEY_D))) {
                if (Keyboard.isKeyPressed(GLFW_KEY_A)) {
                    moveX = -1;
                }
                if (Keyboard.isKeyPressed(GLFW_KEY_D)) {
                    moveX = 1;
                }
            }
        }
        if (!teleport()) {
            cornering();
        }
        if (!corner) {
            moveStraight();
        }
    }

    private void moveStraight() {
        // Vertically
        if (moveY == 1 && !teleport() && map.canGoUp(pos[2][0]+factor/2, pos[2][1]) && map.canGoUp(pos[2][0]+(factor/2)-1, pos[2][1])) {
            changePos(0);
            direction = 0;
        }
        else if (moveY == -1 && !teleport() && map.canGoDown(pos[0][0]+factor/2, pos[0][1]) && map.canGoDown(pos[0][0]+(factor/2)-1, pos[0][1])) {
            changePos(1);
            direction = 1;
        } else {
            moveY = 0;
        }
        // Horizontally
        if (moveX == -1 && (teleport() || (map.canGoLeft(pos[0][0], pos[0][1]+factor/2)) && map.canGoLeft(pos[0][0], pos[0][1]+(factor/2)-1))) {
            changePos(2);
            direction = 2;
        }
        else if (moveX == 1 && (teleport() || (map.canGoRight(pos[1][0], pos[1][1]+factor/2)) && map.canGoRight(pos[1][0], pos[1][1]+(factor/2)-1))) {
            changePos(3);
            direction = 3;
        } else {
            moveX = 0;
        }
        // Get the last move before cornering
        if (moveX == 0 || moveY == 0) {
            if (moveX != 0) {
                oldMove = 0;
            } else if (moveY != 0) {
                oldMove = 1;
            }
        }

    }

    private void moveCorner(int move) {
        // Vertically
        if (move == 0 && !teleport() && map.canGoUp(pos[2][0]+factor/2, pos[2][1]) && map.canGoUp(pos[2][0]+(factor/2)-1, pos[2][1])) {
            changePos(0);
        }
        else if (move == 1 && !teleport() && map.canGoDown(pos[0][0]+factor/2, pos[0][1]) && map.canGoDown(pos[0][0]+(factor/2)-1, pos[0][1])) {
            changePos(1);
        }
        // Horizontally
        if (move == 2 && (teleport() || (map.canGoLeft(pos[0][0], pos[0][1]+factor/2)) && map.canGoLeft(pos[0][0], pos[0][1]+(factor/2)-1))) {
            changePos(2);
        }
        else if (move == 3 && (teleport() || (map.canGoRight(pos[1][0], pos[1][1]+factor/2)) && map.canGoRight(pos[1][0], pos[1][1]+(factor/2)-1))) {
            changePos(3);
        }
    }

    private void cornering() {
        corner = false;
        // Upper Left corner
        if (map.checkGrid(pos[2][0], pos[2][1]) == 1) {
            // Up and down
            if (oldMove == 1) {
                // Down then left
                if (moveX == -1 && moveY == -1) {
                    corner = true;
                    this.moveCorner(2);
                    this.moveCorner(1);
                    direction = 2;
                }
                // Up then left
                if (moveX == -1 && moveY == 1) {
                    corner = true;
                    this.moveCorner(2);
                    this.moveCorner(1);
                    direction = 2;
                }
            }
            // Left and Right
            if (oldMove == 0) {
                // Right then up
                if (moveX == 1 && moveY == 1) {
                    corner = true;
                    this.moveCorner(3);
                    this.moveCorner(0);
                    direction = 0;
                }
                // Left then up
                if (moveX == -1 && moveY == 1) {
                    corner = true;
                    this.moveCorner(3);
                    this.moveCorner(0);
                    direction = 0;
                }
            }
        }
        // Upper right corner
        if (map.checkGrid(pos[3][0], pos[3][1]) == 1) {
            // Up & down
            if (oldMove == 1) {
                // Down then right
                if (moveX == 1 && moveY == -1) {
                    corner = true;
                    this.moveCorner(3);
                    this.moveCorner(1);
                    direction = 3;
                }
                // Up then right
                if (moveX == 1 && moveY == 1) {
                    corner = true;
                    this.moveCorner(3);
                    this.moveCorner(1);
                    direction = 3;
                }
            }
            // Left and Right
            if (oldMove == 0) {
                // Right then up
                if (moveX == 1 && moveY == 1) {
                    corner = true;
                    this.moveCorner(2);
                    this.moveCorner(0);
                    direction = 0;
                }
                // Left then up
                if (moveX == -1 && moveY == 1) {
                    corner = true;
                    this.moveCorner(2);
                    this.moveCorner(0);
                    direction = 0;
                }
            }
        }
        // Lower left corner
        if (map.checkGrid(pos[0][0], pos[0][1]) == 1) {
            // Up & down
            if (oldMove == 1) {
                // Down then left
                if (moveX == -1 && moveY == -1) {
                    corner = true;
                    this.moveCorner(2);
                    this.moveCorner(0);
                    direction = 2;
                }
                // Up then left
                if (moveX == -1 && moveY == 1) {
                    corner = true;
                    this.moveCorner(2);
                    this.moveCorner(0);
                    direction = 2;
                }
            }
            // Left and Right
            if (oldMove == 0) {
                // Right then down
                if (moveX == 1 && moveY == -1) {
                    corner = true;
                    this.moveCorner(3);
                    this.moveCorner(1);
                    direction = 1;
                }
                // Left then down
                if (moveX == -1 && moveY == -1) {
                    corner = true;
                    this.moveCorner(3);
                    this.moveCorner(1);
                    direction = 1;
                }
            }
        }
        // Lower right corner
        if (map.checkGrid(pos[1][0], pos[1][1]) == 1) {
            // Up & down
            if (oldMove == 1) {
                // Down then right
                if (moveX == 1 && moveY == -1) {
                    corner = true;
                    this.moveCorner(3);
                    this.moveCorner(0);
                    direction = 3;
                }
                // Up then right
                if (moveX == 1 && moveY == 1) {
                    corner = true;
                    this.moveCorner(3);
                    this.moveCorner(0);
                    direction = 3;
                }
            }
            // Left and Right
            if (oldMove == 0) {
                // Right then down
                if (moveX == 1 && moveY == -1) {
                    corner = true;
                    this.moveCorner(2);
                    this.moveCorner(1);
                    direction = 1;
                }
                // Left then down
                if (moveX == -1 && moveY == -1) {
                    corner = true;
                    this.moveCorner(2);
                    this.moveCorner(1);
                    direction = 1;
                }
            }
        }
        if (!corner) {
            if (moveX == -1) {
                if (map.checkGrid(pos[2][0], pos[2][1]) == 1) {
                    corner = true;
                    this.moveCorner(2);
                    this.moveCorner(1);
                }
                if (map.checkGrid(pos[0][0], pos[0][1]) == 1) {
                    corner = true;
                    this.moveCorner(2);
                    this.moveCorner(0);
                }
                direction = 2;
            }
            if (moveX == 1) {
                if (map.checkGrid(pos[1][0], pos[1][1]) == 1) {
                    corner = true;
                    this.moveCorner(3);
                    this.moveCorner(1);
                }
                if (map.checkGrid(pos[3][0], pos[2][1]) == 1) {
                    corner = true;
                    this.moveCorner(3);
                    this.moveCorner(0);
                }
                direction = 3;
            }
            if (moveY == -1) {
                if (map.checkGrid(pos[0][0], pos[0][1]) == 1) {
                    corner = true;
                    this.moveCorner(3);
                    this.moveCorner(1);
                }
                if (map.checkGrid(pos[1][0], pos[1][1]) == 1) {
                    corner = true;
                    this.moveCorner(2);
                    this.moveCorner(1);

                }
                direction = 1;
            }

            if (moveY == 1) {
                if (map.checkGrid(pos[2][0], pos[2][1]) == 1) {
                    corner = true;
                    this.moveCorner(3);
                    this.moveCorner(0);
                }
                if (map.checkGrid(pos[3][0], pos[3][1]) == 1) {
                    corner = true;
                    this.moveCorner(2);
                    this.moveCorner(0);
                }
                direction = 0;
            }
        }
    }

    protected void changePos(int value) {
        super.changePos(value);
        direction = value;
    }
    
    public int getDirection() {
        return direction;
    }

}