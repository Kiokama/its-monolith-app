package com.hcmut.its.service;

import com.hcmut.its.model.MCQQuestion;
import org.springframework.stereotype.Service;

@Service
public class AutoGrader {
    public Integer gradeMCQ(MCQQuestion question, String studentAnswer) {
        if (question.getCorrectAnswer().equalsIgnoreCase(studentAnswer)) {
            return question.getPoints();
        }
        return 0;
    }
}
