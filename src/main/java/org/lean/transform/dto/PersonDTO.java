package org.lean.transform.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {

    @NotBlank
    @Schema(description = "Name of the employee.", minLength = 1, maxLength = 200, example = "Nestor")
    @Size(max = 200)
    private String name;

    @NotBlank
    @Schema(description = "Last name of the employee.", minLength = 1, maxLength = 200, example = "Donato")
    private String lastNam2;

    @NotBlank
    @Schema(description = "Employee's address.", minLength = 1, maxLength = 200, example = "Evergreen Av. 123")
    private String address;

    @NotBlank
    @Schema(description = "Employee's cellphone.", pattern = "^\\+(?:[0-9] ?){6,14}[0-9]$", example = "+573131234567")
    private String cellphone;

    @NotBlank
    @Schema(description = "Employee's city of residence.", example = "Medellín", maxLength = 200, minLength = 1)
    private String cityName;
}
