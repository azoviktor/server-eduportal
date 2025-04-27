package cz.cvut.fel.eduportal.user;
import cz.cvut.fel.eduportal.course.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String username;
    private String password;
    private String email;

    private String firstName;
    private String lastName;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "students")
    private Set<Course> enrolledCourses = new HashSet<>();

    @ManyToMany(mappedBy = "teachers")
    private Set<Course> teachingCourses = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
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
}

