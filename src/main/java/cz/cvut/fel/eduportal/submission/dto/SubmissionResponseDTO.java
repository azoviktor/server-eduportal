package cz.cvut.fel.eduportal.submission.dto;

import java.time.LocalDateTime;

public record SubmissionResponseDTO(
        Integer id,
        Integer assignmentId,
        String studentUsername,
        String filePath,
        String fileName,
        String fileType,
        Long fileSize,
        LocalDateTime submissionTime,
        double grade,
        String feedback
) {
}
