package beats.input;

import beats.Loggable;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener implements Loggable {
    private static KeyListener instance = null;
    private boolean[] keyPressed = new boolean[350];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (instance == null) {
            instance = new KeyListener();
            LOGGER.fine("Creating KeyListener");
        }
        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key > get().keyPressed.length) {
            LOGGER.warning("Key " + key + " is not supported");
            return;
        }
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int key) {
        if (key > get().keyPressed.length) {
            LOGGER.warning("Key " + key + " is not supported");
            return false;
        }
        return get().keyPressed[key];
    }


}
