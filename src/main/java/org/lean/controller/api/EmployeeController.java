package org.lean.controller.api;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.lean.model.Employee;
import org.lean.persistence.specification.EmployeeSearchCriteria;
import org.lean.service.EmployeeService;
import org.lean.transform.dto.PositionDTO;
import org.lean.transform.dto.employee.EmployeeDTO;
import org.lean.transform.mapper.EmployeeMapper;
import org.lean.transform.mapper.PositionMapper;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApiInfo {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final PositionMapper positionMapper;

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> saveEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        Employee createdEmployee = employeeService.saveEmployee(employeeMapper.toEntity(employeeDTO));
        return ResponseEntity.created(getEmployeeLocation(createdEmployee))
                             .body(createdEmployee.getId());
    }

    @Override
    @GetMapping(value = "{employee-id}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable("employee-id") Long id) {
        Employee retrievedEmployee = employeeService.getEmployee(id);
        return ResponseEntity.ok(employeeMapper.toDto(retrievedEmployee));
    }

    @Override
    @PutMapping(value = "{employee-id}",
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateEmployee(
        @PathVariable("employee-id") Long id,
        @Valid @RequestBody EmployeeDTO employeeDTO)
    {
        employeeService.updateEmployee(id, employeeMapper.toEntity(employeeDTO));
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping(value = "{employee-id}")
    public ResponseEntity<Void> deleteEmployee(Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PositionDTO>> findPositionsWithSortedEmployeesBySalary(
        @ParameterObject EmployeeSearchCriteria employeeSearchCriteria)
    {
        return ResponseEntity.ok(
            positionMapper.toDtos(employeeService.findEmployeesSortedBy(employeeSearchCriteria, "salary")));
    }

    private URI getEmployeeLocation(Employee employee) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .path("/employees")
                                          .path("/{id}")
                                          .buildAndExpand(employee.getId())
                                          .toUri();
    }
}
