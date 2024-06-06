import java.util.*;


// Superclass to serve as the basis for Single/Multiple Choice questions, as well as
// future variations of questions if needed.

abstract class Question {

    // Holds the question text that will be prompted
    protected String questionText;

    // List to hold the valid answers a student can use.
    protected List < String > options;

    protected String fullAnswer;

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

    public String getFullAnswer() {
        return fullAnswer;
    }


    // Distinguishes if the question is multiple choice or single
    public abstract boolean isMultipleChoice();

    // Now returns boolean to indicate validity (true) or not (false)
    public abstract boolean isValidAnswer(String answer); // Changed return type to boolean 
}
