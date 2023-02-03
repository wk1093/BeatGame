package beats;

import beats.ecs.GameObject;
import beats.renderer.Camera;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene() {
    }

    public void init() {
        this.camera = new Camera(new Vector2f());
    }

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        gameObjects.add(go);
        if (isRunning) {
            go.start();
        }
    }

    public abstract void update(float dt);

    public Camera camera() {
        return camera;
    }

}
