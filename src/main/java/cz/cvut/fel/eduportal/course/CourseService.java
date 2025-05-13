package cz.cvut.fel.eduportal.course;

import cz.cvut.fel.eduportal.course.dto.CourseCreateDTO;
import cz.cvut.fel.eduportal.course.dto.CourseResponseDTO;
import cz.cvut.fel.eduportal.exception.AlreadyExistsException;
import cz.cvut.fel.eduportal.exception.teacher.NotATeacherException;
import cz.cvut.fel.eduportal.exception.NotFoundException;
import cz.cvut.fel.eduportal.exception.student.StudentAlreadyEnrolledException;
import cz.cvut.fel.eduportal.exception.teacher.TeacherAlreadyAssignedException;
import cz.cvut.fel.eduportal.exception.teacher.TeacherNotAssignedException;
import cz.cvut.fel.eduportal.user.User;
import cz.cvut.fel.eduportal.user.UserConverter;
import cz.cvut.fel.eduportal.user.UserRepository;
import cz.cvut.fel.eduportal.user.UserService;
import cz.cvut.fel.eduportal.user.dto.UserResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserService userService;

    public CourseService(CourseRepository courseRepository, UserService userService) {
        this.courseRepository = courseRepository;
        this.userService = userService;
    }

    public CourseResponseDTO toResponseDTO(Course course) {
        if (course == null) {
            return null;
        }
        return new CourseResponseDTO(
                course.getId(),
                course.getTitle(),
                course.getCode(),
                course.getStudentsUsernames(),
                course.getTeachersUsernames(),
                course.getAssignmentsCodes()
        );
    }

    public List<CourseResponseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Optional<CourseResponseDTO> getCourseByCode(String courseCode) {
        Optional<Course> course = courseRepository.findByCode(courseCode);
        return course.map(this::toResponseDTO);
    }

    public Course getCourseByCodeOrThrow(String courseCode) throws NotFoundException {
        return courseRepository.findByCode(courseCode)
                .orElseThrow(() -> new NotFoundException("Course with code " + courseCode + " not found"));
    }

    public Course toEntity(CourseCreateDTO courseDTO) throws NotFoundException {
        Course course = new Course();
        course.setTitle(courseDTO.title());
        course.setCode(courseDTO.code());
        for (String teacherUsername : courseDTO.teachersUsernames() ) {
            User teacher = userService.getUserByUsernameOrThrow(teacherUsername);
            course.addTeacher(teacher);
        }
        return course;
    }

    @Transactional
    public CourseResponseDTO createCourse(CourseCreateDTO courseDTO) throws AlreadyExistsException, NotFoundException {
        if (courseRepository.existsByCode(courseDTO.code())) {
            throw new AlreadyExistsException("Course with code " + courseDTO.code() + " already exists");
        }
        Course course = toEntity(courseDTO);
        courseRepository.save(course);
        return toResponseDTO(course);
    }

    @Transactional
    public void deleteCourse(String courseCode) throws NotFoundException {
        Course course = getCourseByCodeOrThrow(courseCode);
        courseRepository.delete(course);
    }

    public List<UserResponseDTO> getStudents(String courseCode) throws NotFoundException {
        Course course = getCourseByCodeOrThrow(courseCode);
        Set<User> students = course.getStudents();
        return students.stream()
                .map(UserConverter::toResponseDTO)
                .toList();

    }

    @Transactional
    public void enrollStudent(String courseCode, String username) throws NotFoundException, StudentAlreadyEnrolledException {
        Course course = getCourseByCodeOrThrow(courseCode);
        User student = userService.getUserByUsernameOrThrow(username);
        if (course.getStudentsUsernames().contains(username)) {
            throw new StudentAlreadyEnrolledException(username, courseCode);
        }
        course.enrollStudent(student);
    }

    @Transactional
    public void unenrollStudent(String courseCode, String username) throws NotFoundException {
        Course course = getCourseByCodeOrThrow(courseCode);
        User student = userService.getUserByUsernameOrThrow(username);
        if (!course.getStudentsUsernames().contains(username)) {
            throw new StudentAlreadyEnrolledException(username, courseCode);
        }
        course.unenrollStudent(student);
    }

    public List<UserResponseDTO> getTeachers(String courseCode) throws NotFoundException {
        Course course = getCourseByCodeOrThrow(courseCode);
        Set<User> teachers = course.getTeachers();
        return teachers.stream()
                .map(UserConverter::toResponseDTO)
                .toList();
    }

    @Transactional
    public void assignTeacher(String courseCode, String username) throws NotFoundException, NotATeacherException, TeacherAlreadyAssignedException {
        Course course = getCourseByCodeOrThrow(courseCode);
        User teacher = userService.getTeacherByUsernameOrThrow(username);
        if (course.getTeachers().contains(teacher)) {
            throw new TeacherAlreadyAssignedException(username, courseCode);
        }
        course.addTeacher(teacher);
    }

    @Transactional
    public void unassignTeacher(String courseCode, String username) throws NotFoundException, TeacherNotAssignedException {
        Course course = getCourseByCodeOrThrow(courseCode);
        if (!course.getTeachersUsernames().contains(username)) {
            throw new TeacherNotAssignedException(username, courseCode);
        }
        User teacher = userService.getUserByUsernameOrThrow(username);
        course.removeTeacher(teacher);
    }
}
