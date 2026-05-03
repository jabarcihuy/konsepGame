package syntaxblade.engine.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic Entity class for the Entity-Component System.
 */
public class Entity {
    private List<Component> components = new ArrayList<>();

    public void addComponent(Component component) {
        components.add(component);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (componentClass.isInstance(component)) {
                return (T) component;
            }
        }
        return null;
    }

    public void update(float deltaTime) {
        for (Component component : components) {
            component.update(deltaTime);
        }
    }
}
