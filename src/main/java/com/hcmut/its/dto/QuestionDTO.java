package com.hcmut.its.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    @NotNull(message = "Points cannot be null")
    @Positive(message = "Points must be positive")
    private Integer points;

    private Long assessmentId;

    private String questionType; // "MCQ" or "ESSAY"
}
