package com.hcmut.its.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identifier đơn giản cho học sinh; có thể đổi sang relation với User nếu có
    @Column(nullable = false)
    private String studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    // Lưu các câu trả lời ở dạng TEXT (ví dụ JSON); có thể tách thành bảng answers
    // nếu cần
    @Column(columnDefinition = "TEXT")
    private String answers;

    private Integer score;

    private OffsetDateTime submittedAt;
}
