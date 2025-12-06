package com.hcmut.its.mapper;

import com.hcmut.its.dto.EssayQuestionDTO;
import com.hcmut.its.dto.MCQQuestionDTO;
import com.hcmut.its.dto.QuestionDTO;
import com.hcmut.its.model.EssayQuestion;
import com.hcmut.its.model.MCQQuestion;
import com.hcmut.its.model.Question;
import com.hcmut.its.repository.AssessmentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionMapper {
    private final AssessmentRepository assessmentRepository;

    public QuestionMapper(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    public QuestionDTO toDto(Question q) {
        if (q == null)
            return null;
        if (q instanceof MCQQuestion) {
            MCQQuestion mcq = (MCQQuestion) q;
            MCQQuestionDTO dto = new MCQQuestionDTO();
            dto.setId(mcq.getId());
            dto.setContent(mcq.getContent());
            dto.setPoints(mcq.getPoints());
            dto.setQuestionType("MCQ");
            if (mcq.getAssessment() != null) {
                dto.setAssessmentId(mcq.getAssessment().getId());
            }
            dto.setOptions(mcq.getOptions());
            dto.setCorrectAnswer(mcq.getCorrectAnswer());
            return dto;
        } else if (q instanceof EssayQuestion) {
            EssayQuestion essay = (EssayQuestion) q;
            EssayQuestionDTO dto = new EssayQuestionDTO();
            dto.setId(essay.getId());
            dto.setContent(essay.getContent());
            dto.setPoints(essay.getPoints());
            dto.setQuestionType("ESSAY");
            if (essay.getAssessment() != null) {
                dto.setAssessmentId(essay.getAssessment().getId());
            }
            dto.setRubric(essay.getRubric());
            return dto;
        }
        return null;
    }

    public Question toEntity(QuestionDTO dto) {
        if (dto == null)
            return null;

        Question question = null;

        // Determine type from DTO class or questionType field
        String type = dto.getQuestionType() != null ? dto.getQuestionType() : "MCQ";

        if (dto instanceof MCQQuestionDTO || "MCQ".equals(type)) {
            MCQQuestion mcq = new MCQQuestion();
            mcq.setId(dto.getId());
            mcq.setContent(dto.getContent());
            mcq.setPoints(dto.getPoints());
            if (dto instanceof MCQQuestionDTO) {
                MCQQuestionDTO mcqDto = (MCQQuestionDTO) dto;
                mcq.setOptions(mcqDto.getOptions());
                mcq.setCorrectAnswer(mcqDto.getCorrectAnswer());
            }
            question = mcq;
        } else if (dto instanceof EssayQuestionDTO || "ESSAY".equals(type)) {
            EssayQuestion essay = new EssayQuestion();
            essay.setId(dto.getId());
            essay.setContent(dto.getContent());
            essay.setPoints(dto.getPoints());
            if (dto instanceof EssayQuestionDTO) {
                essay.setRubric(((EssayQuestionDTO) dto).getRubric());
            }
            question = essay;
        }

        // Set assessment (must not be null)
        if (question != null && dto.getAssessmentId() != null) {
            question.setAssessment(
                    assessmentRepository.findById(dto.getAssessmentId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Assessment not found: " + dto.getAssessmentId())));
        }

        return question;
    }

    public List<QuestionDTO> toDto(List<Question> questions) {
        if (questions == null)
            return null;
        return questions.stream().map(this::toDto).collect(Collectors.toList());
    }
}
