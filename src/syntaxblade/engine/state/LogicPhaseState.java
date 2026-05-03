package syntaxblade.engine.state;

import syntaxblade.engine.logic.LogicPuzzle;
import syntaxblade.engine.logic.PuzzleFactory;

/**
 * Represents the Logic Phase screen in the game.
 * Assumes a LibGDX-like structure (Screen/State interface concepts).
 */
public class LogicPhaseState {
    
    private LogicPuzzle currentPuzzle;
    private float timeRemaining;
    private StringBuilder playerInputBuffer;
    
    private boolean isFinished;
    private boolean wasSuccessful;

    public LogicPhaseState() {
        this.playerInputBuffer = new StringBuilder();
        this.isFinished = false;
        this.wasSuccessful = false;
    }

    /**
     * Called when this state becomes the active screen.
     * Pauses the physical combat engine and sets up the puzzle.
     * 
     * @param enemyTier The tier of the enemy whose posture was broken.
     */
    public void enter(int enemyTier) {
        System.out.println("\n[LogicPhaseState] Entering Logic Phase! Pausing real-time combat.");
        this.currentPuzzle = PuzzleFactory.createPuzzle(enemyTier);
        this.timeRemaining = currentPuzzle.getTimeLimit();
        this.playerInputBuffer.setLength(0); // Clear input
        this.isFinished = false;
        this.wasSuccessful = false;
        
        System.out.println("[LogicPhaseState] Puzzle Loaded. You have " + timeRemaining + " seconds.");
    }

    /**
     * Simulated LibGDX update loop.
     * @param deltaTime The time elapsed since the last frame.
     */
    public void update(float deltaTime) {
        if (isFinished) return;

        timeRemaining -= deltaTime;
        if (timeRemaining <= 0) {
            timeRemaining = 0;
            resolvePhase(false); // Time's up, failed the puzzle
        }
    }

    /**
     * Simulated Input Processor for keyboard typing.
     * In LibGDX, this would be hooked to InputProcessor.keyTyped(char character).
     */
    public void handleInput(char typedChar) {
        if (isFinished) return;

        if (typedChar == '\b') { // Backspace
            if (playerInputBuffer.length() > 0) {
                playerInputBuffer.deleteCharAt(playerInputBuffer.length() - 1);
            }
        } else if (typedChar == '\n' || typedChar == '\r') { // Enter key
            submitAnswer();
        } else {
            playerInputBuffer.append(typedChar);
        }
    }

    private void submitAnswer() {
        String answer = playerInputBuffer.toString();
        System.out.println("[LogicPhaseState] Submitting answer: " + answer);
        
        if (currentPuzzle.checkAnswer(answer)) {
            resolvePhase(true);
        } else {
            resolvePhase(false);
        }
    }

    private void resolvePhase(boolean success) {
        this.isFinished = true;
        this.wasSuccessful = success;
        
        if (success) {
            System.out.println("[LogicPhaseState] CORRECT! Execution blow will be dealt.");
        } else {
            System.out.println("[LogicPhaseState] FAILED! The enemy recovers their posture.");
        }
        
        // At this point, the GameStateManager would pop this state and return to ACTION_PHASE,
        // applying massive damage if successful, or resetting the enemy's posture if failed.
    }

    /**
     * Simulated LibGDX rendering.
     * In LibGDX, you would use SpriteBatch and BitmapFont here.
     */
    public void render() {
        if (currentPuzzle == null || isFinished) return;

        System.out.println("\n=================================");
        System.out.println("||       LOGIC PHASE           ||");
        System.out.println("=================================");
        System.out.println("Time Remaining: " + String.format("%.1f", timeRemaining) + "s");
        System.out.println("---------------------------------");
        System.out.println(currentPuzzle.getQuestionString());
        System.out.println("---------------------------------");
        System.out.print("Your Input: " + playerInputBuffer.toString() + "_\n");
    }

    public String getPuzzleQuestion() {
        return currentPuzzle != null ? currentPuzzle.getQuestionString() : "";
    }
    
    public String getPlayerInput() {
        return playerInputBuffer.toString();
    }
    
    public float getTimeRemaining() {
        return timeRemaining;
    }

    public float getTimeLimit() {
        return currentPuzzle != null ? currentPuzzle.getTimeLimit() : 1f;
    }

    public boolean isFinished() { return isFinished; }
    public boolean wasSuccessful() { return wasSuccessful; }
}
