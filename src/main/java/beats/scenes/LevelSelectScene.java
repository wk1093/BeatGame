package beats.scenes;

import beats.ImGuiLayer;
import beats.Loggable;
import beats.Scene;
import beats.Window;
import beats.util.FileUtils;
import imgui.ImGui;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LevelSelectScene extends Scene {
    public LevelSelectScene() {
        super();
    }

    public void update(float dt) {

    }

    @Override
    public void imgui() {
        ImGuiLayer.beginFullscreen("Select Level");
        ImGuiLayer.CenteredText("Level Select", -240);
//        for (int i = 0; i < 10; i++) {
//            if (ImGuiLayer.CenteredButton("Level " + (i + 1), -100 + i * 50)) {
//                Window.level = "level" + (i + 1);
//                Window.changeScene(2);
//            }
//        }
        // for each json file in the levels folder
        // add the filename to levels without path and extension
        // add the 'name' field from the json file to names
//        String[] levels = {"level1", "level2", "level3"};
//        String[] names = {"Level 1", "Level 2", "Level 3"};
        List<String> levels = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (String level : Window.levels) {
            levels.add(level);
            // open json file
            // get name field
            // add name to names
            String path = "assets/levels/" + level + ".json";
            String src = FileUtils.readFile(path);
            JSONObject json = new JSONObject(src);
            names.add(json.getString("name"));

        }
        ImGuiLayer.CenterBegin(200, 400, 0, 0);
        ImGui.beginChild("Levels", 200, 400, true);
        ImGuiLayer.CenterEnd();
        for (int i = 0; i < levels.size(); i++) {
            if (ImGui.button(names.get(i))) {
                Window.level = levels.get(i);
                Window.changeScene(2);
            }
        }
        ImGui.endChild();

        if (ImGuiLayer.CenteredButton("Back", 200)) {
            Window.changeScene(0);
        }
        ImGui.end();
    }
}
