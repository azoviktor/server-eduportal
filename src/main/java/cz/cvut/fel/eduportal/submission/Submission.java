package cz.cvut.fel.eduportal.submission;

import cz.cvut.fel.eduportal.assignment.Assignment;
import cz.cvut.fel.eduportal.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

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
}
