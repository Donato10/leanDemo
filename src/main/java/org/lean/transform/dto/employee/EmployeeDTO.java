package org.lean.transform.dto.employee;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import org.lean.transform.dto.PersonDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Employee information.")
public class EmployeeDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Id of the employee")
    @Positive
    Long id;

    @Positive
    @NotNull
    @Schema(description = "Employee's salary.", required = true,
        example = "1000000", minimum = "1")
    Long salary;

    @NotBlank
    @Schema(description = "Name of the employee's position.", required = true,
        example = "dev", maxLength = 255, minLength = 1, accessMode = Schema.AccessMode.WRITE_ONLY)
    @Size(max = 255)
    String position;

    @NotNull
    @Schema(description = "Employee's personal information.")
    PersonDTO person;
}
