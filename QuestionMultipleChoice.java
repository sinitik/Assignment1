import java.util.List;

// Subclass of Question

class QuestionMultipleChoice extends Question {

    // Constructor
    public QuestionMultipleChoice(String questionText, List < String > options) {
        super(questionText, options);
    }

    // Checks if the response is a valid response (A, B, C, D)
    @Override
    public boolean isValidAnswer(String originalAnswer) {

        // Makes a copy of the value in upper case in case the response was lower
        String answer = originalAnswer.trim().toUpperCase(); 

        // Error handling if the response is more than letter
        if (answer.length() != 1) {
            System.out.println("Invalid input format. Please enter a single letter.");
            return false;
        }
    
        for (String option : options) { 
            if (option.substring(1, 2).equals(answer)) {
                setFullAnswer(option);  
                return true;
            }
        }
    
        System.out.println("Invalid answer. Please choose from: " + getOptions());
        return false; 
    }

    @Override
    public boolean isMultipleChoice() {
        return true;
    }
}