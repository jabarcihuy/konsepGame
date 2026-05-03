package syntaxblade.engine.fsm;

/**
 * Represents the possible states for an entity in the game.
 */
public enum EntityState {
    IDLE,
    MOVING,
    ATTACKING,
    PARRYING,
    STAGGERED,
    DEAD
}
