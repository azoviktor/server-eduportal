package cz.cvut.fel.eduportal.exception.teacher;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TeacherAlreadyAssignedException extends RuntimeException {
    public TeacherAlreadyAssignedException(String username, String courseCode) {
        super("Teacher with username " + username + " is already assigned to course " + courseCode);
    }
}
