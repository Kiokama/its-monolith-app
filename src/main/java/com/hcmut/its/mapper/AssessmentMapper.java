package com.hcmut.its.mapper;

import com.hcmut.its.dto.AssessmentDTO;
import com.hcmut.its.model.Assessment;
import org.springframework.stereotype.Component;

@Component
public class AssessmentMapper {
    public AssessmentDTO toDto(Assessment a) {
        if (a == null)
            return null;
        AssessmentDTO dto = new AssessmentDTO();
        dto.setId(a.getId());
        dto.setTitle(a.getTitle());
        dto.setDescription(a.getDescription());
        dto.setTotalPoints(a.getTotalPoints());
        return dto;
    }

    public Assessment toEntity(AssessmentDTO dto) {
        if (dto == null)
            return null;
        Assessment a = new Assessment();
        a.setId(dto.getId());
        a.setTitle(dto.getTitle());
        a.setDescription(dto.getDescription());
        a.setTotalPoints(dto.getTotalPoints());
        return a;
    }
}
