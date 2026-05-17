package com.bio_library.api_gateway.infrastructure.filter;

import com.bio_library.api_gateway.domain.constants.GatewayConstants;
import com.bio_library.api_gateway.infrastructure.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtProvider jwtProvider;

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (isBlocked(path)) {
            return errorResponse(exchange, HttpStatus.FORBIDDEN, "Access denied");
        }

        if (isPublic(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(GatewayConstants.AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(GatewayConstants.BEARER_PREFIX)) {
            return errorResponse(exchange, HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(GatewayConstants.BEARER_PREFIX.length());
        if (!jwtProvider.isValid(token)) {
            return errorResponse(exchange, HttpStatus.UNAUTHORIZED, "Token expired or invalid");
        }

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(GatewayConstants.HEADER_USER_ID,    String.valueOf(jwtProvider.getId(token)))
                .header(GatewayConstants.HEADER_USER_EMAIL, jwtProvider.getEmail(token))
                .header(GatewayConstants.HEADER_USER_ROLE,  jwtProvider.getRole(token))
                .header(GatewayConstants.HEADER_USER_GPA,   String.valueOf(jwtProvider.getGpa(token)))
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private boolean isPublic(String path) {
        return GatewayConstants.PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private boolean isBlocked(String path) {
        return GatewayConstants.BLOCKED_PATHS.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> errorResponse(ServerWebExchange exchange, HttpStatus status, String message) {
        log.warn("[JWT-FILTER] {} → path={}", status, exchange.getRequest().getURI().getPath());
        byte[] body = ("{\"status\":" + status.value() + ",\"message\":\"" + message + "\"}").getBytes();
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body);
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
