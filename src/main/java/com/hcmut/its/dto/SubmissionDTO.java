package com.hcmut.its.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionDTO {
    private Long id;
    private String studentId;
    private Long assessmentId;
    private List<AnswerDTO> answers;
    private Integer score;
    private OffsetDateTime submittedAt;
}
