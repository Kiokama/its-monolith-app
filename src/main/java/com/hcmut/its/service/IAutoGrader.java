package com.hcmut.its.service;

import com.hcmut.its.model.MCQQuestion;

public interface IAutoGrader {
    Integer gradeMCQ(MCQQuestion question, String studentAnswer);
}
