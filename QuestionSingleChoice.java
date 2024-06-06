import java.util.List;

// Subclass of Question, represents all single-choice questions (Yes/No)

class QuestionSingleChoice extends Question {


    // Constructor
    public QuestionSingleChoice(String questionText, List < String > options) {
        super(questionText, options);
    }

    @Override
    public boolean isValidAnswer(String answer) {
        if (answer.equals("1") || answer.equals("2")) {
            fullAnswer = answer.equals("1") ? "[1] for Yes" : "[2] for No";
            return true; // Valid answer
        } else {
            System.out.println("Invalid answer. Please choose from: " + getOptions());
            return false; // Invalid answer
        }
    }

    @Override
    public boolean isMultipleChoice() {
        return false;
    }
}
