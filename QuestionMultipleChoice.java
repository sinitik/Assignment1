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

        // Makes a copy of the value in upper case in case the response was lower
        String answer = originalAnswer.trim().toUpperCase();

        // Error handling if the response is more than letter
        if (answer.length() != 1) {
            System.out.println("Invalid input format. Please enter a single letter.");
            return false;
        }

        for (String option: options) {
            if (option.substring(1, 2).equals(answer)) {
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