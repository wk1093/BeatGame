package beats.ecs.components;

import beats.ecs.Component;

public class FontRenderer extends Component {
    @Override
    public void start() {
        super.start();

        if (gameObject.hasComponent(FontRenderer.class)) {
            System.out.println("FontRenderer already exists on this GameObject!");
        }
    }

    @Override
    public void update(float dt) {

    }
}
