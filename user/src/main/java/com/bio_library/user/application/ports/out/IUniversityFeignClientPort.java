package com.bio_library.user.application.ports.out;

import com.bio_library.user.domain.model.UniversityStudentData;

public interface IUniversityFeignClientPort {
    UniversityStudentData getStudentByCarnetAndUniversity(String carnet, String university);
}
