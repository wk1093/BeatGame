package beats.scenes;

import beats.Scene;
import beats.ecs.GameObject;
import beats.ecs.Transform;
import beats.ecs.components.SpriteRenderer;
import beats.game.BeatType;
import beats.renderer.Camera;
import beats.renderer.Sprite;
import beats.renderer.SpriteSheet;
import beats.util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class InGameScene extends Scene {



    public InGameScene() {
        super();
    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f());

        SpriteSheet sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");
        assert sprites != null;
//
        GameObject obj1 = new GameObject("obj1", new Transform(new Vector2f(100, 100), new Vector2f(200, 200)));
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.gameObjects.add(obj1);

        GameObject obj2 = new GameObject("obj2", new Transform(new Vector2f(300, 100), new Vector2f(200, 200)));
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(3)));
        this.gameObjects.add(obj2);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png", new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"), 32, 32, 6, 0));
    }



    public void update(float dt) {
        System.out.print("\rFPS:"+(1.0f/dt));

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();

    }
}
