package com.hcmut.its.controller;

import com.hcmut.its.dto.AssessmentDTO;
import com.hcmut.its.dto.SubmissionDTO;
import com.hcmut.its.mapper.AssessmentMapper;
import com.hcmut.its.mapper.SubmissionMapper;
import com.hcmut.its.model.Assessment;
import com.hcmut.its.model.Submission;
import com.hcmut.its.service.IAssessmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assessments")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
public class AssessmentController {
    private final IAssessmentService assessmentService;
    private final AssessmentMapper assessmentMapper;
    private final SubmissionMapper submissionMapper;

    // Constructor injection (Dependency Inversion)
    public AssessmentController(IAssessmentService assessmentService, AssessmentMapper assessmentMapper,
            SubmissionMapper submissionMapper) {
        this.assessmentService = assessmentService;
        this.assessmentMapper = assessmentMapper;
        this.submissionMapper = submissionMapper;
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
    public ResponseEntity<AssessmentDTO> createAssessment(@Valid @RequestBody AssessmentDTO dto) {
        Assessment toCreate = assessmentMapper.toEntity(dto);
        Assessment created = assessmentService.createAssessment(toCreate);
        return ResponseEntity.ok(assessmentMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssessmentDTO> updateAssessment(@PathVariable Long id,
            @Valid @RequestBody AssessmentDTO dto) {
        Assessment toUpdate = assessmentMapper.toEntity(dto);
        Assessment updated = assessmentService.updateAssessment(id, toUpdate);
        return ResponseEntity.ok(assessmentMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        assessmentService.deleteAssessment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/submissions")
    public ResponseEntity<SubmissionDTO> createSubmission(@PathVariable Long id, @RequestBody SubmissionDTO dto) {
        dto.setAssessmentId(id);
        Submission toCreate = submissionMapper.toEntity(dto);
        Submission created = assessmentService.createSubmission(toCreate);
        return ResponseEntity.ok(submissionMapper.toDto(created));
    }
}
