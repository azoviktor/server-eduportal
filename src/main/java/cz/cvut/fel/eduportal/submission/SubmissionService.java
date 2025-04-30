package cz.cvut.fel.eduportal.submission;

import cz.cvut.fel.eduportal.assignment.Assignment;
import cz.cvut.fel.eduportal.assignment.AssignmentRepository;
import cz.cvut.fel.eduportal.course.Course;
import cz.cvut.fel.eduportal.exception.NotFoundException;
import cz.cvut.fel.eduportal.exception.student.StudentNotEnrolledException;
import cz.cvut.fel.eduportal.submission.dto.SubmissionResponseDTO;
import cz.cvut.fel.eduportal.user.User;
import cz.cvut.fel.eduportal.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    private final String uploadDir = "/tmp/uploads/";

    public SubmissionService(SubmissionRepository submissionRepository,
                             AssignmentRepository assignmentRepository,
                             UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
    }

    public SubmissionResponseDTO toResponseDTO(Submission submission) {
        if (submission == null) {
            return null;
        }
        return new SubmissionResponseDTO(
                submission.getId(),
                submission.getAssignmentId(),
                submission.getStudentUsername(),
                submission.getFilePath(),
                submission.getFileName(),
                submission.getFileType(),
                submission.getFileSize(),
                submission.getSubmissionTime(),
                submission.getGrade(),
                submission.getFeedback()
        );
    }

    public List<SubmissionResponseDTO> getAllSubmissions() {
        List<Submission> submissions = submissionRepository.findAll();
        return submissions.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Optional<SubmissionResponseDTO> getSubmission(Integer id) {
        Optional<Submission> submission = submissionRepository.findById(id);
        return submission.map(this::toResponseDTO);
    }

    @Transactional
    public SubmissionResponseDTO createSubmission(
            MultipartFile file,
            Integer assignmentId,
            String studentUsername
    ) throws NotFoundException, IOException, StudentNotEnrolledException {
        Submission submission = new Submission();

        Optional<Assignment> assignment = assignmentRepository.findById(assignmentId);
        if (assignment.isEmpty()) {
            throw new NotFoundException("Assignment with ID " + assignmentId + " not found");
        }
        submission.setAssignment(assignment.get());

        Optional<User> student = userRepository.findByUsername(studentUsername);
        if (student.isEmpty()) {
            throw new NotFoundException("User with username " + studentUsername + " not found");
        }
        submission.setStudent(student.get());

        Course course = assignment.get().getCourse();
        if (!student.get().getEnrolledCourses().contains(course)) {
            throw new StudentNotEnrolledException(studentUsername, course.getCode());
        }

        Path directory = Paths.get(uploadDir);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + filename);
        Files.copy(file.getInputStream(), filePath);

        submission.setFilePath(uploadDir + filename);
        submission.setFileName(filename);
        submission.setFileType(file.getContentType());
        submission.setFileSize(file.getSize());
        submission.setSubmissionTime(LocalDateTime.now());

        submissionRepository.save(submission);
        return toResponseDTO(submission);
    }

    @Transactional
    public void deleteSubmission(Integer id) throws NotFoundException {
        if (!submissionRepository.existsById(id)) {
            throw new NotFoundException("Submission with id " + id + " not found");
        }
        submissionRepository.deleteById(id);
    }
}
