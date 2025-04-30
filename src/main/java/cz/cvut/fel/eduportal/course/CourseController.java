package cz.cvut.fel.eduportal.course;

import cz.cvut.fel.eduportal.course.dto.CourseCreateDTO;
import cz.cvut.fel.eduportal.course.dto.CourseResponseDTO;
import cz.cvut.fel.eduportal.exception.student.StudentAlreadyEnrolledException;
import cz.cvut.fel.eduportal.exception.NotFoundException;
import cz.cvut.fel.eduportal.exception.teacher.NotATeacherException;
import cz.cvut.fel.eduportal.exception.teacher.TeacherAlreadyAssignedException;
import cz.cvut.fel.eduportal.exception.teacher.TeacherNotAssignedException;
import cz.cvut.fel.eduportal.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        List<CourseResponseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseCreateDTO course) {
        CourseResponseDTO savedCourse = courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }

    @GetMapping("/{courseCode}")
    public ResponseEntity<CourseResponseDTO> getCourse(@PathVariable String courseCode) {
        Optional<CourseResponseDTO> course = courseService.getCourseByCode(courseCode);
        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{courseCode}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String courseCode) throws NotFoundException {
        courseService.deleteCourse(courseCode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{courseCode}/students")
    public ResponseEntity<List<UserResponseDTO>> getStudents(@PathVariable String courseCode) throws NotFoundException {
        List<UserResponseDTO> students = courseService.getStudents(courseCode);
        return ResponseEntity.ok(students);
    }

    @PostMapping("/{courseCode}/students")
    public ResponseEntity<Void> enrollStudent(@PathVariable String courseCode, @RequestParam String username) throws NotFoundException, StudentAlreadyEnrolledException {
        courseService.enrollStudent(courseCode, username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{courseCode}/teachers")
    public ResponseEntity<List<UserResponseDTO>> getTeachers(@PathVariable String courseCode) throws NotFoundException {
        List<UserResponseDTO> teachers = courseService.getTeachers(courseCode);
        return ResponseEntity.ok(teachers);
    }

    @PostMapping("/{courseCode}/teachers")
    public ResponseEntity<Void> assignTeacher(@PathVariable String courseCode, @RequestParam String username) throws NotFoundException, NotATeacherException, TeacherAlreadyAssignedException {
        courseService.assignTeacher(courseCode, username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{courseCode}/students/{username}")
    public ResponseEntity<Void> unenrollStudent(@PathVariable String courseCode, @PathVariable String username) throws NotFoundException {
        courseService.unenrollStudent(courseCode, username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{courseCode}/teachers/{username}")
    public ResponseEntity<Void> unassignTeacher(@PathVariable String courseCode, @PathVariable String username) throws NotFoundException, TeacherNotAssignedException {
        courseService.unassignTeacher(courseCode, username);
        return ResponseEntity.noContent().build();
    }
}
