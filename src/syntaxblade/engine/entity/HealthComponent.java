package syntaxblade.engine.entity;

/**
 * Component that manages the health and posture of an entity.
 */
public class HealthComponent implements Component {
    private int maxHealth;
    private int currentHealth;
    private boolean isStaggered;
    private float staggerTimer;
    private float staggerDuration = 3.0f;

    public HealthComponent(int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public void takeDamage(int amount) {
        currentHealth -= amount;
        if (currentHealth < 0) currentHealth = 0;
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    public boolean isStaggered() {
        return isStaggered;
    }

    public void setStaggered(boolean staggered) {
        this.isStaggered = staggered;
        if (staggered) staggerTimer = 0f;
    }

    @Override
    public void update(float deltaTime) {
        if (isStaggered) {
            staggerTimer += deltaTime;
            if (staggerTimer >= staggerDuration) {
                isStaggered = false;
            }
        }
    }
}
