import java.util.List;

/**
 * Subclass of the Question superclass, handles all multiple choice questions (A, B, C, D)
 */

class QuestionMultipleChoice extends Question {

    // Constructor
    public QuestionMultipleChoice(String questionText, List < String > options) {
        super(questionText, options);
    }


    /**
     * Verifies if answer is valid valid response (A, B, C, D) or not
     * @return true for valid, false for invalid
     */
    @Override
    public boolean isValidAnswer(String originalAnswer) {
        String answer = originalAnswer.trim().toUpperCase(); // 

        if (answer.length() != 1) { // Check if it's a single letter
            System.out.println("Invalid input format. Please enter a single letter.");
            return false;
        }

        // Find the full answer based on the letter choice (A, B, C, or D)
        for (String option : options) {
            if (option.startsWith("[" + answer + "]")) { // Check if option starts with the letter
                setFullAnswer(option);
                return true;
            }
        }

        System.out.println("Invalid answer. Please choose from: " + getOptions());
        return false;
    }

    /**
     * @return returns true since the Question is multipleChoice
     */
    @Override
    public boolean isMultipleChoice() {
        return true;
    }
}