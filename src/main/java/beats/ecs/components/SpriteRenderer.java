package beats.ecs.components;

import beats.ecs.Component;
import beats.ecs.Transform;
import beats.renderer.Sprite;
import beats.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color;
    private Sprite sprite;

    private Transform lastTransform;
    private boolean isDirty = false;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
        isDirty = true;
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1, 1, 1, 1);
        isDirty = true;
    }

    @Override
    public void start() {
        super.start();
        lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if (!lastTransform.equals(gameObject.transform)) {
            gameObject.transform.copy(lastTransform);
            isDirty = true;
        }
    }

    public Vector4f getColor() {
        return color;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }

    public void setSprite(Sprite sprite) {
        if (sprite == null) return;
        if (this.sprite == sprite) return;
        sprite = sprite;
        isDirty = true;
    }

    public void setColor(Vector4f color) {
        if (color == null) return;
        if (this.color.equals(color)) return;
        this.color.set(color);
        isDirty = true;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setClean() {
        isDirty = false;
    }
}
