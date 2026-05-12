package com.bio_library.catalog.infrastructure.configuration.openapi;

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
                        .title("BIO LIBRARY - Catalog API")
                        .description("Book catalog service: paginated queries and loan count management")
                        .version("1.0.0"));
    }
}
