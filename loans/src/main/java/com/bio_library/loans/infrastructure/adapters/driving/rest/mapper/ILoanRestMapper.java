package com.bio_library.loans.infrastructure.adapters.driving.rest.mapper;

import com.bio_library.loans.domain.model.Loan;
import com.bio_library.loans.infrastructure.adapters.driving.rest.dto.response.LoanResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ILoanRestMapper {
    LoanResponse toResponse(Loan loan);
}
