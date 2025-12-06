package com.hcmut.its.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("MCQ")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MCQQuestion extends Question {
    // Lưu các lựa chọn ở dạng text (JSON hoặc phân tách bằng ký tự); có thể
    // refactor thành entity riêng nếu cần
    @Column(columnDefinition = "TEXT")
    private String options;

    @Column(nullable = false)
    private String correctAnswer;
}
