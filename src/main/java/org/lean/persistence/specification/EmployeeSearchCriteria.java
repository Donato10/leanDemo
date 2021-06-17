package org.lean.persistence.specification;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Schema(description = "search options")
@Data
@Builder
public class EmployeeSearchCriteria {
    public static final String PATH_SEPARATOR = "\\.";
    public static final Map<String, String> fieldsPathMap = Map.of("name", "name",
                                                                   "position", "position.name");

    @Parameter(
        schema = @Schema(implementation = String.class), in = ParameterIn.QUERY,
        example = "Nestor",
        description = "string to match the exact name of the employee")
    private final Optional<String> name;

    @Parameter(
        schema = @Schema(implementation = String.class), in = ParameterIn.QUERY,
        example = "dev",
        description = "string to match the exact name of the employee position")
    private final Optional<String> position;

    public Map<String, Object> getPathValues() {
        Map<String, Object> pathValues = new HashMap<>();
        this.name.ifPresent(value -> pathValues.put("person.name", value));
        this.position.ifPresent(value -> pathValues.put("position.name", value));
        return pathValues;
    }
}
