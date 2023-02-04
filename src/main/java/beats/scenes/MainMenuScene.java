package beats.scenes;

import beats.Scene;
import beats.Window;
import imgui.ImGui;
import beats.ImGuiLayer;

public class MainMenuScene extends Scene {

    public MainMenuScene() {
        super();
    }

    public void update(float dt) {

    }

    @Override
    public void imgui() {
        ImGuiLayer.beginFullscreen("Main Menu");
        ImGuiLayer.CenteredText("BeatGame", -200);
        if (ImGuiLayer.CenteredButton("Select Level", -100)) {
            Window.changeScene(1);
        }
        if (ImGuiLayer.CenteredButton("Exit", -50)) {
            Window.exit();
        }
        ImGui.end();
    }
}
