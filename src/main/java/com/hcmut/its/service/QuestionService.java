package com.hcmut.its.service;

import com.hcmut.its.exception.AssessmentNotFoundException;
import com.hcmut.its.model.Question;
import com.hcmut.its.repository.AssessmentRepository;
import com.hcmut.its.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService implements IQuestionService {
    private final QuestionRepository questionRepository;
    private final AssessmentRepository assessmentRepository;
    private final IQuestionValidator validator;

    public QuestionService(QuestionRepository questionRepository, AssessmentRepository assessmentRepository,
            IQuestionValidator validator) {
        this.questionRepository = questionRepository;
        this.assessmentRepository = assessmentRepository;
        this.validator = validator;
    }

    @Override
    public List<Question> getQuestionsByAssessmentId(Long assessmentId) {
        if (!assessmentRepository.existsById(assessmentId)) {
            throw new AssessmentNotFoundException(assessmentId);
        }
        return questionRepository.findByAssessmentId(assessmentId);
    }

    @Override
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    @Override
    public Question createQuestion(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }
        validator.validateForCreate(question);
        return questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(Long id, Question question) {
        return questionRepository.findById(id)
                .map(existing -> {
                    existing.setContent(question.getContent());
                    existing.setPoints(question.getPoints());
                    return questionRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Question not found: " + id));
    }

    @Override
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
