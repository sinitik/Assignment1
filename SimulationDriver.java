import java.util.*;

/**
 * Driver program for the iVote Service. Initializes and runs a voting session.
 * After running, the program will prompt if the user is running the program in automatic
 * or manual mode. If used in automatic, it will generate 10-50 students that will provide
 * answers automatically. Whereas if manual mode is selected, it will prompt how many users
 * will be using the service. After a positive integer is input, it will request for a 
 * unique studentID. After providing a unique ID the user will be able to answer questions.
 * 
 */

public class SimulationDriver {
    public static void main(String[] args) {

        VotingSession session = new VotingSession();

        // Configures questions from the configureQuestions method
        session.configureQuestions();

        System.out.println("Welcome to the iVote Service!");
        System.out.println("=============================\n");

        Scanner inputScan = new Scanner(System.in);
        session.selectMode(inputScan);


        // Generates students if the session is in automatic mode
        // else it will collect the student responses
        if (session.isAutomaticMode()) {
            session.generateStudents();
        } else {
            session.collectStudentResponses(inputScan);
        }


        session.displayResults();
        inputScan.close();
    }
}

/**  VotingSession holds the question configuration, a manual/auto mode for usage, and 
 * the ability to call the VotingService class to provide the logged collected votes. 
 */
class VotingSession {
    private VotingService votingService = new VotingService();
    private List < Question > questionList = new ArrayList < > ();
    private boolean automaticMode; // Whether automatic or manual mode
    private Random randomID = new Random();

    /**
     * Configures the questions for the voting session
     * Covers part 2/3 of the question prompt
     */
    public void configureQuestions() {
        questionList.add(new QuestionMultipleChoice(
            "1.) What are your favorite programming languages?",
            Arrays.asList("[A] Java", "[B] Python", "[C] C++", "[D] Rust")
        ));
        questionList.add(new QuestionMultipleChoice(
            "2.) What is your favorite day?",
            Arrays.asList("[A] Monday", "[B] Tuesday", "[C] Friday", "[D] Saturday")
        ));
        questionList.add(new QuestionSingleChoice(
            "3.) Do you take the elevator to class?",
            Arrays.asList("[1] for Yes", "[2] for No")
        ));
        questionList.add(new QuestionSingleChoice(
            "4.) Is this class CS3560?",
            Arrays.asList("[1] for Yes", "[2] for No")
        ));
        questionList.forEach(votingService::configureQuestion);
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
            for (Question question: questionList) {
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
        Map<String, String> studentRecords = new HashMap<>(); 
        System.out.print("How many students are using the iVote service? ");
        int numberOfStudents = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < numberOfStudents; i++) {
            String studentId;
            boolean uniqueId = false;

            do {
                System.out.print("Enter student ID: ");
                studentId = scanner.nextLine();

                if (studentRecords.containsValue(studentId)) {
                    System.out.println("This student ID has already been used. Please enter a unique ID.");
                } else {
                    uniqueId = true;
                    studentRecords.put("Student " + (i + 1), studentId); 
                }
            } while (!uniqueId);

            Student student = new Student(studentId);
            for (Question question : questionList) { 
                String answer;
                do {
                    System.out.println(question.getQuestionText());
                    System.out.println("Choose an answer from: " + String.join(", ", question.getOptions()));
                    answer = scanner.nextLine();
                } while (!question.isValidAnswer(answer)); 

                student.submitAnswer(answer);
                votingService.submitVote(student, question);
            }
        }
    }


    // Originally was used to create a studentID provided by a user's name. Opted for using a studentID system 
    // instead to avoid having to deal with duplicate names.
    /**
     * Retrieves a unique student ID from the user and ensures it hasn't been used before.
     *
     * @param scanner The Scanner object for reading user input.
     * @param studentRecords A map storing the names and IDs of students who have already responded.
     * @return A unique student ID string.
     */
    // private String getUniqueStudentID(Scanner scanner, Map < String, String > studentRecords) {
    //     String studentName;
    //     String studentID = "";
    //     boolean uniqueID = false; // Checks if the ID is unique

    //     do {
    //         System.out.print("Enter student's name: ");
    //         studentName = scanner.nextLine();
    //         if (studentRecords.containsKey(studentName)) {
    //             System.out.println("A student may only take the test once.");
    //             System.out.println("The student ID " + studentRecords.get(studentName) + " has already been used.");
    //         } else {
    //             // Generate random ID directly within the method:
    //             studentID = String.format("%05d", randomID.nextInt(100000));
    //             System.out.println("This is " + studentName + "'s ID number: " + studentID);
    //             studentRecords.put(studentName, studentID);
    //             uniqueID = true; // Set the flag when a unique ID is found
    //         }
    //     } while (!uniqueID); // Loop until a unique ID is found

    //     return studentID;
    // }



    // Checks whether its mode is automatic or manual
    public boolean isAutomaticMode() {
        return automaticMode;
    }

    // Calls the votingService class methods to display the counted results of users
    public void displayResults() {
        votingService.displayResults();
        votingService.displayConsolidatedResults();
    }

}