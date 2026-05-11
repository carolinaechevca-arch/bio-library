package com.bio_library.user.infrastructure.adapters.driven.feign.adapter;

import com.bio_library.user.application.ports.out.IUniversityFeignClientPort;
import com.bio_library.user.domain.constants.DomainConstants;
import com.bio_library.user.domain.exceptions.UniversityStudentNotFoundException;
import com.bio_library.user.domain.model.UniversityStudentData;
import com.bio_library.user.infrastructure.adapters.driven.feign.IUniversityFeignClient;
import com.bio_library.user.infrastructure.adapters.driven.feign.dto.UniversityStudentFeignResponse;
import com.bio_library.user.infrastructure.adapters.driven.feign.mapper.IUniversityFeignMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UniversityFeignClientAdapter implements IUniversityFeignClientPort {

    private final IUniversityFeignClient feignClient;
    private final IUniversityFeignMapper mapper;

    @Override
    public UniversityStudentData getStudentByCarnetAndUniversity(String carnet, String university) {
        log.info("[FEIGN] Querying university system: carnet={}, university={}", carnet, university);
        try {
            UniversityStudentFeignResponse response = feignClient.getStudent(carnet, university);
            log.info("[FEIGN] Student found in university system: carnet={}", carnet);
            return mapper.toDomain(response);
        } catch (FeignException.NotFound e) {
            log.warn("[FEIGN] Carnet not found in university system: carnet={}", carnet);
            throw new UniversityStudentNotFoundException(
                    String.format(DomainConstants.UNIVERSITY_STUDENT_NOT_FOUND, carnet));
        }
    }
}
