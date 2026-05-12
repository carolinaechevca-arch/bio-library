package com.bio_library.catalog.infrastructure.adapters.driving.rest.dto.request;

import com.bio_library.catalog.domain.enums.LoanAction;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoanCountRequest {

    @NotNull(message = "Action is required (INCREMENT or DECREMENT)")
    private LoanAction action;
}
