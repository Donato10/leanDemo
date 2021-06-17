package org.lean.transform.mapper;

import org.junit.jupiter.api.Test;
import org.lean.model.Employee;
import org.lean.model.Person;
import org.lean.model.Position;
import org.lean.transform.dto.PersonDTO;
import org.lean.transform.dto.employee.EmployeeDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeMapperTest {

    EmployeeMapper employeeMapper = new EmployeeMapper(new PersonMapper());

    @Test
    void test_toEntity() {
        //GIVEN: An employee DTO
        var positionName = "position-name";
        var name = "Homer";
        var lastName = "Simpson";
        var address = "Evergreen Av. 123";
        var city = "Springfield";
        var cellPhone = "+573001234567";
        var salary = 1_000_000L;

        var employeeDto = EmployeeDTO.builder()
                                     .position(positionName)
                                     .salary(salary)
                                     .person(PersonDTO.builder()
                                                      .name(name)
                                                      .lastNam2(lastName)
                                                      .cityName(city)
                                                      .address(address)
                                                      .cellphone(cellPhone)
                                                      .build())
                                     .build();
        var expectedEmployee = Employee.builder()
                                       .salary(salary)
                                       .position(Position.builder().name(positionName).build())
                                       .person(Person.builder()
                                                     .name(name)
                                                     .lastName(lastName)
                                                     .address(address)
                                                     .cellphone(cellPhone)
                                                     .city(city)
                                                     .build())
                                       .build();

        //WHEN: It is mapped to an employee entity
        Employee mappedEntity = employeeMapper.toEntity(employeeDto);

        //THEN: The produced mapped employee correspond to the expected one
        assertEquals(expectedEmployee, mappedEntity);
    }

    @Test
    void testToDto() {
        //GIVEN: An employee
        var positionName = "position-name";
        var name = "Homer";
        var lastName = "Simpson";
        var address = "Evergreen Av. 123";
        var city = "Springfield";
        var cellPhone = "+573001234567";
        var salary = 1_000_000L;
        var employee = Employee.builder()
                               .salary(salary)
                               .position(Position.builder().name(positionName).build())
                               .person(Person.builder()
                                             .name(name)
                                             .lastName(lastName)
                                             .address(address)
                                             .cellphone(cellPhone)
                                             .city(city)
                                             .build())
                               .build();
        var expectedDTO = EmployeeDTO.builder()
                                     .salary(salary)
                                     .person(PersonDTO.builder()
                                                      .name(name)
                                                      .lastNam2(lastName)
                                                      .cityName(city)
                                                      .address(address)
                                                      .cellphone(cellPhone)
                                                      .build())
                                     .build();

        //WHEN: It is mapped to a DTO
        EmployeeDTO mappedDTO = employeeMapper.toDto(employee);

        //THEN: the resulting DTO correspond with the expected one
        assertEquals(expectedDTO, mappedDTO);
    }
}