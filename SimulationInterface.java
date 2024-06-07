/**
 * Structure for the Voting Service application
 */

 interface SimulationInterface {

    // Submits a student's vote for a specific question.
    void submitVote(Student student, Question question);

    // Displays an students results for each of their questions.
    void displayResults();

    // Displays the consolidated result of all questions taken.
    void displayConsolidatedResults();
}