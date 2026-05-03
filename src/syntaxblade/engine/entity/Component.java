package syntaxblade.engine.entity;

/**
 * Interface for all components that can be attached to an Entity.
 */
public interface Component {
    void update(float deltaTime);
}
