package com.hcmut.its.service;

import com.hcmut.its.model.MCQQuestion;
import org.springframework.stereotype.Service;

@Service
public class AutoGrader implements IAutoGrader {
    @Override
    public Integer gradeMCQ(MCQQuestion question, String studentAnswer) {
        if (question == null)
            return 0;
        if (question.getCorrectAnswer() == null)
            return 0;
        if (studentAnswer == null)
            return 0;
        if (question.getCorrectAnswer().equalsIgnoreCase(studentAnswer)) {
            return question.getPoints();
        }
        return 0;
    }
}
