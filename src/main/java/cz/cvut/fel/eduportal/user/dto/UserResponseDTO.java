package cz.cvut.fel.eduportal.user.dto;

import java.util.List;

public record UserResponseDTO(
        Integer id,
        String username,
        String email,
        String firstName,
        String lastName,
        List<String> enrolledCoursesCodes,
        List<String> teachingCoursesCodes
) {
}
