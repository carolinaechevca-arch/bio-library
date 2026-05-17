package com.bio_library.loans.application.ports.out;

public interface IUserFeignClientPort {
    StudentContactInfo getStudentContact(Long userId);
}
