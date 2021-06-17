package org.lean.transform.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.lean.model.Employee;
import org.lean.model.Position;
import org.lean.transform.dto.employee.EmployeeDTO;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeMapper implements InboundMapper<Employee, EmployeeDTO>, OutboundMapper<EmployeeDTO, Employee> {
    private final PersonMapper personMapper;

    @Override
    public Employee toEntity(EmployeeDTO dto) {
        return Employee.builder()
                       .salary(dto.getSalary())
                       .person(personMapper.toEntity(dto.getPerson()))
                       .position(Position.builder().name(dto.getPosition()).build())
                       .build();
    }

    @Override
    public EmployeeDTO toDto(Employee employee) {
        return EmployeeDTO.builder()
                          .id(employee.getId())
                          .salary(employee.getSalary())
                          .person(personMapper.toDto(employee.getPerson()))
                          .build();
    }

    public List<EmployeeDTO> toDtos(List<Employee> entities) {
        return entities.stream()
                       .map(this::toDto)
                       .collect(Collectors.toList());
    }
}