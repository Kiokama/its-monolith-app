package com.hcmut.its.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MCQQuestionDTO extends QuestionDTO {
    @NotEmpty(message = "Options cannot be empty")
    private List<String> options;

    @NotBlank(message = "Correct answer cannot be blank")
    private String correctAnswer;
}
