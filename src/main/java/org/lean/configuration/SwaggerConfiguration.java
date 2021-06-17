package org.lean.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SwaggerConfiguration {
    public static final String TAG_EMPLOYEES = "Employees";
    public static final String TAG_EMPLOYEES_DESC =
        "Endpoint for managing employees. An employee is a person who holds a position in a the company";

    private Info apiInfo() {
        return new Info()
                   .title("Lean demo API")
                   .description("Lean demo OpenAPI definition")
                   .version("1.0.0");
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                   .components(new Components())
                   .info(apiInfo())
                   .addTagsItem(new Tag().name(TAG_EMPLOYEES).description(TAG_EMPLOYEES_DESC));
    }
}
