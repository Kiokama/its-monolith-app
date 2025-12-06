package com.hcmut.its.controller;

import com.hcmut.its.dto.AssessmentDTO;
import com.hcmut.its.mapper.AssessmentMapper;
import com.hcmut.its.model.Assessment;
import com.hcmut.its.service.IAssessmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {
    private final IAssessmentService assessmentService;
    private final AssessmentMapper assessmentMapper;

    // Constructor injection (Dependency Inversion)
    public AssessmentController(IAssessmentService assessmentService, AssessmentMapper assessmentMapper) {
        this.assessmentService = assessmentService;
        this.assessmentMapper = assessmentMapper;
    }

    @GetMapping
    public List<AssessmentDTO> getAllAssessments() {
        return assessmentService.getAllAssessments()
                .stream()
                .map(assessmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssessmentDTO> getAssessmentById(@PathVariable Long id) {
        return assessmentService.getAssessmentById(id)
                .map(assessmentMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AssessmentDTO> createAssessment(@RequestBody AssessmentDTO dto) {
        Assessment toCreate = assessmentMapper.toEntity(dto);
        Assessment created = assessmentService.createAssessment(toCreate);
        return ResponseEntity.ok(assessmentMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssessmentDTO> updateAssessment(@PathVariable Long id, @RequestBody AssessmentDTO dto) {
        Assessment toUpdate = assessmentMapper.toEntity(dto);
        Assessment updated = assessmentService.updateAssessment(id, toUpdate);
        return ResponseEntity.ok(assessmentMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        assessmentService.deleteAssessment(id);
        return ResponseEntity.noContent().build();
    }
}
