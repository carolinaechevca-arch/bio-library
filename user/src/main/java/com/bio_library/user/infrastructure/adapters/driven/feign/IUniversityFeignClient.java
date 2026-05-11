package com.bio_library.user.infrastructure.adapters.driven.feign;

import com.bio_library.user.infrastructure.adapters.driven.feign.dto.UniversityStudentFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "university-mock", url = "${university.mock.url}")
public interface IUniversityFeignClient {

    @GetMapping("/api/v1/university/students/{carnet}/{university}")
    UniversityStudentFeignResponse getStudent(
            @PathVariable("carnet") String carnet,
            @PathVariable("university") String university);
}
