package cz.cvut.fel.eduportal.user;

import cz.cvut.fel.eduportal.exception.AlreadyExistsException;
import cz.cvut.fel.eduportal.exception.NotFoundException;
import cz.cvut.fel.eduportal.exception.student.StudentHasEnrolledCoursesException;
import cz.cvut.fel.eduportal.exception.teacher.NotATeacherException;
import cz.cvut.fel.eduportal.exception.teacher.TeacherHasAssignedCoursesException;
import cz.cvut.fel.eduportal.user.dto.UserCreateDTO;
import cz.cvut.fel.eduportal.user.dto.UserResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsernameOrThrow(String username) throws NotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username " + username + " not found"));
    }

    public User getTeacherByUsernameOrThrow(String username) throws NotFoundException, NotATeacherException {
        User user = getUserByUsernameOrThrow(username);
        if (!user.isTeacher()) {
            throw new NotATeacherException(username);
        }
        return user;
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserConverter::toResponseDTO)
                .toList();
    }

    public Optional<UserResponseDTO> getUser(String username) {
        String normalizedUsername = username.trim().toLowerCase();
        Optional<User> user = userRepository.findByUsername(normalizedUsername);
        return user.map(UserConverter::toResponseDTO);
    }

    @Transactional
    public UserResponseDTO createUser(UserCreateDTO userDTO) throws AlreadyExistsException {
        String normalizedUsername = userDTO.username().trim().toLowerCase();
        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new AlreadyExistsException("User with username " + normalizedUsername + " already exists");
        }
        User user = UserConverter.toEntity(userDTO);
        user.setUsername(normalizedUsername);
        userRepository.save(user);
        return UserConverter.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateUser(String username, UserCreateDTO userDTO) throws NotFoundException {
        User user = getUserByUsernameOrThrow(username);
        user.setUsername(userDTO.username());
        user.setPassword(userDTO.password());
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setRoles(userDTO.roles());
        return UserConverter.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO addRoles(String username, Set<Role> roles) throws NotFoundException {
        User user = getUserByUsernameOrThrow(username);
        user.addRoles(roles);
        return UserConverter.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO removeRoles(
            String username,
            Set<Role> roles
    ) throws NotFoundException, TeacherHasAssignedCoursesException, StudentHasEnrolledCoursesException {
        User user = getUserByUsernameOrThrow(username);
        if (roles.contains(Role.TEACHER) && !user.getTeachingCourses().isEmpty()) {
            throw new TeacherHasAssignedCoursesException(username);
        }
        if (roles.contains(Role.STUDENT) && !user.getEnrolledCourses().isEmpty()) {
            throw new StudentHasEnrolledCoursesException(username);
        }
        user.removeRoles(roles);
        return UserConverter.toResponseDTO(user);
    }

    @Transactional
    public void deleteUser(String username) throws NotFoundException {
        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            throw new NotFoundException("User not found with username: " + username);
        }
        User user = optUser.get();
        user.getTeachingCourses().forEach(course -> course.removeTeacher(user));
        user.getEnrolledCourses().forEach(course -> course.unenrollStudent(user));
        userRepository.delete(user);
    }
}
