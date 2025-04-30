package cz.cvut.fel.eduportal.user;

import cz.cvut.fel.eduportal.exception.AlreadyExistsException;
import cz.cvut.fel.eduportal.exception.NotFoundException;
import cz.cvut.fel.eduportal.user.dto.UserCreateDTO;
import cz.cvut.fel.eduportal.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO user) throws AlreadyExistsException {
        UserResponseDTO savedUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String username) {
        Optional<UserResponseDTO> user = userService.getUser(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String username, @Valid @RequestBody UserCreateDTO user)  throws NotFoundException {
        UserResponseDTO updatedUser = userService.updateUser(username, user);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{username}/roles")
    public ResponseEntity<UserResponseDTO> addRoles(@PathVariable String username, @RequestBody List<Role> roles) throws NotFoundException {
        UserResponseDTO updatedUser = userService.addRoles(username, roles);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) throws NotFoundException {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}
