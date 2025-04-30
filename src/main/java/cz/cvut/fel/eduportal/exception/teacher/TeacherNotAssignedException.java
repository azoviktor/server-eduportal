package cz.cvut.fel.eduportal.exception.teacher;

public class TeacherNotAssignedException extends RuntimeException {
    public TeacherNotAssignedException(String username, String courseCode) {
        super("Teacher with username " + username + " is not assigned to course " + courseCode);
    }
}
