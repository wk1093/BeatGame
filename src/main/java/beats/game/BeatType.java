package beats.game;

import beats.input.KeyListener;
import beats.input.MouseListener;

import static org.lwjgl.glfw.GLFW.*;

public enum BeatType {
    up(0, "up"),
    left(1, "left"),
    right(2, "right"),
    down(3, "down"),
    click(4, "click"),
    set(5, "set");

    private final int id;
    private final String jsonval;

    private BeatType(int id, String jsonval) {
        this.id = id;
        this.jsonval = jsonval;

    }

    public String toString() {
        return jsonval;
    }

    public static int getID(String json) {
        for (BeatType t : BeatType.values()) {
            if (t.jsonval.equals(json)) return t.id;
        }
        return -1;
    }

    public static BeatType getType(int id) {
        for (BeatType t : BeatType.values()) {
            if (t.id == id) return t;
        }
        return null;
    }

    public boolean pressed() {
        switch (this) {
            case up:
                return KeyListener.isKeyPressed(GLFW_KEY_W) || KeyListener.isKeyPressed(GLFW_KEY_UP);
            case down:
                return KeyListener.isKeyPressed(GLFW_KEY_S) || KeyListener.isKeyPressed(GLFW_KEY_DOWN);
            case left:
                return KeyListener.isKeyPressed(GLFW_KEY_A) || KeyListener.isKeyPressed(GLFW_KEY_LEFT);
            case right:
                return KeyListener.isKeyPressed(GLFW_KEY_D) || KeyListener.isKeyPressed(GLFW_KEY_RIGHT);
            case click:
                return MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT);
            default:

                return false;
        }
    }
}
