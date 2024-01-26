package util;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {

    private static Keyboard instance;
    private final boolean[] keyPressed = new boolean[350]; // Stores the possible key inputs

    private Keyboard() {

    }

    public static Keyboard get() {
        if (Keyboard.instance == null) {
            Keyboard.instance = new Keyboard();
        }
        return Keyboard.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true; // Sets a specific key true so it can be called by isKeyPressed
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    // Returns if a specific key is currently pressed
    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }

}