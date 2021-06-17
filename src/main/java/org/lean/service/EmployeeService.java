package org.lean.service;

import java.util.List;
import org.lean.model.Employee;
import org.lean.model.Position;
import org.lean.persistence.specification.EmployeeSearchCriteria;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    void updateEmployee(Long id, Employee employee);

    void deleteEmployee(Long employeeId);

    Employee getEmployee(Long employeeId);

    List<Position> findEmployeesSortedBy(EmployeeSearchCriteria searchCriteria, String sortBy);
}
