package com.hcmut.its.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EssayQuestionDTO extends QuestionDTO {
    private String rubric;
}
