package cz.cvut.fel.eduportal.exception.student;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StudentNotEnrolledException extends RuntimeException {
    public StudentNotEnrolledException(String username, String courseCode) {
        super("Student with username " + username + " is not enrolled in course " + courseCode);
    }
}
