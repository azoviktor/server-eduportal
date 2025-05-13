package cz.cvut.fel.eduportal.user.dto;

import cz.cvut.fel.eduportal.user.Role;
import java.util.Set;

public record RolesDTO(
        Set<Role> roles
) {
}
