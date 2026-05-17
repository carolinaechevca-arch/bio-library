package com.bio_library.loans.infrastructure.adapters.driven.feign.adapter;

import com.bio_library.loans.application.ports.out.IUserFeignClientPort;
import com.bio_library.loans.application.ports.out.StudentContactInfo;
import com.bio_library.loans.infrastructure.adapters.driven.feign.IUserFeignClient;
import com.bio_library.loans.infrastructure.adapters.driven.feign.dto.StudentEmailFeignResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFeignClientAdapter implements IUserFeignClientPort {

    private final IUserFeignClient feignClient;

    @Override
    public StudentContactInfo getStudentContact(Long userId) {
        try {
            StudentEmailFeignResponse response = feignClient.getStudentEmail(userId);
            return new StudentContactInfo(response.email(), response.phone(), response.hasSanction());
        } catch (FeignException e) {
            log.warn("[USER-FEIGN] Could not retrieve contact for userId={}: {}", userId, e.getMessage());
            return null;
        }
    }
}
