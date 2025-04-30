package cz.cvut.fel.eduportal.user.dto;

import cz.cvut.fel.eduportal.user.Role;

import java.util.List;
import java.util.Set;

public record UserResponseDTO(
        Integer id,
        String username,
        String email,
        String firstName,
        String lastName,
        Set<Role> roles,
        List<String> enrolledCoursesCodes,
        List<String> teachingCoursesCodes
) {
}
