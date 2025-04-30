package cz.cvut.fel.eduportal.exception.teacher;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TeacherHasAssignedCoursesException extends RuntimeException {
    public TeacherHasAssignedCoursesException(String username) {
        super("Teacher with username " + username + " has assigned courses");
    }
}
