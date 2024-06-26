import java.util.*;


/**
 * The VotingService class implements the iVoteInterface and is responsible for managing the voting process.
 * Holds methods for configuring questions, submitting votes, and displaying the results by 
 * cycling through the studentID list 
 */

class VotingService implements SimulationInterface {

    // Stores all questions and collected votes
    private List < Question > questions = new ArrayList < > ();

    // Maintains the order of the questions added and stores the number of votes
    // for each of the questions
    private Map < String, Map < String, Integer >> allVotes = new LinkedHashMap < > ();


    /**
     * Configures a question for the voting session.
     * Initializes the vote count map for this question with empty (zero) values for all possible answers.
     *
     * @param question The question to be added to the voting session.
     */
    public void configureQuestion(Question question) {
        // Configures a question for the voting session.
        // Initializes the vote count map for this question with empty (zero) values for all possible answers.
        questions.add(question); // Add the question to the list
        allVotes.put(question.getQuestionText(), new HashMap < > ()); // Create an empty map for this question's votes in allVotes
    }

    /**
     * Submits a student's vote for a specific question.
     *
     * @param student The student submitting the vote.
     * @param question The question the student is voting on.
     */

     public void submitVote(Student student, Question question) {
        String studentAnswer = student.getAnswer();
        boolean validAnswer = question.isValidAnswer(studentAnswer);
    
        if (validAnswer) {
            String answerKey = question.getFullAnswer(); // Get the full answer, including brackets
            Map<String, Integer> voteCounts = allVotes.get(question.getQuestionText());
            voteCounts.put(answerKey, voteCounts.getOrDefault(answerKey, 0) + 1); // Increment the count for the full answer
        }
    }

    /**
     * Displays the voting results for each individual question.
     */
    public void displayResults() {

        for (Question question: questions) {
            System.out.println("\nVoting Results for: " + question.getQuestionText()); // Reiterates the question prompt
            Map < String, Integer > voteCounts = allVotes.get(question.getQuestionText());

            // Display vote count for the questions options
            for (String option: question.getOptions()) {
                System.out.println(option + ": " + voteCounts.getOrDefault(option, 0));
            }
        }
    }

    /**
     * Displays the consolidated results of all questions, combining results for similar answer types (A, B, C, D or True/False).
     */
    public void displayConsolidatedResults() {

        System.out.println("\nVoting Results For All iVote Service Questions:");
        Map < String, Integer > consolidatedResults = new HashMap < > ();

        // Initialize the map with zero counts for all possible options (A, B, C, D, or T/F)
        consolidatedResults.put("A", 0);
        consolidatedResults.put("B", 0);
        consolidatedResults.put("C", 0);
        consolidatedResults.put("D", 0);
        consolidatedResults.put("True", 0);
        consolidatedResults.put("False", 0);

        // Iterate over all questions and their vote counts:
        for (Map < String, Integer > voteCounts: allVotes.values()) {
            for (Map.Entry < String, Integer > entry: voteCounts.entrySet()) {
                String key = entry.getKey().substring(1, 2); // Extract the option letter (A, B, C, D) or the answer (1/2)
                if (key.equals("1")) {
                    key = "True"; // Set "1" to "True"
                } else if (key.equals("2")) {
                    key = "False"; // Set "2" to "False"
                }
                consolidatedResults.put(key, consolidatedResults.get(key) + entry.getValue()); // Increment count for the option
            }
        }

        // Displays the consolidated results:
        System.out.println("[A]: " + consolidatedResults.get("A"));
        System.out.println("[B]: " + consolidatedResults.get("B"));
        System.out.println("[C]: " + consolidatedResults.get("C"));
        System.out.println("[D]: " + consolidatedResults.get("D"));
        System.out.println("[True]: " + consolidatedResults.get("True"));
        System.out.println("[False]: " + consolidatedResults.get("False"));
    }
}