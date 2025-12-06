package com.hcmut.its.mapper;

import com.hcmut.its.dto.AnswerDTO;
import com.hcmut.its.model.Answer;
import com.hcmut.its.repository.QuestionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnswerMapper implements EntityMapper<AnswerDTO, Answer> {

    private final QuestionRepository questionRepository;

    public AnswerMapper(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public AnswerDTO toDto(Answer a) {
        if (a == null)
            return null;
        AnswerDTO dto = new AnswerDTO();
        dto.setId(a.getId());
        if (a.getQuestion() != null) {
            dto.setQuestionId(a.getQuestion().getId());
        }
        dto.setAnswer(a.getAnswer());
        dto.setScore(a.getScore());
        return dto;
    }

    @Override
    public Answer toEntity(AnswerDTO dto) {
        if (dto == null)
            return null;
        Answer a = new Answer();
        a.setId(dto.getId());
        if (dto.getQuestionId() != null) {
            a.setQuestion(questionRepository.findById(dto.getQuestionId()).orElse(null));
        }
        a.setAnswer(dto.getAnswer());
        a.setScore(dto.getScore());
        return a;
    }

    @Override
    public List<AnswerDTO> toDto(List<Answer> entityList) {
        if (entityList == null)
            return null;
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<Answer> toEntity(List<AnswerDTO> dtoList) {
        if (dtoList == null)
            return null;
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
