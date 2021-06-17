package org.lean.service.impl;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lean.exception.ResourceNotFoundException;
import org.lean.model.Employee;
import org.lean.model.Position;
import org.lean.persistence.EmployeeRepository;
import org.lean.persistence.specification.EmployeeSearchCriteria;
import org.lean.persistence.specification.EmployeeSpecification;
import org.lean.service.EmployeeService;
import org.lean.service.PositionService;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    PositionService positionService;

    EmployeeService employeeService;

    @BeforeEach
    void setup() {
        employeeService = new EmployeeServiceImpl(employeeRepository, positionService);
    }

    @Test
    void test_saveEmployee() {
        //GIVEN: An employee to be saved with an existing position name
        var positionName = "dev";
        var unsavedEmployee = Employee.builder().position(Position.builder().name(positionName).build()).build();
        var existingPosition = Position.builder().name(positionName).id(1L).build();

        //WHEN: It is requested to be saved
        when(positionService.findPosition(positionName)).thenReturn(existingPosition);
        employeeService.saveEmployee(unsavedEmployee);

        //THEN: The existing position is associated and the employee is saved
        var expectedEmployee = Employee.builder().position(existingPosition).build();
        verify(employeeRepository).save(expectedEmployee);
    }

    @Test
    void test_updateEmployee_existingId() {
        //GIVEN: An existing employee
        var existingId = 1234L;
        var existingEmployee = Employee.builder()
                                       .id(existingId)
                                       .build();
        when(employeeRepository.findById(existingId)).thenReturn(Optional.of(existingEmployee));

        //WHEN: updating it
        var newEmployee = Employee.builder()
                                  .id(existingId)
                                  .position(Position
                                                .builder()
                                                .name("new position")
                                                .build())
                                  .build();
        employeeService.updateEmployee(existingId, newEmployee);

        //THEN: the repository is requested to save the new instance
        verify(employeeRepository).save(newEmployee);
    }

    @Test
    void test_updateEmployee_nonExistingId() {
        //GIVEN: A non-existing employee ID
        var nonExistingId = 1234L;
        when(employeeRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        //WHEN: trying to update an employee using it
        //THEN: A resourceNotFoundException is thrown
        assertThrows(ResourceNotFoundException.class,
                     () -> employeeService.updateEmployee(nonExistingId, Employee.builder().build()));
    }

    @Test
    void test_deleteEmployee_existingId() {
        //GIVEN: An existing employee
        var existingId = 1234L;
        var existingEmployee = Employee.builder()
                                       .id(existingId)
                                       .build();
        when(employeeRepository.findById(existingId)).thenReturn(Optional.of(existingEmployee));

        //WHEN: deleting it using its id
        employeeService.deleteEmployee(existingId);

        //THEN: the repository is requested to delete the existing instance
        verify(employeeRepository).delete(existingEmployee);
    }

    @Test
    void test_deleteEmployee_nonExistingId() {
        //GIVEN: A non-existing employee ID
        var nonExistingId = 1234L;
        when(employeeRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        //WHEN: trying to delete an employee using it
        //THEN: A resourceNotFoundException is thrown
        assertThrows(ResourceNotFoundException.class,
                     () -> employeeService.deleteEmployee(nonExistingId));
    }

    @Test
    void findEmployeesSortedBy_withNoFilters() {
        //GIVEN: some existing employees
        var devPosition = Position.builder().name("dev").id(1L).build();
        var testerPosition = Position.builder().name("tester").id(2L).build();

        var dev_1 = Employee.builder().salary(1_000_000L).position(devPosition).build();
        var dev_2 = Employee.builder().salary(2_000_000L).position(devPosition).build();
        var dev_3 = Employee.builder().salary(1_500_000L).position(devPosition).build();
        var tester_1 = Employee.builder().salary(1_400_000L).position(testerPosition).build();
        var tester_2 = Employee.builder().salary(1_801_000L).position(testerPosition).build();

        //AND: a filtering specification
        var searchCriteria = EmployeeSearchCriteria.builder()
                                                   .name(Optional.empty())
                                                   .position(Optional.empty())
                                                   .build();
        var sortBy = "salary";

        //WHEN: finding the employees sorted by a given field
        when(employeeRepository.findAll(new EmployeeSpecification(searchCriteria, sortBy)))
            .thenReturn(List.of(tester_2, dev_3, tester_1, dev_2, dev_1));
        List<Position> resultingEmployees = employeeService.findEmployeesSortedBy(searchCriteria, sortBy);

        //THEN: the resulting employees are grouped as positions and ordered by salary
        List<Position> expectedEmployees = List.of(Position.builder()
                                                           .id(1L)
                                                           .name("dev")
                                                           .employees(List.of(dev_3, dev_2, dev_1))
                                                           .build(),
                                                   Position.builder()
                                                           .id(2L)
                                                           .name("tester")
                                                           .employees(List.of(tester_2, tester_1))
                                                           .build());

        assertEquals(expectedEmployees, resultingEmployees);
        assertEquals(expectedEmployees.get(0).getEmployees(), resultingEmployees.get(0).getEmployees());
        assertEquals(expectedEmployees.get(1).getEmployees(), resultingEmployees.get(1).getEmployees());
    }
}