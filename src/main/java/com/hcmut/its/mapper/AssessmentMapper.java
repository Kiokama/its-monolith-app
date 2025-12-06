package com.hcmut.its.mapper;

import com.hcmut.its.dto.AssessmentDTO;
import com.hcmut.its.model.Assessment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssessmentMapper implements EntityMapper<AssessmentDTO, Assessment> {

    @Override
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

    @Override
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

    @Override
    public List<Assessment> toEntity(List<AssessmentDTO> dtoList) {
        if (dtoList == null)
            return null;
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<AssessmentDTO> toDto(List<Assessment> entityList) {
        if (entityList == null)
            return null;
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
