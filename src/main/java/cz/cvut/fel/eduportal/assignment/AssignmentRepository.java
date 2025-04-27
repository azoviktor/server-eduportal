package cz.cvut.fel.eduportal.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    boolean existsByCodeAndCourse_Code(String code, String courseCode);
}
