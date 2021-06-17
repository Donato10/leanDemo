package org.lean.controller.api;

import java.util.List;
import javax.validation.Valid;
import org.lean.persistence.specification.EmployeeSearchCriteria;
import org.lean.transform.dto.PositionDTO;
import org.lean.transform.dto.employee.EmployeeDTO;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.lean.configuration.SwaggerConfiguration.TAG_EMPLOYEES;

@Tag(name = TAG_EMPLOYEES)
public interface EmployeeApiInfo {

    @Operation(summary = "Creates a new Employee. If the position provided does not exist, it will also be created.")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "Employee successfully created.",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Long.class))
                }),
            @ApiResponse(responseCode = "400", description = "Invalid input.", content = @Content)
        })
    ResponseEntity<Long> saveEmployee(@Valid @RequestBody(required = true) EmployeeDTO employee);

    @Operation(summary = "Gets an employee associated with the given ID ")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "204",
                description = "Employee successfully deleted."),
            @ApiResponse(responseCode = "404", description = "Employee not found.")
        })
    ResponseEntity<EmployeeDTO> getEmployee(@PathVariable("employee-id") Long id);

    @Operation(summary = "Updates an employee associated with the given ID with the provided data.")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "204",
                description = "Employee successfully updated."),
            @ApiResponse(responseCode = "400", description = "Invalid input."),
            @ApiResponse(responseCode = "404", description = "Employee not found.")
        })
    ResponseEntity<Void> updateEmployee(
        @PathVariable("employee-id") Long id,
        @Valid @RequestBody(required = true) EmployeeDTO employee);

    @Operation(summary = "Updates an employee associated with the given ID with the provided data.")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "204",
                description = "Employee successfully deleted."),
            @ApiResponse(responseCode = "404", description = "Employee not found.")
        })
    ResponseEntity<Void> deleteEmployee(@PathVariable("employee-id") Long id);

    @Operation(summary = "Gets all the employees in the system after applying the specified filters")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "List of existing employees returned sorted by salary in desc order")
        })
    ResponseEntity<List<PositionDTO>> findPositionsWithSortedEmployeesBySalary(
        @ParameterObject EmployeeSearchCriteria employeeSearchCriteria);
}
