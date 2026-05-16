package com.bio_library.notification.domain.constants;

public final class DomainConstants {

    private DomainConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String BORROWED_SUBJECT = "Confirmación de préstamo - Bio Library";
    public static final String RETURNED_SUBJECT = "Devolución registrada - Bio Library";
    public static final String USAGE_WARNING_SUBJECT = "Aviso: tu licencia vence en 24 horas - Bio Library";
    public static final String USAGE_REVOKED_SUBJECT = "Licencia revocada por inactividad - Bio Library";
    public static final String LOAN_EXPIRED_SUBJECT = "Préstamo vencido - Bio Library";

    public static final String BORROWED_BODY =
            "Tu préstamo del libro con ID '%s' ha sido registrado exitosamente. Fecha: %s";
    public static final String RETURNED_BODY =
            "La devolución del libro con ID '%s' ha sido procesada correctamente. Fecha: %s";
    public static final String USAGE_WARNING_BODY =
            "Tu libro con ID '%s' lleva más de 2 días sin ser usado. " +
            "Tienes 24 horas para marcarlo como usado o perderás la licencia. Fecha: %s";
    public static final String USAGE_REVOKED_BODY =
            "Tu licencia del libro con ID '%s' fue revocada porque no lo usaste en los 3 días establecidos. Fecha: %s";
    public static final String LOAN_EXPIRED_BODY =
            "Tu préstamo del libro con ID '%s' ha expirado automáticamente por alcanzar el plazo máximo permitido. Fecha: %s";
}
