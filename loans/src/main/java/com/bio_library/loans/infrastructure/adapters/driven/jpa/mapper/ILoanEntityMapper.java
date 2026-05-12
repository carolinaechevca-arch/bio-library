package com.bio_library.loans.infrastructure.adapters.driven.jpa.mapper;

import com.bio_library.loans.domain.model.Loan;
import com.bio_library.loans.infrastructure.adapters.driven.jpa.entity.LoanEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ILoanEntityMapper {

    @Mapping(target = "id", ignore = true)
    LoanEntity toEntity(Loan loan);

    Loan toDomain(LoanEntity entity);
}
