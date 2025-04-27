package cz.cvut.fel.eduportal.course;

import cz.cvut.fel.eduportal.assignment.Assignment;
import cz.cvut.fel.eduportal.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "courses",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code")
        }
)
@Data
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String title;
    private String code;
    private String description;

    @ManyToMany
    @JoinTable(
            name = "courses_teachers",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Set<User> teachers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "courses_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<User> students = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<Assignment> assignments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id.equals(course.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public List<String> getStudentsUsernames() {
        return students.stream()
                .map(User::getUsername)
                .toList();
    }

    public List<String> getTeachersUsernames() {
        return teachers.stream()
                .map(User::getUsername)
                .toList();
    }

    public void addTeacher(User teacher) {
        if (teacher != null) {
            teachers.add(teacher);
        }
    }
}
