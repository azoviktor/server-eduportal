package cz.cvut.fel.eduportal.user;

import cz.cvut.fel.eduportal.exception.AlreadyExistsException;
import cz.cvut.fel.eduportal.exception.NotFoundException;
import cz.cvut.fel.eduportal.user.dto.UserCreateDTO;
import cz.cvut.fel.eduportal.user.dto.UserResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public UserResponseDTO createUser(UserCreateDTO userDTO) throws AlreadyExistsException {
        String normalizedUsername = userDTO.username().trim().toLowerCase();
        if (userRepository.findByUsername(normalizedUsername).isPresent()) {
            throw new AlreadyExistsException("User with username " + normalizedUsername + " already exists");
        }
        User user = UserConverter.toEntity(userDTO);
        user.setUsername(normalizedUsername);
        userRepository.save(user);
        return UserConverter.toResponseDTO(user);
    }

    public UserResponseDTO updateUser(String username, UserCreateDTO userDTO) throws NotFoundException {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setUsername(userDTO.username());
            user.setPassword(userDTO.password());
            user.setEmail(userDTO.email());
            user.setFirstName(userDTO.firstName());
            user.setLastName(userDTO.lastName());
            userRepository.save(user);
            return UserConverter.toResponseDTO(user);
        } else {
            throw new NotFoundException("User not found with username: " + username);
        }
    }

    @Transactional
    public void deleteUser(String username) throws NotFoundException {
        if (!userRepository.existsByUsername(username)) {
            throw new NotFoundException("User not found with username: " + username);
        }
        userRepository.deleteByUsername(username);
    }
}
