package syntaxblade.engine.logic;

/**
 * Abstract base class for all Logic Puzzles presented during a Posture Break.
 */
public abstract class LogicPuzzle {
    protected String questionString;
    protected String expectedAnswer;
    protected float timeLimit;

    public LogicPuzzle(String questionString, String expectedAnswer, float timeLimit) {
        this.questionString = questionString;
        this.expectedAnswer = expectedAnswer;
        this.timeLimit = timeLimit;
    }

    public String getQuestionString() {
        return questionString;
    }

    public String getExpectedAnswer() {
        return expectedAnswer;
    }

    public float getTimeLimit() {
        return timeLimit;
    }

    /**
     * Validates the player's input against the expected answer.
     * @param input The string input provided by the player.
     * @return true if correct, false otherwise.
     */
    public boolean checkAnswer(String input) {
        if (input == null) return false;
        // Trim and ignore case for flexibility, though strict formatting could be applied based on design
        return input.trim().equalsIgnoreCase(expectedAnswer.trim());
    }
}
