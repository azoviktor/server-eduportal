package cz.cvut.fel.eduportal.assignment;

import cz.cvut.fel.eduportal.assignment.dto.AssignmentCreateDTO;
import cz.cvut.fel.eduportal.assignment.dto.AssignmentResponseDTO;
import cz.cvut.fel.eduportal.course.dto.CourseCreateDTO;
import cz.cvut.fel.eduportal.course.dto.CourseResponseDTO;
import cz.cvut.fel.eduportal.exception.NotFoundException;
import cz.cvut.fel.eduportal.submission.dto.SubmissionResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public ResponseEntity<List<AssignmentResponseDTO>> getAllAssignments() {
        List<AssignmentResponseDTO> courses = assignmentService.getAllAssignments();
        return ResponseEntity.ok(courses);
    }

    @PostMapping
    public ResponseEntity<AssignmentResponseDTO> createCourse(@Valid @RequestBody AssignmentCreateDTO assignmentDTO) {
        AssignmentResponseDTO savedAssignment = assignmentService.createAssignment(assignmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAssignment);
    }

    @GetMapping("/{assignmentId}")
    public ResponseEntity<AssignmentResponseDTO> getAssignment(@PathVariable Integer assignmentId) {
        Optional<AssignmentResponseDTO> course = assignmentService.getAssignment(assignmentId);
        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer assignmentId) throws NotFoundException {
        assignmentService.deleteAssignment(assignmentId);
        return ResponseEntity.noContent().build();
    }
}
