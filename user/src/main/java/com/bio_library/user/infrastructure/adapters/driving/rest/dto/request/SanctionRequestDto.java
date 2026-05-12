package com.bio_library.user.infrastructure.adapters.driving.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

import static com.bio_library.user.domain.constants.StudentConstants.SANCTION_ACTIVE_REQUIRED_MESSAGE;

@Builder
public record SanctionRequestDto(

        @NotNull(message = SANCTION_ACTIVE_REQUIRED_MESSAGE)
        Boolean active,

        LocalDateTime sanctionEndDate

) {}
