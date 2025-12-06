package com.hcmut.its.service;

import com.hcmut.its.model.Question;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class BasicQuestionValidator implements IQuestionValidator {

    @Override
    public void validateForCreate(Question question) {
        if (question == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question payload is missing");
        }
        if (question.getContent() == null || question.getContent().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question content is required");
        }
        if (question.getPoints() == null || question.getPoints() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question points must be > 0");
        }
        if (question.getAssessment() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assessment must be specified");
        }
    }
}
