package com.bio_library.loans.infrastructure.adapters.driven.feign;

import com.bio_library.loans.infrastructure.adapters.driven.feign.dto.StudentEmailFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user", url = "${user.url}")
public interface IUserFeignClient {

    @GetMapping("/api/v1/internal/students/{userId}/email")
    StudentEmailFeignResponse getStudentEmail(@PathVariable Long userId);
}
