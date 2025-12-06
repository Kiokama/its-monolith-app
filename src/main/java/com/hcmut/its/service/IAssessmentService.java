package com.hcmut.its.service;

import com.hcmut.its.model.Assessment;
import java.util.List;
import java.util.Optional;

public interface IAssessmentService {
    List<Assessment> getAllAssessments();

    Optional<Assessment> getAssessmentById(Long id);

    Assessment createAssessment(Assessment assessment);

    Assessment updateAssessment(Long id, Assessment assessment);

    void deleteAssessment(Long id);
}
