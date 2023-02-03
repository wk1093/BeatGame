package beats.ecs.components;

import beats.Loggable;
import beats.game.BeatObject;
import org.joml.Vector4f;

public class BeatRenderer extends SpriteRenderer implements Loggable {

    public BeatRenderer() {
        super(new Vector4f(1, 1, 1, 1));
    }

    @Override
    public void start() {
        super.start();
        if (gameObject instanceof BeatObject) {
            BeatObject beatObject = (BeatObject) gameObject;
            this.texture = beatObject.getTexture();
        } else {
            this.texture = null;
            LOGGER.warning("BeatRenderer is not attached to a BeatObject");
        }

    }
}
