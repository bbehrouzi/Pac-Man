package game;

import logic.Map;

public record Scene(Map map, Controller controller) {

    public void update() {
        map.draw();
        controller.draw();
    }

}