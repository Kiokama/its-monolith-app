package com.hcmut.its.controller;

import com.hcmut.its.dto.QuestionDTO;
import com.hcmut.its.mapper.QuestionMapper;
import com.hcmut.its.model.Question;
import com.hcmut.its.service.IQuestionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
        RequestMethod.DELETE })
public class QuestionController {
    private final IQuestionService questionService;
    private final QuestionMapper questionMapper;

    public QuestionController(IQuestionService questionService, QuestionMapper questionMapper) {
        this.questionService = questionService;
        this.questionMapper = questionMapper;
    }

    @GetMapping("/assessment/{assessmentId}")
    public List<QuestionDTO> getQuestionsByAssessmentId(@PathVariable Long assessmentId) {
        return questionMapper.toDto(questionService.getQuestionsByAssessmentId(assessmentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id)
                .map(questionMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Question DTO cannot be null");
        }
        Question toCreate = questionMapper.toEntity(dto);
        if (toCreate == null) {
            throw new IllegalArgumentException("Unable to convert DTO to entity - check questionType");
        }
        Question created = questionService.createQuestion(toCreate);
        return ResponseEntity.ok(questionMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id, @Valid @RequestBody QuestionDTO dto) {
        Question toUpdate = questionMapper.toEntity(dto);
        Question updated = questionService.updateQuestion(id, toUpdate);
        return ResponseEntity.ok(questionMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
