package beats;

import beats.input.KeyListener;
import beats.input.MouseListener;
import beats.scenes.InGameScene;
import beats.scenes.LevelSelectScene;
import beats.scenes.MainMenuScene;
import beats.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements Loggable {
    private int width, height;
    private String title;
    private long glfwWindow;

    private static Window window = null;

    private static Scene currentScene;

    private float clearR, clearG, clearB, clearA;

    private Window() {
        this.width = 1280;
        this.height = 720;
        this.title = "BeatGame";
        this.clearR = 1.0f;
        this.clearG = 1.0f;
        this.clearB = 1.0f;
        this.clearA = 1.0f;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new MainMenuScene();
                break;
            case 1:
                currentScene = new LevelSelectScene();
                break;
            case 2:
                currentScene = new InGameScene();
                break;
            default:
                LOGGER.severe("Invalid scene number");
                throw new IllegalArgumentException("Invalid scene number");
        }
        currentScene.init();
        currentScene.start();
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
            LOGGER.fine("Creating Window");
        }
        return Window.window;
    }

    public static Scene getScene() {
        return get().currentScene;
    }

    public void run() {
        LOGGER.info("LWJGL " + Version.getVersion());

        init();
        loop();

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        LOGGER.fine("Initializing window");
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW!");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW window!");
        }
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        Window.changeScene(2); // TODO: Set to 0
    }

    public void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();

            if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && KeyListener.isKeyPressed(GLFW_KEY_C)) {
                glfwSetWindowShouldClose(glfwWindow, true);
            }

            glClearColor(clearR, clearG, clearB, clearA);
            glClear(GL_COLOR_BUFFER_BIT);
            
            if (dt >= 0)
                currentScene.update(dt);

            glfwSwapBuffers(glfwWindow);
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public float getR() {
        return clearR;
    }

    public void setR(float clearR) {
        this.clearR = clearR;
    }

    public float getG() {
        return clearG;
    }

    public void setG(float clearG) {
        this.clearG = clearG;
    }

    public float getB() {
        return clearB;
    }

    public void setB(float clearB) {
        this.clearB = clearB;
    }

    public float getA() {
        return clearA;
    }

    public void setA(float clearA) {
        this.clearA = clearA;
    }
}
