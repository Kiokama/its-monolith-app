package com.hcmut.its.service;

import com.hcmut.its.exception.AssessmentNotFoundException;
import com.hcmut.its.model.Assessment;
import com.hcmut.its.model.Submission;
import com.hcmut.its.repository.AssessmentRepository;
import com.hcmut.its.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AssessmentService implements IAssessmentService {
    private final AssessmentRepository assessmentRepository;
    private final SubmissionRepository submissionRepository;
    private final IAssessmentValidator validator;
    private final IAutoGrader autoGrader;

    // Constructor injection
    public AssessmentService(
            AssessmentRepository assessmentRepository,
            SubmissionRepository submissionRepository,
            IAssessmentValidator validator,
            IAutoGrader autoGrader) {
        this.assessmentRepository = assessmentRepository;
        this.submissionRepository = submissionRepository;
        this.validator = validator;
        this.autoGrader = autoGrader;
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
        validator.validateForCreate(assessment);
        return assessmentRepository.save(assessment);
    }

    @Override
    public Assessment updateAssessment(Long id, Assessment assessment) {
        validator.validateForUpdate(id, assessment);
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

    @Override
    public Submission createSubmission(Submission submission) {
        if (submission.getAnswers() != null) {
            submission.getAnswers().forEach(answer -> answer.setSubmission(submission));
        }
        // Auto-grade if answers exist
        Integer totalScore = autoGrader.calculateScore(submission);
        submission.setScore(totalScore);
        return submissionRepository.save(submission);
    }
}
