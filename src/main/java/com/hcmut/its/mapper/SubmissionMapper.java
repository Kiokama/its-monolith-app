package com.hcmut.its.mapper;

import com.hcmut.its.dto.SubmissionDTO;
import com.hcmut.its.model.Submission;
import com.hcmut.its.repository.AssessmentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubmissionMapper implements EntityMapper<SubmissionDTO, Submission> {

    private final AssessmentRepository assessmentRepository;
    private final AnswerMapper answerMapper;

    public SubmissionMapper(AssessmentRepository assessmentRepository, AnswerMapper answerMapper) {
        this.assessmentRepository = assessmentRepository;
        this.answerMapper = answerMapper;
    }

    @Override
    public SubmissionDTO toDto(Submission s) {
        if (s == null)
            return null;
        SubmissionDTO dto = new SubmissionDTO();
        dto.setId(s.getId());
        dto.setStudentId(s.getStudentId());
        if (s.getAssessment() != null) {
            dto.setAssessmentId(s.getAssessment().getId());
        }
        if (s.getAnswers() != null) {
            dto.setAnswers(s.getAnswers().stream().map(answerMapper::toDto).collect(Collectors.toList()));
        }
        dto.setScore(s.getScore());
        dto.setSubmittedAt(s.getSubmittedAt());
        return dto;
    }

    @Override
    public Submission toEntity(SubmissionDTO dto) {
        if (dto == null)
            return null;
        Submission s = new Submission();
        s.setId(dto.getId());
        s.setStudentId(dto.getStudentId());
        if (dto.getAssessmentId() != null) {
            s.setAssessment(assessmentRepository.findById(dto.getAssessmentId()).orElse(null));
        }
        if (dto.getAnswers() != null) {
            s.setAnswers(dto.getAnswers().stream().map(answerMapper::toEntity).collect(Collectors.toList()));
        }
        s.setScore(dto.getScore());
        s.setSubmittedAt(dto.getSubmittedAt());
        return s;
    }

    @Override
    public List<SubmissionDTO> toDto(List<Submission> entityList) {
        if (entityList == null)
            return null;
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<Submission> toEntity(List<SubmissionDTO> dtoList) {
        if (dtoList == null)
            return null;
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
