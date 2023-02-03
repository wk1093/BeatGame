package beats.scenes;

import beats.Loggable;
import beats.Scene;

public class LevelSelectScene extends Scene implements Loggable {
    public LevelSelectScene() {
        super();
    }

    public void update(float dt) {
        LOGGER.warning("Skipping LevelSelectScene");
    }
}
