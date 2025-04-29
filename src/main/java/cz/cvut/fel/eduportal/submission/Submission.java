package cz.cvut.fel.eduportal.submission;

import cz.cvut.fel.eduportal.assignment.Assignment;
import cz.cvut.fel.eduportal.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Data
@NoArgsConstructor
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    private String filePath;
    private String fileName;
    private String fileType;
    private Long fileSize;

    private LocalDateTime submissionTime;

    private double grade;
    private String feedback;

    public Integer getAssignmentId() {
        return assignment.getId();
    }

    public String getStudentUsername() {
        return student.getUsername();
    }
}
