package com.hcmut.its.service;

import com.hcmut.its.model.Answer;
import com.hcmut.its.model.Submission;

public interface IAutoGrader {
    Integer grade(Answer answer);
    Integer calculateScore(Submission submission);
}
