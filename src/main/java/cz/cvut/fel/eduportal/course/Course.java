package cz.cvut.fel.eduportal.course;

import cz.cvut.fel.eduportal.assignment.Assignment;
import cz.cvut.fel.eduportal.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Set<User> students = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Assignment> assignments = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id.equals(course.id);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
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

    public List<String> getAssignmentsCodes() {
        return assignments.stream()
                .map(Assignment::getCode)
                .toList();
    }

    public void addTeacher(User teacher) {
        if (teacher != null) {
            teachers.add(teacher);
        }
    }

    public void removeTeacher(User teacher) {
        if (teacher != null) {
            teachers.remove(teacher);
        }
    }

    public void enrollStudent(User student) {
        if (student != null) {
            students.add(student);
        }
    }

    public void unenrollStudent(User student) {
        if (student != null) {
            students.remove(student);
        }
    }
}
