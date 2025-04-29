package cz.cvut.fel.eduportal.submission.dto;

public record SubmissionCreateDTO(
        Integer assignmentId,
        String studentUsername,
        String filePath,
        String fileName,
        String fileType,
        Long fileSize
) {
}
