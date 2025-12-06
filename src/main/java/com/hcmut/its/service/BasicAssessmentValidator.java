package com.hcmut.its.service;

import com.hcmut.its.model.Assessment;
import com.hcmut.its.repository.AssessmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class BasicAssessmentValidator implements IAssessmentValidator {
    private final AssessmentRepository repository;

    public BasicAssessmentValidator(AssessmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validateForCreate(Assessment assessment) {
        if (assessment == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assessment payload is missing");
        }
        if (assessment.getTitle() == null || assessment.getTitle().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required");
        }
        if (assessment.getTotalPoints() == null || assessment.getTotalPoints() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Total points must be >= 0");
        }
    }

    @Override
    public void validateForUpdate(Long id, Assessment assessment) {
        if (id == null || !repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Assessment not found: " + id);
        }
        // Reuse create validations for fields
        validateForCreate(assessment);
    }
}
