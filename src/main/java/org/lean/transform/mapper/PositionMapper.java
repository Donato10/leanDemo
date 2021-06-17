package org.lean.transform.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.lean.model.Position;
import org.lean.transform.dto.PositionDTO;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PositionMapper implements OutboundMapper<PositionDTO, Position> {
    private final EmployeeMapper employeeMapper;

    @Override
    public PositionDTO toDto(Position position) {
        return PositionDTO.builder()
                          .id(position.getId())
                          .name(position.getName())
                          .employees(employeeMapper.toDtos(position.getEmployees()))
                          .build();
    }

    public List<PositionDTO> toDtos(List<Position> entities) {
        return entities.stream()
                       .map(this::toDto)
                       .collect(Collectors.toList());
    }
}
