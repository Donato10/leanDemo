package org.lean.transform.mapper;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.lean.model.Employee;
import org.lean.model.Person;
import org.lean.model.Position;
import org.lean.transform.dto.PersonDTO;
import org.lean.transform.dto.PositionDTO;
import org.lean.transform.dto.employee.EmployeeDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionMapperTest {

    PositionMapper positionMapper = new PositionMapper(new EmployeeMapper(new PersonMapper()));

    @Test
    void test_toDto() {
        //GIVEN: A position entity
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
        var position = Position.builder()
                               .name(positionName)
                               .employees(Collections.singletonList(employee))
                               .build();
        var expectedPositionDto = PositionDTO.builder()
                                             .employees(Collections.singletonList(
                                                 EmployeeDTO.builder()
                                                            .salary(salary)
                                                            .person(PersonDTO.builder()
                                                                             .name(name)
                                                                             .lastNam2(lastName)
                                                                             .cityName(city)
                                                                             .address(address)
                                                                             .cellphone(cellPhone)
                                                                             .build())
                                                            .build()))
                                             .name(positionName)
                                             .build();

        //WHEN: it is mapped to a DTO
        PositionDTO mappedDTO = positionMapper.toDto(position);

        //THEN: the resulting DTO corresponds to the expected one
        assertEquals(expectedPositionDto, mappedDTO);
    }
}