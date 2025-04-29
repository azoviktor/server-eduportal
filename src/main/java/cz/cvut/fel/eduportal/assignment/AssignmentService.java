package cz.cvut.fel.eduportal.assignment;

import cz.cvut.fel.eduportal.assignment.dto.AssignmentCreateDTO;
import cz.cvut.fel.eduportal.assignment.dto.AssignmentResponseDTO;
import cz.cvut.fel.eduportal.course.Course;
import cz.cvut.fel.eduportal.course.CourseRepository;
import cz.cvut.fel.eduportal.exception.AlreadyExistsException;
import cz.cvut.fel.eduportal.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;

    public AssignmentService(AssignmentRepository assignmentRepository, CourseRepository courseRepository) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
    }

    public AssignmentResponseDTO toResponseDTO(Assignment assignment) {
        if (assignment == null) {
            return null;
        }
        return new AssignmentResponseDTO(
                assignment.getId(),
                assignment.getCode(),
                assignment.getTitle(),
                assignment.getType(),
                assignment.getDeadline(),
                assignment.getCourseCode()
        );
    }

    public List<AssignmentResponseDTO> getAllAssignments() {
        List<Assignment> assignments = assignmentRepository.findAll();
        return assignments.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Optional<AssignmentResponseDTO> getAssignment(Integer id) {
        Optional<Assignment> assignment = assignmentRepository.findById(id);
        return assignment.map(this::toResponseDTO);
    }

    public Assignment toEntity(AssignmentCreateDTO assignmentDTO) throws NotFoundException {
        Assignment assignment = new Assignment();
        assignment.setCode(assignmentDTO.code());
        assignment.setTitle(assignmentDTO.title());
        assignment.setType(assignmentDTO.type());
        assignment.setDeadline(assignmentDTO.deadline());
        Optional<Course> course = courseRepository.findByCode(assignmentDTO.courseCode());
        if (course.isEmpty()) {
            throw new NotFoundException("Course with code " + assignmentDTO.courseCode() + " not found");
        }
        assignment.setCourse(course.get());
        return assignment;
    }

    @Transactional
    public AssignmentResponseDTO createAssignment(AssignmentCreateDTO assignmentDTO) throws NotFoundException {
        if (assignmentRepository.existsByCodeAndCourse_Code(assignmentDTO.code(), assignmentDTO.courseCode())) {
            throw new AlreadyExistsException("Assignment with code " + assignmentDTO.code() + " already exists in course " + assignmentDTO.courseCode());
        }
        Assignment assignment = toEntity(assignmentDTO);
        assignmentRepository.save(assignment);
        return toResponseDTO(assignment);
    }

    @Transactional
    public void deleteAssignment(Integer id) throws NotFoundException {
        if (!assignmentRepository.existsById(id)) {
            throw new NotFoundException("Assignment with id " + id + " not found");
        }
        assignmentRepository.deleteById(id);
    }
}
