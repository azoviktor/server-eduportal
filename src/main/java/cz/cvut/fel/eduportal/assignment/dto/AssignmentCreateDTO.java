package cz.cvut.fel.eduportal.assignment.dto;

import cz.cvut.fel.eduportal.assignment.AssignmentType;

import java.time.LocalDateTime;

public record AssignmentCreateDTO(
        String code,
        String title,
        AssignmentType type,
        LocalDateTime deadline,
        String courseCode
) {
}
