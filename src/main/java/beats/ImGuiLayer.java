package beats;

import beats.input.KeyListener;
import beats.input.MouseListener;
import imgui.*;
import imgui.callback.*;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import static org.lwjgl.glfw.GLFW.*;

public class ImGuiLayer {
    private long glfwWindow;
    private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();

    public ImGuiLayer(long glfwWindow) {
        this.glfwWindow = glfwWindow;
    }

    public static void beginFullscreen(String menu) {
        ImGui.setNextWindowPos(0, 0);
        ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight());
        ImGui.begin(menu, ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoNav | ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoBringToFrontOnFocus);
    }

    public static boolean CenteredButton(String text, float yOff) {
        // only for fullscreen windows
        // do the same as ImGui::Button but center it
        float windowWidth = Window.getWidth();
        ImVec2 dst = new ImVec2();
        ImGui.calcTextSize(dst, text);
        ImVec2 dst2 = new ImVec2();
        ImGui.getStyle().getFramePadding(dst2);
        float buttonWidth = dst.x + dst2.x * 2;
        float buttonX = (windowWidth - buttonWidth) / 2;
        // center x and y, then add yOff
        ImVec2 oldPos = new ImVec2();
        ImGui.getCursorPos(oldPos);
        ImGui.setCursorPos(buttonX, Window.getHeight() / 2.0f + yOff);
        boolean result = ImGui.button(text);
        ImGui.setCursorPos(oldPos.x, oldPos.y);
        return result;
    }

    public static boolean CenteredText(String text, float yOff) {
        // only for fullscreen windows
        // do the same as ImGui::Text but center it
        float windowWidth = Window.getWidth();
        ImVec2 dst = new ImVec2();
        ImGui.calcTextSize(dst, text);
        float textWidth = dst.x;
        float textX = (windowWidth - textWidth) / 2;
        // center x and y, then add yOff
        ImVec2 oldPos = new ImVec2();
        ImGui.getCursorPos(oldPos);
        ImGui.setCursorPos(textX, Window.getHeight() / 2.0f + yOff);
        ImGui.text(text);
        ImGui.setCursorPos(oldPos.x, oldPos.y);
        return true;
    }

    public static ImVec2 lastPos = new ImVec2();

    public static void CenterBegin(int w, int h, int xoff, int yoff) {
        // only one at a time
        // stores the cursor position in lastPos
        // sets the cursor to the center of the screen (for an object of w,h) but offset by xoff and yoff
        ImGui.getCursorPos(lastPos);
        ImGui.setCursorPos(Window.getWidth() / 2.0f - w / 2.0f + xoff, Window.getHeight() / 2.0f - h / 2.0f + yoff);
    }

    public static void CenterEnd() {
        // sets the cursor to the position stored in lastPos
        ImGui.setCursorPos(lastPos.x, lastPos.y);
    }

    public void initImGui() {
        // IMPORTANT!!
        // This line is critical for Dear ImGui to work.
        ImGui.createContext();

        // ------------------------------------------------------------
        // Initialize ImGuiIO config
        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename("assets/imgui.ini"); // We don't want to save .ini file
        io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navigation with keyboard
        io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Mouse cursors to display while resizing windows etc.
        io.setBackendPlatformName("imgui_java_impl_glfw");

//        // ------------------------------------------------------------
//        // Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
//        final int[] keyMap = new int[ImGuiKey.COUNT];
//        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
//        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
//        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
//        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
//        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
//        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
//        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
//        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
//        keyMap[ImGuiKey.End] = GLFW_KEY_END;
//        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
//        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
//        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
//        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
//        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
//        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
//        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
//        keyMap[ImGuiKey.A] = GLFW_KEY_A;
//        keyMap[ImGuiKey.C] = GLFW_KEY_C;
//        keyMap[ImGuiKey.V] = GLFW_KEY_V;
//        keyMap[ImGuiKey.X] = GLFW_KEY_X;
//        keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
//        keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
//        io.setKeyMap(keyMap);
//
//        // ------------------------------------------------------------
//        // Mouse cursors mapping
//        mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
//        mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
//        mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
//        mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
//        mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
//        mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
//        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
//        mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
//        mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
//
//        // ------------------------------------------------------------
//        // GLFW callbacks to handle user input
//
//        glfwSetKeyCallback(glfwWindow, (w, key, scancode, action, mods) -> {
//            if (action == GLFW_PRESS) {
//                io.setKeysDown(key, true);
//            } else if (action == GLFW_RELEASE) {
//                io.setKeysDown(key, false);
//            }
//
//            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
//            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
//            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
//            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
//        });
//
//        glfwSetCharCallback(glfwWindow, (w, c) -> {
//            if (c != GLFW_KEY_DELETE) {
//                io.addInputCharacter(c);
//            }
//        });
//
//        glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {
//            final boolean[] mouseDown = new boolean[5];
//
//            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
//            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
//            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
//            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
//            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;
//
//            io.setMouseDown(mouseDown);
//
//            if (!io.getWantCaptureMouse() && mouseDown[1]) {
//                ImGui.setWindowFocus(null);
//            }
//        });
//
//        glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) -> {
//            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
//            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
//        });

        glfwSetKeyCallback(glfwWindow, (w, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                io.setKeysDown(key, true);
            } else if (action == GLFW_RELEASE) {
                io.setKeysDown(key, false);
            }

            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));

            if (!io.getWantCaptureKeyboard()) {
                KeyListener.keyCallback(w, key, scancode, action, mods);
            }
        });

        glfwSetCharCallback(glfwWindow, (w, c) -> {
            if (c != GLFW_KEY_DELETE) {
                io.addInputCharacter(c);
            }
        });

        glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if (!io.getWantCaptureMouse() && mouseDown[1]) {
                ImGui.setWindowFocus(null);
            }


            MouseListener.mouseButtonCallback(w, button, action, mods);
        });

        glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) -> {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
            if (!io.getWantCaptureMouse()) {
                MouseListener.mouseScrollCallback(w, xOffset, yOffset);
            } else {
                MouseListener.mouseScrollCallback(w, 0, 0);
            }
        });

        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String s) {
                glfwSetClipboardString(glfwWindow, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                final String clipboardString = glfwGetClipboardString(glfwWindow);
                if (clipboardString != null) {
                    return clipboardString;
                } else {
                    return "";
                }
            }
        });

        // ------------------------------------------------------------
        // Fonts configuration
        // Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        // Glyphs could be added per-font as well as per config used globally like here
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());

        fontConfig.setPixelSnapH(true);
        fontAtlas.addFontFromFileTTF("assets/fonts/segoeui.ttf", 32, fontConfig);

        fontConfig.destroy(); // After all fonts were added we don't need this config more

        // ------------------------------------------------------------
        // Use freetype instead of stb_truetype to build a fonts texture

        // Style configuration

        final ImGuiStyle style = ImGui.getStyle();
        // found here: https://www.unknowncheats.me/forum/c-and-c-/189635-imgui-style-settings.html
        style.setWindowPadding(15, 15);
        style.setWindowRounding(5);
        style.setFramePadding(5, 5);
        style.setFrameRounding(4);
        style.setItemSpacing(12, 8);
        style.setItemInnerSpacing(8, 6);
        style.setIndentSpacing(25);
        style.setScrollbarSize(15);
        style.setScrollbarRounding(9);
        style.setGrabMinSize(5);
        style.setGrabRounding(3);

        style.setColor(ImGuiCol.Text, 0.80f, 0.80f, 0.83f, 1.00f);
        style.setColor(ImGuiCol.TextDisabled, 0.24f, 0.23f, 0.29f, 1.00f);
        style.setColor(ImGuiCol.WindowBg, 0.06f, 0.05f, 0.07f, 1.00f);
        style.setColor(ImGuiCol.ChildBg, 0.07f, 0.07f, 0.09f, 1.00f);
        style.setColor(ImGuiCol.PopupBg, 0.07f, 0.07f, 0.09f, 1.00f);
        style.setColor(ImGuiCol.Border, 0.80f, 0.80f, 0.83f, 0.88f);
        style.setColor(ImGuiCol.BorderShadow, 0.92f, 0.91f, 0.88f, 0.00f);
        style.setColor(ImGuiCol.FrameBg, 0.10f, 0.09f, 0.12f, 1.00f);
        style.setColor(ImGuiCol.FrameBgHovered, 0.24f, 0.23f, 0.29f, 1.00f);
        style.setColor(ImGuiCol.FrameBgActive, 0.56f, 0.56f, 0.58f, 1.00f);
        style.setColor(ImGuiCol.TitleBg, 0.10f, 0.09f, 0.12f, 1.00f);
        style.setColor(ImGuiCol.TitleBgCollapsed, 1.00f, 0.98f, 0.95f, 0.75f);
        style.setColor(ImGuiCol.TitleBgActive, 0.07f, 0.07f, 0.09f, 1.00f);
        style.setColor(ImGuiCol.MenuBarBg, 0.10f, 0.09f, 0.12f, 1.00f);
        style.setColor(ImGuiCol.ScrollbarBg, 0.10f, 0.09f, 0.12f, 1.00f);
        style.setColor(ImGuiCol.ScrollbarGrab, 0.80f, 0.80f, 0.83f, 0.31f);
        style.setColor(ImGuiCol.ScrollbarGrabHovered, 0.56f, 0.56f, 0.58f, 1.00f);
        style.setColor(ImGuiCol.ScrollbarGrabActive, 0.06f, 0.05f, 0.07f, 1.00f);
        style.setColor(ImGuiCol.CheckMark, 0.80f, 0.80f, 0.83f, 0.31f);
        style.setColor(ImGuiCol.SliderGrab, 0.80f, 0.80f, 0.83f, 0.31f);
        style.setColor(ImGuiCol.SliderGrabActive, 0.06f, 0.05f, 0.07f, 1.00f);
        style.setColor(ImGuiCol.Button, 0.10f, 0.09f, 0.12f, 1.00f);
        style.setColor(ImGuiCol.ButtonHovered, 0.24f, 0.23f, 0.29f, 1.00f);
        style.setColor(ImGuiCol.ButtonActive, 0.56f, 0.56f, 0.58f, 1.00f);
        style.setColor(ImGuiCol.Header, 0.10f, 0.09f, 0.12f, 1.00f);
        style.setColor(ImGuiCol.HeaderHovered, 0.56f, 0.56f, 0.58f, 1.00f);
        style.setColor(ImGuiCol.HeaderActive, 0.06f, 0.05f, 0.07f, 1.00f);
        style.setColor(ImGuiCol.ResizeGrip, 0.00f, 0.00f, 0.00f, 0.00f);
        style.setColor(ImGuiCol.ResizeGripHovered, 0.56f, 0.56f, 0.58f, 1.00f);
        style.setColor(ImGuiCol.ResizeGripActive, 0.06f, 0.05f, 0.07f, 1.00f);
        style.setColor(ImGuiCol.PlotLines, 0.40f, 0.39f, 0.38f, 0.63f);
        style.setColor(ImGuiCol.PlotLinesHovered, 0.25f, 1.00f, 0.00f, 1.00f);
        style.setColor(ImGuiCol.PlotHistogram, 0.40f, 0.39f, 0.38f, 0.63f);
        style.setColor(ImGuiCol.PlotHistogramHovered, 0.25f, 1.00f, 0.00f, 1.00f);
        style.setColor(ImGuiCol.TextSelectedBg, 0.25f, 1.00f, 0.00f, 0.43f);

        // Method initializes LWJGL3 renderer.
        // This method SHOULD be called after you've initialized your ImGui configuration (fonts and so on).
        // ImGui context should be created as well.
        imGuiGlfw.init(glfwWindow, false);
        imGuiGl3.init("#version 330 core");
    }

    public void update(float dt, Scene scene) {
        startFrame(dt);
        ImGui.newFrame();
        scene.imgui();
        ImGui.render();
        endFrame();
    }

    private void startFrame(final float deltaTime) {

        // Get window properties and mouse position
        final int[] winWidth = {Window.getWidth()};
        final int[] winHeight = {Window.getHeight()};
        double[] mousePosX = {0};
        double[] mousePosY = {0};
        glfwGetCursorPos(glfwWindow, mousePosX, mousePosY);

        // We SHOULD call those methods to update Dear ImGui state for the current frame
        final ImGuiIO io = ImGui.getIO();
        io.setDisplaySize(winWidth[0], winHeight[0]);
        io.setDisplayFramebufferScale(1f, 1f);
        io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
        io.setDeltaTime(deltaTime);

        // Update the mouse cursor
        final int imguiCursor = ImGui.getMouseCursor();
        glfwSetCursor(glfwWindow, mouseCursors[imguiCursor]);
        glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    private void endFrame() {
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        imGuiGl3.renderDrawData(ImGui.getDrawData());
        long backupWindowPtr = glfwGetCurrentContext();
        ImGui.updatePlatformWindows();
        ImGui.renderPlatformWindowsDefault();
        glfwMakeContextCurrent(backupWindowPtr);
    }

    // If you want to clean a room after yourself - do it by yourself
    private void destroyImGui() {
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }

}
