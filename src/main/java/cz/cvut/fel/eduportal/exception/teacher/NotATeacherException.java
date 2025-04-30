package cz.cvut.fel.eduportal.exception.teacher;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NotATeacherException extends RuntimeException {
    public NotATeacherException(String username) {
        super("User with username " + username + " is not a teacher");
    }
}
