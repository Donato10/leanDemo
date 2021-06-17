package org.lean.controller.api;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lean.controller.ControllerExceptionHandler;
import org.lean.model.Employee;
import org.lean.model.Person;
import org.lean.model.Position;
import org.lean.persistence.specification.EmployeeSearchCriteria;
import org.lean.service.EmployeeService;
import org.lean.transform.dto.PersonDTO;
import org.lean.transform.dto.employee.EmployeeDTO;
import org.lean.transform.mapper.EmployeeMapper;
import org.lean.transform.mapper.PersonMapper;
import org.lean.transform.mapper.PositionMapper;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    private static final String BASE_URL = "/employees";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EmployeeMapper employeeMapper = new EmployeeMapper(new PersonMapper());
    private final PositionMapper positionMapper = new PositionMapper(employeeMapper);
    private MockMvc mvc;

    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    void beforeEach() {
        mvc = MockMvcBuilders.standaloneSetup(
            new EmployeeController(employeeService, employeeMapper, positionMapper))
                             .setControllerAdvice(new ControllerExceptionHandler())
                             .build();
    }

    @Test
    void test_saveEmployee() throws Exception {
        //GIVEN: An employee DTO
        var positionName = "position-name";
        var name = "Homer";
        var lastName = "Simpson";
        var address = "Evergreen Av. 123";
        var city = "Springfield";
        var cellPhone = "+573001234567";
        var salary = 1_000_000L;
        var employeeDTO = EmployeeDTO.builder()
                                     .salary(salary)
                                     .position(positionName)
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

        //WHEN: saving it
        when(employeeService.saveEmployee(expectedEmployee))
            .thenReturn(Employee.builder().id(1L).build());

        ResultActions results = mvc.perform(post(BASE_URL)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(employeeDTO)));

        //THEN: the response status is CREATED and the body is the id generated for the new resource
        results
            .andExpect(status().isCreated())
            .andExpect(
                header()
                    .string("Location",
                            "http://localhost/employees/1"));
    }

    @Test
    void test_getEmployee() throws Exception {
        //GIVEN: An existing employee
        var positionName = "position-name";
        var name = "Homer";
        var lastName = "Simpson";
        var address = "Evergreen Av. 123";
        var city = "Springfield";
        var cellPhone = "+573001234567";
        var salary = 1_000_000L;
        var exitingEmployee = Employee.builder()
                                      .id(1234L)
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
        //WHEN: Getting it by ID
        when(employeeService.getEmployee(exitingEmployee.getId())).thenReturn(exitingEmployee);
        ResultActions results = mvc.perform(get(BASE_URL + "/" + exitingEmployee.getId()));

        //THEN: The result JSON object contains the expected data and names
        results.andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(content()
                              .json("{\n" +
                                    "  \"id\": 1234,\n" +
                                    "  \"salary\": 1000000,\n" +
                                    "  \"position\": null,\n" +
                                    "  \"person\": {\n" +
                                    "    \"name\": \"Homer\",\n" +
                                    "    \"lastNam2\": \"Simpson\",\n" +
                                    "    \"address\": \"Evergreen Av. 123\",\n" +
                                    "    \"cellphone\": \"+573001234567\",\n" +
                                    "    \"cityName\": \"Springfield\"\n" +
                                    "  }\n" +
                                    "}", true));
    }

    @Test
    void test_updateEmployee() throws Exception {
        //GIVEN: An employee DTO
        var existingId = 1L;
        var positionName = "position-name";
        var name = "Homer";
        var lastName = "Simpson";
        var address = "Evergreen Av. 123";
        var city = "Springfield";
        var cellPhone = "+573001234567";
        var salary = 1_000_000L;
        var employeeDTO = EmployeeDTO.builder()
                                     .salary(salary)
                                     .position(positionName)
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
                                                     .city(city)
                                                     .address(address)
                                                     .cellphone(cellPhone)
                                                     .build())
                                       .build();

        //WHEN: saving it to update an existing employee
        ResultActions results = mvc.perform(put(BASE_URL + "/" + existingId)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(employeeDTO)));

        //THEN: the response status is NO CONTENT
        verify(employeeService).updateEmployee(existingId, expectedEmployee);
        results.andExpect(status().isNoContent());
    }

    @Test
    void test_deleteEmployee() throws Exception {
        //GIVEN: An existing employee ID
        var existingId = 1234L;

        //WHEN: deleting the associated entity
        ResultActions results = mvc.perform(delete(BASE_URL + "/" + existingId));

        //THEN: the response is no content
        verify(employeeService).deleteEmployee(existingId);
        results.andExpect(status().isNoContent());
    }

    @Test
    void test_findPositionsWithSortedEmployeesBySalary() throws Exception {
        //GIVEN: Some existing employees
        var positionName = "position-name";
        var name = "Homer";
        var lastName = "Simpson";
        var address = "Evergreen Av. 123";
        var city = "Springfield";
        var cellPhone = "+573001234567";
        var salary = 1_000_000L;

        var dev = Position.builder().id(1L).name("dev").build();
        var tester = Position.builder().id(2L).name("tester").build();

        var dev1 = Employee.builder()
                           .id(1111L)
                           .salary(salary)
                           .position(tester)
                           .person(Person.builder()
                                         .name(name)
                                         .lastName(lastName)
                                         .address(address)
                                         .cellphone(cellPhone)
                                         .city(city)
                                         .build())
                           .build();
        var dev2 = Employee.builder()
                           .id(2222L)
                           .salary(salary + 2)
                           .position(Position.builder().name(positionName).build())
                           .person(Person.builder()
                                         .name(name)
                                         .lastName(lastName)
                                         .address(address)
                                         .cellphone(cellPhone)
                                         .city(city)
                                         .build())
                           .build();
        var dev3 = Employee.builder()
                           .id(3333L)
                           .salary(salary + 1)
                           .position(Position.builder().name(positionName).build())
                           .person(Person.builder()
                                         .name(name)
                                         .lastName(lastName)
                                         .address(address)
                                         .cellphone(cellPhone)
                                         .city(city)
                                         .build())
                           .build();
        var tester1 = Employee.builder()
                              .id(4444L)
                              .salary(salary - 1)
                              .position(Position.builder().name(positionName).build())
                              .person(Person.builder()
                                            .name(name)
                                            .lastName(lastName)
                                            .address(address)
                                            .cellphone(cellPhone)
                                            .city(city)
                                            .build())
                              .build();

        var tester2 = Employee.builder()
                              .id(5555L)
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
        dev.setEmployees(List.of(dev2, dev3, dev1));
        tester.setEmployees(List.of(tester2, tester1));
        var expectedSearchCriteria = EmployeeSearchCriteria.builder().name(Optional.empty()).position(Optional.empty()).build();

        //WHEN: getting them with no filters
        when(employeeService.findEmployeesSortedBy(expectedSearchCriteria, "salary"))
            .thenReturn(List.of(dev, tester));

        var results = mvc.perform(get(BASE_URL));

        //THEN: the response contains a JSON object with the expected sorted employees by salary grouped by position name
        results.andExpect(status().isOk())
               .andExpect(content().contentType("application/json"))
               .andExpect(content().json("[\n" +
                                         "  {\n" +
                                         "    \"id\": 1,\n" +
                                         "    \"name\": \"dev\",\n" +
                                         "    \"employees\": [\n" +
                                         "      {\n" +
                                         "        \"id\": 2222,\n" +
                                         "        \"salary\": 1000002,\n" +
                                         "        \"position\": null,\n" +
                                         "        \"person\": {\n" +
                                         "          \"name\": \"Homer\",\n" +
                                         "          \"lastNam2\": \"Simpson\",\n" +
                                         "          \"address\": \"Evergreen Av. 123\",\n" +
                                         "          \"cellphone\": \"+573001234567\",\n" +
                                         "          \"cityName\": \"Springfield\"\n" +
                                         "        }\n" +
                                         "      },\n" +
                                         "      {\n" +
                                         "        \"id\": 3333,\n" +
                                         "        \"salary\": 1000001,\n" +
                                         "        \"position\": null,\n" +
                                         "        \"person\": {\n" +
                                         "          \"name\": \"Homer\",\n" +
                                         "          \"lastNam2\": \"Simpson\",\n" +
                                         "          \"address\": \"Evergreen Av. 123\",\n" +
                                         "          \"cellphone\": \"+573001234567\",\n" +
                                         "          \"cityName\": \"Springfield\"\n" +
                                         "        }\n" +
                                         "      },\n" +
                                         "      {\n" +
                                         "        \"id\": 1111,\n" +
                                         "        \"salary\": 1000000,\n" +
                                         "        \"position\": null,\n" +
                                         "        \"person\": {\n" +
                                         "          \"name\": \"Homer\",\n" +
                                         "          \"lastNam2\": \"Simpson\",\n" +
                                         "          \"address\": \"Evergreen Av. 123\",\n" +
                                         "          \"cellphone\": \"+573001234567\",\n" +
                                         "          \"cityName\": \"Springfield\"\n" +
                                         "        }\n" +
                                         "      }\n" +
                                         "    ]\n" +
                                         "  },\n" +
                                         "  {\n" +
                                         "    \"id\": 2,\n" +
                                         "    \"name\": \"tester\",\n" +
                                         "    \"employees\": [\n" +
                                         "      {\n" +
                                         "        \"id\": 5555,\n" +
                                         "        \"salary\": 1000000,\n" +
                                         "        \"position\": null,\n" +
                                         "        \"person\": {\n" +
                                         "          \"name\": \"Homer\",\n" +
                                         "          \"lastNam2\": \"Simpson\",\n" +
                                         "          \"address\": \"Evergreen Av. 123\",\n" +
                                         "          \"cellphone\": \"+573001234567\",\n" +
                                         "          \"cityName\": \"Springfield\"\n" +
                                         "        }\n" +
                                         "      },\n" +
                                         "      {\n" +
                                         "        \"id\": 4444,\n" +
                                         "        \"salary\": 999999,\n" +
                                         "        \"position\": null,\n" +
                                         "        \"person\": {\n" +
                                         "          \"name\": \"Homer\",\n" +
                                         "          \"lastNam2\": \"Simpson\",\n" +
                                         "          \"address\": \"Evergreen Av. 123\",\n" +
                                         "          \"cellphone\": \"+573001234567\",\n" +
                                         "          \"cityName\": \"Springfield\"\n" +
                                         "        }\n" +
                                         "      }\n" +
                                         "    ]\n" +
                                         "  }\n" +
                                         "]", true));
    }
}