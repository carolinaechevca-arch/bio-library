package com.bio_library.api_gateway.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

    @Value("${services.user}")
    private String userUrl;

    @Value("${services.catalog}")
    private String catalogUrl;

    @Value("${services.loans}")
    private String loansUrl;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("user-auth", r -> r
                        .path("/api/v1/auth/**")
                        .uri(userUrl))

                .route("user-students", r -> r
                        .path("/api/v1/students/**")
                        .uri(userUrl))

                .route("catalog-books", r -> r
                        .path("/api/v1/books/**")
                        .uri(catalogUrl))

                .route("loans", r -> r
                        .path("/api/v1/loans/**")
                        .uri(loansUrl))

                .build();
    }
}
