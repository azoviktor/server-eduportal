package cz.cvut.fel.eduportal.submission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
}
