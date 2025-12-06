package com.hcmut.its.service;

import com.hcmut.its.model.Assessment;

public interface IAssessmentValidator {
    void validateForCreate(Assessment assessment);

    void validateForUpdate(Long id, Assessment assessment);
}
