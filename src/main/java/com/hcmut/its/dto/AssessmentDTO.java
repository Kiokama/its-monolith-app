package com.hcmut.its.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentDTO {
    private Long id;
    private String title;
    private String description;
    private Integer totalPoints;
    // Nếu cần, có thể thêm counts hoặc other metadata (keeps DTO open for
    // extension)
}
