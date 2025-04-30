package cz.cvut.fel.eduportal.exception.teacher;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TeacherNotAssignedException extends RuntimeException {
    public TeacherNotAssignedException(String username, String courseCode) {
        super("Teacher with username " + username + " is not assigned to course " + courseCode);
    }
}
