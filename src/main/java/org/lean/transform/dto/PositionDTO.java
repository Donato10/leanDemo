package org.lean.transform.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import org.lean.transform.dto.employee.EmployeeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PositionDTO {

    @NotBlank
    @Positive
    @Schema(description = "id of the position", example = "1")
    Long id;

    @NotBlank
    @Schema(description = "Name of the position", example = "dev")
    String name;

    @Schema(description = "List of all the employees associated to the current position (it can be filtered)")
    List<EmployeeDTO> employees;
}
