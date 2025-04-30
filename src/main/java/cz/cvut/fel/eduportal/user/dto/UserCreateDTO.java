package cz.cvut.fel.eduportal.user.dto;

import cz.cvut.fel.eduportal.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UserCreateDTO(
        @NotBlank(message = "Username is required")
        String username,

        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        @Email
        String email,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotEmpty(message = "At least one role is required")
        Set<Role> roles
) {
}
