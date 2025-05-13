package cz.cvut.fel.eduportal.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    STUDENT, TEACHER, ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
