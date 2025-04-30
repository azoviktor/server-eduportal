package cz.cvut.fel.eduportal.exception.student;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StudentAlreadyEnrolledException extends RuntimeException {
    public StudentAlreadyEnrolledException(String username, String courseCode) {
        super("Student with username " + username + " is already enrolled in course " + courseCode);
    }
}
