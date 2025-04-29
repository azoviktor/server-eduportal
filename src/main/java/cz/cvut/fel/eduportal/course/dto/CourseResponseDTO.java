package cz.cvut.fel.eduportal.course.dto;

import java.util.List;

public record CourseResponseDTO(
        Integer id,
        String title,
        String code,
        List<String> studentsUsernames,
        List<String> teachersUsernames,
        List<String> assignmentsCodes
) {
}
