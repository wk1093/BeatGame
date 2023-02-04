package beats.ecs;

import beats.Loggable;

import java.util.ArrayList;
import java.util.List;

public class GameObject implements Loggable {
    private String name;
    private List<Component> components;
    public Transform transform;
    private int zIndex;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        this.zIndex = 0;
    }

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.zIndex = zIndex;
        this.components = new ArrayList<>();
        this.transform = transform;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    throw new ClassCastException("Error casting component to " + componentClass.getName() + " in GameObject " + name);
                }
            }
        }
        return null;
    }

    public <T extends Component> boolean hasComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                return true;
            }
        }
        return false;
    }

    public <T extends Component> void removeComponent(Class<T> component) {
        for (int i = 0; i < components.size(); i++) {
            if (component.isAssignableFrom(components.get(i).getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {
        components.add(c);
        c.gameObject = this;
    }

    public void update(float dt) {
        for (Component c : components) {
            c.update(dt);
        }
    }

    public void start() {
        for (Component c : components) {
            c.start();
        }
    }

    public int zIndex() {
        return this.zIndex;
    }
}
