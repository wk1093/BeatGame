package beats.scenes;

import beats.Scene;
import beats.ecs.GameObject;
import beats.ecs.Transform;
import beats.ecs.components.SpriteRenderer;
import beats.game.BeatType;
import beats.renderer.Camera;
import beats.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class InGameScene extends Scene {



    public InGameScene() {
        super();
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        GameObject obj1 = new GameObject("obj1", new Transform(new Vector2f(100, 100), new Vector2f(200, 200)));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/test.jpg")));
        this.gameObjects.add(obj1);

        GameObject obj2 = new GameObject("obj2", new Transform(new Vector2f(300, 300), new Vector2f(200, 200)));
        obj2.addComponent(new SpriteRenderer(new Vector4f(1, 0, 0, 1)));
        this.gameObjects.add(obj2);


        loadResources();
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
    }



    public void update(float dt) {
        System.out.print("\rFPS:"+(1.0f/dt));

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();

    }
}
