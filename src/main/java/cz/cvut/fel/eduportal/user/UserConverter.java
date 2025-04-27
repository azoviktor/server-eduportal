package cz.cvut.fel.eduportal.user;

import cz.cvut.fel.eduportal.user.dto.UserCreateDTO;
import cz.cvut.fel.eduportal.user.dto.UserResponseDTO;

public class UserConverter {
    public static User toEntity(UserCreateDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setUsername(userDTO.username());
        user.setPassword(userDTO.password());
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        return user;
    }

    public static UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getEnrolledCoursesCodes(),
                user.getTeachingCoursesCodes()
        );
    }
}
