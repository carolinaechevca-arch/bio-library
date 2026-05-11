package com.bio_library.university_mock.infrastructure.configuration.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BIO LIBRARY - University Mock API")
                        .description("University system simulation: GPA and enrollment status queries")
                        .version("1.0.0"));
    }
}
