package beats;

import beats.input.KeyListener;
import beats.input.MouseListener;
import beats.scenes.InGameScene;
import beats.scenes.LevelSelectScene;
import beats.scenes.MainMenuScene;
import beats.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements Loggable {
    public static String[] levels;
    private int width, height;
    private String title;
    private long glfwWindow;
    private ImGuiLayer imguiLayer;

    public static String level = "";

    private static Window window = null;

    private long audioContext;
    private long audioDevice;

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

        List<String> lvlList = new ArrayList<>();
        File folder = new File("assets/levels");
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                lvlList.add(file.getName().replace(".json", ""));
            }
        }
        levels = lvlList.toArray(new String[0]);
        System.out.println(Arrays.toString(levels));
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

        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);


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

        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
        if (!alCapabilities.OpenAL10) {
            throw new IllegalStateException("OpenAL 1.0 not supported!");
        }

        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        imguiLayer = new ImGuiLayer(glfwWindow);
        imguiLayer.initImGui();

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
