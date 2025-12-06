package com.hcmut.its.service;

import com.hcmut.its.model.Answer;
import com.hcmut.its.model.MCQQuestion;
import com.hcmut.its.model.Question;
import com.hcmut.its.model.Submission;
import org.springframework.stereotype.Service;

@Service
public class AutoGrader implements IAutoGrader {

    @Override
    public Integer grade(Answer answer) {
        if (answer == null || answer.getQuestion() == null || answer.getAnswer() == null) {
            return 0;
        }
        Question question = answer.getQuestion();
        String studentAnswer = answer.getAnswer();

        if (question instanceof MCQQuestion) {
            MCQQuestion mcq = (MCQQuestion) question;
            if (mcq.getCorrectAnswer() != null && mcq.getCorrectAnswer().equalsIgnoreCase(studentAnswer)) {
                return mcq.getPoints();
            }
        }
        // Other question types can be handled here
        return 0;
    }

    @Override
    public Integer calculateScore(Submission submission) {
        if (submission == null || submission.getAnswers() == null) {
            return 0;
        }
        return submission.getAnswers().stream()
                .mapToInt(this::grade)
                .sum();
    }
}
