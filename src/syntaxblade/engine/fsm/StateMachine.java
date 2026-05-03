package syntaxblade.engine.fsm;

import syntaxblade.engine.entity.Component;
import syntaxblade.engine.entity.Entity;
import syntaxblade.engine.entity.HealthComponent;

/**
 * An advanced Finite State Machine implemented as a Component.
 * Tracks current state, state transitions, and frame-perfect timings (e.g., i-frames).
 */
public class StateMachine implements Component {
    private Entity owner;
    private EntityState currentState;
    private float stateTimer;
    
    // I-frame tracking
    private boolean isInvulnerable;
    private float invulnerabilityTimer;
    private float invulnerabilityDuration;

    // Parry window tracking
    private float parryWindowDuration;

    public StateMachine(Entity owner) {
        this.owner = owner;
        this.currentState = EntityState.IDLE;
        this.stateTimer = 0f;
        this.isInvulnerable = false;
    }

    public EntityState getCurrentState() {
        return currentState;
    }

    /**
     * Transitions to a new state if allowed.
     *
     * @param newState The state to transition to.
     */
    public void changeState(EntityState newState) {
        if (this.currentState == newState) return;
        
        // Example logic: Can't break out of stagger or death easily
        if (this.currentState == EntityState.DEAD) return;
        if (this.currentState == EntityState.STAGGERED && newState != EntityState.IDLE && newState != EntityState.DEAD) return;

        this.currentState = newState;
        this.stateTimer = 0f;

        // State entry logic
        switch (newState) {
            case PARRYING:
                parryWindowDuration = 0.2f; // 200ms active parry window
                break;
            case DEAD:
                setInvulnerable(true, 9999f);
                break;
            default:
                break;
        }
    }

    /**
     * Grants invulnerability frames (i-frames) for dodging/parrying.
     *
     * @param isInvulnerable Whether the entity is invulnerable.
     * @param duration       The duration of the i-frames.
     */
    public void setInvulnerable(boolean isInvulnerable, float duration) {
        this.isInvulnerable = isInvulnerable;
        this.invulnerabilityDuration = duration;
        this.invulnerabilityTimer = 0f;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    @Override
    public void update(float deltaTime) {
        stateTimer += deltaTime;

        // Handle i-frames
        if (isInvulnerable) {
            invulnerabilityTimer += deltaTime;
            if (invulnerabilityTimer >= invulnerabilityDuration) {
                isInvulnerable = false;
            }
        }

        // Automatic state transitions
        HealthComponent health = owner.getComponent(HealthComponent.class);
        if (health != null) {
            if (health.isDead() && currentState != EntityState.DEAD) {
                changeState(EntityState.DEAD);
            } else if (health.isStaggered() && currentState != EntityState.STAGGERED) {
                changeState(EntityState.STAGGERED);
            } else if (currentState == EntityState.STAGGERED && !health.isStaggered()) {
                changeState(EntityState.IDLE);
            }
        }

        // Handle specific state timers
        if (currentState == EntityState.PARRYING) {
            if (stateTimer >= parryWindowDuration) {
                changeState(EntityState.IDLE); // End of parry window
            }
        }
    }
}
