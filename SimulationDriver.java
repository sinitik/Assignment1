import java.util.*;

public class SimulationDriver {

    public static void main(String[] args) {

        VotingService votingService = new VotingService();
        Scanner scanner = new Scanner(System.in);
        Random random = new Random(); // Used to generate the random responses
        Map<String, String> studentRecords = new HashMap<>(); 

        System.out.println("Welcome to the iVote Service!");
        System.out.println("=============================\n");

        // 1/2) Question configuration: Create and configure the questions for the simulation.
        QuestionMultipleChoice mcq = new QuestionMultipleChoice(
            "1. What are your favorite programming languages?",
            Arrays.asList("[A] Java", "[B] Python", "[C] C++", "[D] Rust"));

        QuestionMultipleChoice mcq2 = new QuestionMultipleChoice(
            "2. What is your favorite day?",
            Arrays.asList("[A] Monday", "[B] Tuesday", "[C] Friday", "[D] Saturday"));

        QuestionSingleChoice scq = new QuestionSingleChoice(
            "3. Do you take the elevator to class?",
            Arrays.asList("[1] for Yes", "[2] for No"));

        QuestionSingleChoice scq2 = new QuestionSingleChoice(
            "4. Is this class CS3560?",
            Arrays.asList("[1] for Yes", "[2] for No"));

        // Add the configured questions to the iVote service:
        List<Question> questions = Arrays.asList(mcq, mcq2, scq, scq2);

        for (Question question : questions) {
            votingService.configureQuestion(question);
        }

        String mode = "";
        boolean validMode = false;

        while (!validMode) { // Loop until a valid mode response is given.
            System.out.println("Press [1] for manual mode");
            System.out.println("Press [2] for automatic mode");
            mode = scanner.nextLine();

            if ("1".equals(mode) || "2".equals(mode)) {
                validMode = true;
            } else {
                System.out.println("Invalid mode selection. Please enter 1 or 2.");
            }
        }

        // Automatic mode is selected. Generates a random number of students between 10 and 50 to randomly answer the questions
        if ("2".equals(mode)) {

            // 3) Randomly generates a number of students ranging from 10 to 50
            //  to "answer" the prompted questions
            int numberOfStudents = 10 + random.nextInt(41);
            System.out.println("Number of students participating: " + numberOfStudents);

            for (int i = 0; i < numberOfStudents; i++) {
                // Generates the student IDs, but not useful outside of manual mode.
                String studentId = String.format("%05d", random.nextInt(100000));
                Student student = new Student(studentId);

                for (Question question : questions) { // Iterate over Question objects
                    List<String> options = question.getOptions();
                    String answer;

                    if (question.isMultipleChoice()) {
                        int index = random.nextInt(options.size());
                        answer = options.get(index).substring(1, 2);
                    } else {
                        answer = "" + (1 + random.nextInt(2));
                    }

                    // 4) submit all the students’ answers to iVote Service
                    student.submitAnswer(answer);
                    votingService.submitVote(student, question);
                }
            }
        } // This closing brace was missing, causing the syntax error

        // Manual mode selected
        else if ("1".equals(mode)) {
            int numberOfStudents = 0;
            boolean validInput = false;
        
            while (!validInput) {
                try {
                    System.out.print("How many students are using the iVote service? ");
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
                String studentName;
                String studentId;
                boolean validName = false; // Initialize validName to false
        
                do { // Start a do-while loop for name validation
                    validName = true;
                    System.out.print("Enter student's name: ");
                    studentName = scanner.nextLine();
        
                    if (studentRecords.containsKey(studentName)) {
                        validName = false;
                        System.out.println("A student may only take the test once.");
                        System.out.println("The student ID " + studentRecords.get(studentName) + " has already been used.");
                        System.out.println("Please enter a new name.");
                    } else {
                        studentId = String.format("%05d", (int)(Math.random() * 100000));
                        System.out.println("This is " + studentName + "'s ID number: " + studentId);
                        studentRecords.put(studentName, studentId);
        
                        // Create the student object AFTER valid name is obtained
                        Student student = new Student(studentId);
        
                        for (Question question : questions) {
                            boolean validAnswer = false;
        
                            while (!validAnswer) {
                                System.out.println(question.getQuestionText());
                                System.out.println("Choose an answer from: " + String.join(", ", question.getOptions()));
                                String answer = scanner.nextLine();
        
                                validAnswer = question.isValidAnswer(answer); // This line is corrected to update validAnswer
        
                                if (validAnswer) {
                                    // 4) submit all the students’ answers to iVote Service
                                    student.submitAnswer(answer);
                                    votingService.submitVote(student, question);
                                } else {
                                    System.out.println("Please try again.");
                                }
                            } // End of while loop for invalid answers
                        } // End of question loop
                    } // End of else block for valid names
                } while (!validName);
            }
        } // This closing brace was missing, causing the syntax error
        // Display results after all questions have been answered

        // 5) call the VotingService output function to display the result. 
        votingService.displayResults(); // Shows the individual question's answers
        votingService.displayConsolidatedResults(); // Shows the consolidated question's answers
        scanner.close();
    }
}
