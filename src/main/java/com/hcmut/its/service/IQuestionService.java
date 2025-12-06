package com.hcmut.its.service;

import com.hcmut.its.model.Question;
import java.util.List;
import java.util.Optional;

public interface IQuestionService {
    List<Question> getQuestionsByAssessmentId(Long assessmentId);

    Optional<Question> getQuestionById(Long id);

    Question createQuestion(Question question);

    Question updateQuestion(Long id, Question question);

    void deleteQuestion(Long id);
}
