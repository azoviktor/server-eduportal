package cz.cvut.fel.eduportal.user;
import cz.cvut.fel.eduportal.course.Course;
import cz.cvut.fel.eduportal.submission.Submission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String username;
    private String password;
    private String email;

    private String firstName;
    private String lastName;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "students")
    private Set<Course> enrolledCourses = new HashSet<>();

    @ManyToMany(mappedBy = "teachers")
    private Set<Course> teachingCourses = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Submission> submissions = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    public List<String> getEnrolledCoursesCodes() {
        return enrolledCourses.stream()
                .map(Course::getCode)
                .toList();
    }

    public List<String> getTeachingCoursesCodes() {
        return teachingCourses.stream()
                .map(Course::getCode)
                .toList();
    }

    public boolean isTeacher() {
        return roles.contains(Role.TEACHER);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public void addRoles(Set<Role> roles) {
        this.roles.addAll(roles);
    }

    public void removeRoles(Set<Role> roles) {
        this.roles.removeAll(roles);
    }
}

