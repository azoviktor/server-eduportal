package cz.cvut.fel.eduportal.assignment;

import cz.cvut.fel.eduportal.course.Course;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "assignments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"code", "course_id"}),
        }
)
@Data
@NoArgsConstructor
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String code;
    private String title;

    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    private AssignmentType type;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment assignment = (Assignment) o;
        return id.equals(assignment.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getCourseCode() {
        return course.getCode();
    }
}
