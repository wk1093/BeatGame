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
    public static String[] levels = {
            "test"
    };
    private int width, height;
    private String title;
    private long glfwWindow;
    private ImGuiLayer imguiLayer;

    public static String level = "";

    private static Window window = null;

    private static Scene currentScene;

    private float clearR, clearG, clearB, clearA;

    private Window() {
        this.width = 1280;
        this.height = 720;
        this.title = "BeatGame";
        this.clearR = 0.06f;
        this.clearG = 0.05f;
        this.clearB = 0.07f;
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
        glfwSetWindowSizeCallback(glfwWindow, (window, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        imguiLayer = new ImGuiLayer(glfwWindow);
        imguiLayer.initImGui();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        Window.changeScene(0); // TODO: Set to 0
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

            imguiLayer.update(dt, currentScene);
            glfwSwapBuffers(glfwWindow);
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public static void setClearColor(float r, float g, float b, float a) {
        get().clearR = r;
        get().clearG = g;
        get().clearB = b;
        get().clearA = a;
    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void setWidth(int width) {
        get().width = width;
    }

    public static void setHeight(int height) {
        get().height = height;
    }

    public static void exit() {
        glfwSetWindowShouldClose(get().glfwWindow, true);
    }

}
