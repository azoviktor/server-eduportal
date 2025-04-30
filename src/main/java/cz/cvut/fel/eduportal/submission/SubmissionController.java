package cz.cvut.fel.eduportal.submission;

import cz.cvut.fel.eduportal.exception.NotFoundException;
import cz.cvut.fel.eduportal.exception.student.StudentNotEnrolledException;
import cz.cvut.fel.eduportal.submission.dto.SubmissionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {
    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping
    public ResponseEntity<List<SubmissionResponseDTO>> getAllSubmissions() {
        List<SubmissionResponseDTO> submissions = submissionService.getAllSubmissions();
        return ResponseEntity.ok(submissions);
    }

    @PostMapping
    public ResponseEntity<SubmissionResponseDTO> createSubmission(
            @RequestParam("file") MultipartFile file,
            @RequestParam("assignmentId") Integer assignmentId,
            @RequestParam("studentUsername") String studentUsername
    ) throws NotFoundException, IOException, StudentNotEnrolledException {
        SubmissionResponseDTO submission = submissionService.createSubmission(file, assignmentId, studentUsername);
        return ResponseEntity.status(HttpStatus.CREATED).body(submission);
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionResponseDTO> getSubmission(@PathVariable Integer submissionId) throws NotFoundException {
        Optional<SubmissionResponseDTO> submission = submissionService.getSubmission(submissionId);
        return submission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{submissionId}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Integer submissionId) throws NotFoundException {
        submissionService.deleteSubmission(submissionId);
        return ResponseEntity.noContent().build();
    }
}
