package beats.scenes;

import beats.Scene;
import beats.ecs.GameObject;
import beats.ecs.Transform;
import beats.ecs.components.SpriteRenderer;
import beats.renderer.Camera;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class InGameScene extends Scene {



    public InGameScene() {
        super();
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(600 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                float xPos = xOffset + (i * sizeX);
                float yPos = yOffset + (j * sizeY);

                GameObject go = new GameObject("Obj"+i+"_"+j, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
                this.addGameObjectToScene(go);
            }
        }
    }



    public void update(float dt) {
        System.out.print("\rFPS:"+(1.0f/dt));

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();

    }
}
