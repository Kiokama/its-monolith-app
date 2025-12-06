package com.hcmut.its.repository;

import com.hcmut.its.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudentId(String studentId);

    List<Submission> findByAssessmentId(Long assessmentId);
}
