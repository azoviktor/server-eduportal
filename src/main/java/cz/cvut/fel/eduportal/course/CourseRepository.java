package cz.cvut.fel.eduportal.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCode(String name);
    boolean existsByCode(String code);
    void deleteByCode(String code);
}
