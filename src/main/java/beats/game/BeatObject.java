package beats.game;

import beats.ecs.GameObject;
import beats.ecs.Transform;
import beats.ecs.components.BeatRenderer;
import beats.renderer.Texture;
import beats.util.AssetPool;
import beats.game.BeatType;

public class BeatObject extends GameObject {
    private BeatType type;
    private double time;

    public BeatObject(String name, Transform transform, BeatType type, double time) {
        super(name, transform);
        this.type = type;
        this.time = time;
        addComponent(new BeatRenderer());
    }

    public Texture getTexture() {
        switch (type) {
            case up:
                return AssetPool.getTexture("assets/images/up.png");
            case down:
                return AssetPool.getTexture("assets/images/down.png");
            case left:
                return AssetPool.getTexture("assets/images/left.png");
            case right:
                return AssetPool.getTexture("assets/images/right.png");
            case click:
                return AssetPool.getTexture("assets/images/click.png");
            case set:
                return null;
        }
        return null;
    }


}
