import java.util.*;


/** Superclass to serve as the basis for Single/Multiple Choice questions, as well as
 * future variations of questions if needed such as open-ended questions.
 */

abstract class Question {

    // Holds the question text that will be prompted
    protected String questionText;

    // Strings to hold the valid answers a student can use.
    protected List < String > options;
    private String fullAnswer;

    // Constructor
    public Question(String questionText, List < String > options) {
        this.questionText = questionText;
        this.options = options;
    }

    // Getters for retrieving question details.
    public String getQuestionText() {
        return questionText;
    }
    public List < String > getOptions() {
        return options;
    }

    // Getter and setter for the fullAnswer field
    public String getFullAnswer() {
        return fullAnswer;
    }
    protected void setFullAnswer(String fullAnswer) {
        this.fullAnswer = fullAnswer;
    }

    // Distinguishes if the question is multiple choice or single
    public abstract boolean isMultipleChoice();

    // Now returns boolean to indicate validity (true) or not (false)
    public abstract boolean isValidAnswer(String answer); // Changed return type to boolean 
}