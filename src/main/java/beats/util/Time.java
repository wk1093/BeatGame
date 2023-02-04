package beats.util;

import beats.Loggable;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time implements Loggable {
    public static float getTime() { return (float)(glfwGetTime()); }
    public static float getSysTime() { return (float)(System.currentTimeMillis()); }
}
