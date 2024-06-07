import java.util.*;

/**
 * Driver program for the iVote Service. Initializes and runs a voting session.
 * After running, the program will prompt if the user is running the program in automatic
 * or manual mode. 
 * 
 * Automatic Mode: Generates 10-50 students randomly where they are associated by studentID
 * Each studentID has 2-5 answers per question, where it saves the most recent valid response
 * by that student. 
 * Manual Mode: Input a postive integer stating how many students are using the iVote Service
 * followed by a unique studentID. Afterward, the user will answer the questions manually.
 * 
 * At the end of manual and automatic mode, the program will display the answers using the VotingService
 * class to display all consolidated answers and answers for the individual questions.
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
        session.selectMode(inputScan); // Let the user choose manual or automatic mode


        // Generates students if the session is in automatic mode
        // else it will collect the student responses
        if (session.isAutomaticMode()) {
            session.generateStudents();
        } else {
            session.collectStudentResponses(inputScan);
        }


        session.displayResults(); // Show the voting results
        inputScan.close(); // Closed the scanner
    }
}

/**  VotingSession holds the question configuration, a manual/auto mode for usage, and 
 * the ability to call the VotingService class to provide the logged collected votes. 
 */
class VotingSession {
    private VotingService votingService = new VotingService();
    private List < Question > questionList = new ArrayList < > ();
    private boolean automaticMode; // Whether automatic or manual mode


    /**
     * Configures the questions for the voting session
     * and adds them to the questionList
     * Covers part 2/3 of the question prompt
     */
    public void configureQuestions() {
        questionList.add(new QuestionMultipleChoice(
            "1.) What are your favorite programming languages?",
            Arrays.asList("[A] Python", "[B] Java", "[C] C++", "[D] Rust")
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
     * Simulates student responses in automatic mode.
     * Generates a random number of students (10-50), assigns them IDs,
     * and automatically generates random answers for each question.
     */
    public void generateStudents() {
        Random random = new Random();
        int numberOfStudents = 10 + random.nextInt(41); 
        System.out.println("Number of students participating: " + numberOfStudents);
    
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < numberOfStudents; i++) {
            String studentId = String.format("%05d", random.nextInt(100000));
            students.add(new Student(studentId));
        }
    
        // Store last valid answers per student and question
        Map<String, Map<Question, String>> lastValidAnswers = new HashMap<>();
        for (Student student : students) {
            lastValidAnswers.put(student.getStudentID(), new HashMap<>());
        }
    
        // Simulate students having multiple answers per question but only storing
        // the most recent answer
        for (Student student : students) {
            for (Question question : questionList) {
                // Generates 2-5 answers per question
                int numAnswers = 2 + random.nextInt(4); 
                for (int j = 0; j < numAnswers; j++) {
                    String answer = generateValidAnswer(question, random);
                    if (question.isValidAnswer(answer)) { // Checks if the answers that were generated before saving
                        lastValidAnswers.get(student.getStudentID()).put(question, answer); 
                    }
                }
            }
        }
       
        // If answer was valid, it will save the lastValidAnswer and add it to the student's ID as their answer for that question
        for (Student student : students) {
            for (Question question : questionList) {
                String lastValidAnswer = lastValidAnswers.get(student.getStudentID()).get(question);
                if (lastValidAnswer != null) {
                    student.submitAnswer(lastValidAnswer);
                    votingService.submitVote(student, question);
                }
            }
        }
    }
    


    /**
     * Generates random valid answers to be used in conjunction with generateStudents
     * @param question The question for which to generate an answer.
     * @param answer   A response for the generator
     * @return Returns the students answer
     */
    private String generateValidAnswer(Question question, Random random) {
        List < String > options = question.getOptions();

        if (question.isMultipleChoice()) {
            int randomIndex = random.nextInt(options.size());
            String option = options.get(randomIndex);
            return option.substring(1, 2).toUpperCase(); // Extract and return A, B, C, or D
        } else {
            return random.nextInt(2) == 0 ? "1" : "2"; // Return 1 or 2 for single-choice
        }
    }



    /**
     * Collects responses if driver is in manual mode.
     * Prompts for the number of students and then takes individual responses for each question.
     * @param scanner The Scanner object for reading user input.
     */
    public void collectStudentResponses(Scanner scanner) {
        Map < String, String > studentRecords = new HashMap < > ();
        System.out.print("How many students are using the iVote service? ");
        int numberOfStudents = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < numberOfStudents; i++) {
            String studentID;
            boolean uniqueID = false;


            do {
                System.out.print("Enter student ID: ");
                studentID = scanner.nextLine();

                // Error handling if the studentID has already been taken.
                if (studentRecords.containsValue(studentID)) {
                    System.out.println("This student ID has already been used. Please enter a unique ID.");
                } else {
                    uniqueID = true;
                    studentRecords.put("Student " + (i + 1), studentID);
                }
            } while (!uniqueID);

            // For each unique studentID begin providing the questionList
            Student student = new Student(studentID);
            for (Question question: questionList) {
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