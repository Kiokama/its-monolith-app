package com.hcmut.its.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentDTO {
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String description;

    @NotNull(message = "Total points cannot be null")
    @Positive(message = "Total points must be positive")
    private Integer totalPoints;
    // Nếu cần, có thể thêm counts hoặc other metadata (keeps DTO open for
    // extension)
}
