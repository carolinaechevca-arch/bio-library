package com.bio_library.user.domain.model;

import com.bio_library.user.domain.enums.Role;
import com.bio_library.user.domain.enums.University;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class User {
    Long id;
    String dni;
    String name;
    String lastName;
    String email;
    String password;
    String phoneNumber;
    Role role;
    University university;
}
