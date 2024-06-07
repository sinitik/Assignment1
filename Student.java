/**
 * Stores the student's ID and their answer to a question. 
 */
class Student {

    private String studentID; // Unique String for the student.
    private String answer; // Holds the answer for each student.

    /**
     * Constructor
     * @param studentID
     */
    public Student(String studentID) {
        this.studentID = studentID;
    }

    // Setter to record the student's answer.
    public void submitAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Getter methods for the student's answer
     * @return student's answer
     */
    public String getAnswer() {
        return answer;
    }
    /**
     * Getter method for the studentID
     * @return student's ID
     */
    public String getStudentID() {
        return studentID;
    }
}