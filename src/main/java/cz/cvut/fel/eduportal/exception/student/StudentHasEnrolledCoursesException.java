package cz.cvut.fel.eduportal.exception.student;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StudentHasEnrolledCoursesException extends RuntimeException {
    public StudentHasEnrolledCoursesException(String username) {
        super("Student with username " + username + " has enrolled courses");
    }
}
