package com.hcmut.its.service;

import com.hcmut.its.model.Assessment;
import com.hcmut.its.repository.AssessmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AssessmentService implements IAssessmentService {
    private final AssessmentRepository assessmentRepository;

    // Constructor injection
    public AssessmentService(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    @Override
    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    @Override
    public Optional<Assessment> getAssessmentById(Long id) {
        return assessmentRepository.findById(id);
    }

    @Override
    public Assessment createAssessment(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    @Override
    public Assessment updateAssessment(Long id, Assessment assessment) {
        return assessmentRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(assessment.getTitle());
                    existing.setDescription(assessment.getDescription());
                    existing.setTotalPoints(assessment.getTotalPoints());
                    return assessmentRepository.save(existing);
                })
                .orElseThrow(() -> new AssessmentNotFoundException(id));
    }

    @Override
    public void deleteAssessment(Long id) {
        if (!assessmentRepository.existsById(id)) {
            throw new AssessmentNotFoundException(id);
        }
        assessmentRepository.deleteById(id);
    }
}
