package com.bio_library.user.infrastructure.adapters.driving.rest.controller;

import com.bio_library.user.application.ports.in.IAuthServicePort;
import com.bio_library.user.domain.model.AuthResponseModel;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.request.AuthRequestDto;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.response.AuthResponseDto;
import com.bio_library.user.infrastructure.adapters.driving.rest.dto.response.ExceptionResponse;
import com.bio_library.user.infrastructure.adapters.driving.rest.mapper.IAuthDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

    private final IAuthServicePort authServicePort;
    private final IAuthDtoMapper authDtoMapper;

    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticates a user using email and password, and returns a JWT access token if credentials are valid."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public ResponseEntity<AuthResponseDto> login(
            @RequestBody(
                    description = "User credentials for authentication",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AuthRequestDto.class))
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody AuthRequestDto authRequestDto) {

        log.info("[AUTH][LOGIN] Incoming login request for email: {}", authRequestDto.email());

        var authModel = authDtoMapper.toModel(authRequestDto);
        log.debug("[AUTH][LOGIN] AuthRequestDto mapped to domain model for email: {}", authRequestDto.email());

        AuthResponseModel responseModel = authServicePort.authenticate(authModel);
        log.info("[AUTH][LOGIN-SUCCESS] Authentication successful for email: {}", authRequestDto.email());

        AuthResponseDto responseDto = authDtoMapper.toAuthResponseDto(responseModel);
        log.debug("[AUTH][LOGIN] Response mapped successfully for email: {}", authRequestDto.email());

        return ResponseEntity.ok(responseDto);
    }
}