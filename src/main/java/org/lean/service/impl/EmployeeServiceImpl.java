package org.lean.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.lean.exception.ResourceNotFoundException;
import org.lean.model.Employee;
import org.lean.model.Position;
import org.lean.persistence.EmployeeRepository;
import org.lean.persistence.specification.EmployeeSearchCriteria;
import org.lean.persistence.specification.EmployeeSpecification;
import org.lean.service.EmployeeService;
import org.lean.service.PositionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionService positionService;

    @Override
    @Transactional
    public Employee saveEmployee(Employee employee) {
        Position position = positionService.findPosition(employee.getPositionName());
        employee.setPosition(position);
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void updateEmployee(Long id, Employee employee) {
        getEmployee(id);
        employee.setId(id);
        saveEmployee(employee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        employeeRepository.delete(getEmployee(id));
    }

    @Override
    public List<Position> findEmployeesSortedBy(EmployeeSearchCriteria searchCriteria, String sortBy) {
        return employeeRepository.findAll(new EmployeeSpecification(searchCriteria, sortBy))
                                 .stream()
                                 .collect(groupingBy(Employee::getPosition))
                                 .entrySet().stream()
                                 .map(entry -> {
                                     entry.getKey().setEmployees(entry.getValue());
                                     return entry.getKey();
                                 }).collect(Collectors.toList());
    }

    @Override
    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Employee.class, id));
    }
}
