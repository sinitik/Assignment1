import java.util.*;

/**
 * Driver program for the iVote Service. Initializes and runs a voting session.
 */
public class SimulationDriver {
    public static void main(String[] args) {
        VotingSession session = new VotingSession();
        session.configureQuestions();
        System.out.println("Welcome to the iVote Service!");
        System.out.println("=============================\n");

        Scanner scanner = new Scanner(System.in);
        session.selectMode(scanner);

        if (session.isAutomaticMode()) {
            session.generateStudents();
        } else {
            session.collectStudentResponses(scanner);
        }

        session.displayResults();
        scanner.close();
    }
}

/**  Makes a VotingSession that holds the question configuration, a manual/auto mode for usage, and 
 * the ability to call the VotingService class to provide the logged collected votes. 
 */
class VotingSession {
    private VotingService votingService = new VotingService();
    private List < Question > questions = new ArrayList < > ();
    private boolean automaticMode; // Whether automatic or manual mode
    private static final long STUDENT_ID_SEED = 12345;

    /**
     * Configures the questions for the voting session
     * Covers part 2/3 of the question prompt
     */
    public void configureQuestions() {
        questions.add(new QuestionMultipleChoice(
            "1. What are your favorite programming languages?",
            Arrays.asList("[A] Java", "[B] Python", "[C] C++", "[D] Rust")
        ));
        questions.add(new QuestionMultipleChoice(
            "2. What is your favorite day?",
            Arrays.asList("[A] Monday", "[B] Tuesday", "[C] Friday", "[D] Saturday")
        ));
        questions.add(new QuestionSingleChoice(
            "3. Do you take the elevator to class?",
            Arrays.asList("[1] for Yes", "[2] for No")
        ));
        questions.add(new QuestionSingleChoice(
            "4. Is this class CS3560?",
            Arrays.asList("[1] for Yes", "[2] for No")
        ));
        questions.forEach(votingService::configureQuestion);
    }
    /**
     * Prompts the user to select the mode manual or automatic.
     * @param scanner Saves the users input for mode.
     */
    public void selectMode(Scanner scanner) {

        boolean validMode = false;
        while (!validMode) { // Loop until a valid mode response is given.
            System.out.println("Press [1] for manual mode");
            System.out.println("Press [2] for automatic mode");
            String mode = scanner.nextLine();

            if ("1".equals(mode)) {
                automaticMode = false; // Manual mode
                validMode = true;
            } else if ("2".equals(mode)) {
                automaticMode = true; // Automatic mode
                validMode = true;
            } else {
                System.out.println("Invalid mode selection. Please enter 1 or 2.");
            }
        }
    }

    /**
     * Generates a random number of students
     */
    public void generateStudents() {
        Random random = new Random();
        int numberOfStudents = 10 + random.nextInt(41); // Randomly generate 10 to 50 students
        System.out.println("Number of students participating: " + numberOfStudents);

        for (int i = 0; i < numberOfStudents; i++) {
            String studentId = String.format("%05d", random.nextInt(100000));
            Student student = new Student(studentId);

            // Enhanced for-loop to cycle through all generated students and to have them answer 
            // each configured question
            for (Question question: questions) {
                String answer = generateRandomAnswer(question, random);
                student.submitAnswer(answer);
                votingService.submitVote(student, question);
            }
        }
    }

    /**
     * Generates random answers to be used in conjunction with generateStudents
     * @param question The question for which to generate an answer.
     * @param answer   A response for the generator
     * @return Returns the students answer
     */
    private String generateRandomAnswer(Question question, Random answer) {
        List < String > options = question.getOptions();
        if (question.isMultipleChoice()) {
            return options.get(answer.nextInt(options.size())).substring(1, 2);
        } else {
            return "" + (1 + answer.nextInt(2));
        }
    }


    /**
     * Collects responses if driver is in manual mode.
     * Prompts for the number of students and then takes individual responses for each question.
     * @param scanner The Scanner object for reading user input.
     */

    public void collectStudentResponses(Scanner scanner) {
        Map < String, String > studentRecords = new HashMap < > ();
        int numberOfStudents = 0;
        boolean validInput = false;

        // Error handling if a positive integer is not put in for the number of students using the service.
        while (!validInput) {
            System.out.print("How many students are using the iVote service? ");
            try {
                numberOfStudents = Integer.parseInt(scanner.nextLine());
                if (numberOfStudents > 0) {
                    validInput = true;
                } else {
                    System.out.println("Please enter a positive integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
            }
        }
        for (int i = 0; i < numberOfStudents; i++) {
            Student student = new Student(getUniqueStudentID(scanner, studentRecords));

            for (Question question: questions) {
                String answer;
                do {
                    System.out.println(question.getQuestionText());
                    System.out.println("Choose an answer from: " + String.join(", ", question.getOptions()));
                    answer = scanner.nextLine();
                } while (!question.isValidAnswer(answer)); // Loop until a valid answer is provided

                student.submitAnswer(answer);
                votingService.submitVote(student, question);
            }
        }
    }



    /**
     * Retrieves a unique student ID from the user and ensures it hasn't been used before.
     *
     * @param scanner The Scanner object for reading user input.
     * @param studentRecords A map storing the names and IDs of students who have already responded.
     * @return A unique student ID string.
     */
    private String getUniqueStudentID(Scanner scanner, Map < String, String > studentRecords) {
        String studentName;
        String studentId = null;
        boolean uniqueId = false;
        Random randomID = new Random(STUDENT_ID_SEED); // Seeded random for student IDs

        do {
            System.out.print("Enter student's name: ");
            studentName = scanner.nextLine();
            if (studentRecords.containsKey(studentName)) {
                System.out.println("A student may only take the test once.");
                System.out.println("The student ID " + studentRecords.get(studentName) + " has already been used.");
            } else {
                studentId = String.format("%05d", randomID.nextInt(100000)); // Uses a seed to keep results for the ID same for users
                System.out.println("This is " + studentName + "'s ID number: " + studentId);
                studentRecords.put(studentName, studentId);
                uniqueId = true;
            }
        } while (!uniqueId); // Keep looping if the ID is not unique
        return studentId;
    }


    public boolean isAutomaticMode() {
        return automaticMode;
    }
    public void displayResults() {
        votingService.displayResults();
        votingService.displayConsolidatedResults();
    }

}