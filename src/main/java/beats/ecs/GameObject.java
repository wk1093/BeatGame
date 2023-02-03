package beats.ecs;

import beats.Loggable;

import java.util.ArrayList;
import java.util.List;

public class GameObject implements Loggable {
    private String name;
    private List<Component> components;
    public Transform transform;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
    }

    public GameObject(String name, Transform transform) {
        this.name = name;
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
        LOGGER.warning("Tried to get component " + componentClass.getName() + " from GameObject " + name + " but it does not have that component (use hasComponent() to check)");
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
        LOGGER.warning("Tried to remove component " + component.getName() + " from GameObject " + name + " but it does not have that component (use hasComponent() to check)");
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
}
