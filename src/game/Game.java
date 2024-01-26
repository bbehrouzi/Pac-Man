/*
@author Benjamin Behrouzi, Oliver Kamrath
@version 1.0
 */

package game;

import logic.Map;

public class Game {

    private final Map map = new Map();
    private final Controller controller = new Controller(map);
    private final Scene scene = new Scene(map, controller);
    public static int pellets = 244;
    public static int life = 3;
    public static int score = 0;

    public static void main(String[] args) {
        Window window = Window.get();
        window.run();
    }

    public void update() {
        controller.update();
        scene.update();
        System.out.println(score);
    }

}