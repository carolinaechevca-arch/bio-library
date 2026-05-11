package com.bio_library.university_mock.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum University {
    UNIVERSIDAD_NACIONAL("unacional.edu.co"),
    UNIVERSIDAD_DE_ANTIOQUIA("udea.edu.co"),
    UNIVERSIDAD_EAFIT("eafit.edu.co"),
    UNIVERSIDAD_DE_LOS_ANDES("uniandes.edu.co"),
    UNIVERSIDAD_PONTIFICIA_BOLIVARIANA("upb.edu.co"),
    ITM("itm.edu.co"),
    PASCUAL_BRAVO("pascualbravo.edu.co"),
    COLMAYOR("colmayor.edu.co"),
    UNIREMINGTON("uniremington.edu.co"),
    UNIVERSIDAD_DE_MEDELLIN("udem.edu.co"),
    UNIVERSIDAD_CES("ces.edu.co");

    private final String emailDomain;
}
