package com.hcmut.its.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AssessmentNotFoundException extends RuntimeException {
    public AssessmentNotFoundException(Long id) {
        super("Assessment not found: " + id);
    }
}