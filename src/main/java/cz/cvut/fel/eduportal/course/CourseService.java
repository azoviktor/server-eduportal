package cz.cvut.fel.eduportal.course;

import cz.cvut.fel.eduportal.course.dto.CourseCreateDTO;
import cz.cvut.fel.eduportal.course.dto.CourseResponseDTO;
import cz.cvut.fel.eduportal.exception.AlreadyExistsException;
import cz.cvut.fel.eduportal.exception.NotFoundException;
import cz.cvut.fel.eduportal.user.User;
import cz.cvut.fel.eduportal.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
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
                course.getTeachersUsernames()
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

    public Course toEntity(CourseCreateDTO courseDTO) throws NotFoundException {
        Course course = new Course();
        course.setTitle(courseDTO.title());
        course.setCode(courseDTO.code());
        course.setDescription(courseDTO.description());
        for (String teacherUsername : courseDTO.teachersUsernames() ) {
            Optional<User> teacher = userRepository.findByUsername(teacherUsername);
            if (teacher.isEmpty()) {
                throw new NotFoundException("Teacher with username " + teacherUsername + " not found");
            }
            course.addTeacher(teacher.get());
        }
        return course;
    }

    public CourseResponseDTO createCourse(CourseCreateDTO courseDTO) throws AlreadyExistsException {
        if (courseRepository.existsByCode(courseDTO.code())) {
            throw new AlreadyExistsException("Course with code " + courseDTO.code() + " already exists");
        }
        Course course = toEntity(courseDTO);
        courseRepository.save(course);
        return toResponseDTO(course);
    }

    public void deleteCourse(String courseCode) throws NotFoundException {
        if (!courseRepository.existsByCode(courseCode)) {
            throw new NotFoundException("Course with code " + courseCode + " not found");
        }
        courseRepository.deleteByCode(courseCode);
    }
}
