import java.util.List;

/** 
*   Subclass of Question, represents all single-choice questions (Yes/No)
*/
class QuestionSingleChoice extends Question {


    // Constructor
    public QuestionSingleChoice(String questionText, List < String > options) {
        super(questionText, options);
    }

    /**
     * Verifies if answer is valid or not
     * @return true for valid, false for invalid
     */
    @Override
    public boolean isValidAnswer(String answer) {
        if (answer.equals("1") || answer.equals("2")) {
            setFullAnswer(answer.equals("1") ? "[1] for Yes" : "[2] for No");
            return true; // Valid answer
        } else {
            System.out.println("Invalid answer. Please choose from: " + getOptions());
            return false; // Invalid answer
        }
    }

    /**
     * Returns false since it is not multiple choice
     * @return false
     */
    @Override
    public boolean isMultipleChoice() {
        return false;
    }
}
