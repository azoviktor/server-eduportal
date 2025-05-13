package cz.cvut.fel.eduportal.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CourseCreateDTO(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Code is required")
        String code,

        List<String> teachersUsernames
) {
}
