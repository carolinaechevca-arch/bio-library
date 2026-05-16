# Notification Microservice

Microservicio encargado de enviar notificaciones a los estudiantes ante eventos de préstamo de libros. Expone un endpoint REST interno que recibe eventos publicados por el microservicio de préstamos.

## Puerto por defecto

`8084`

## Arquitectura

Arquitectura hexagonal (Ports & Adapters).

```
notification/
└── src/main/java/com/bio_library/notification/
    ├── domain/
    │   ├── enums/
    │   │   └── LoanEventType          # Todos los eventos soportados
    │   ├── model/
    │   │   └── LoanNotification       # studentId, studentEmail, bookId, eventType, occurredAt
    │   ├── constants/
    │   │   └── DomainConstants        # Plantillas de asunto y cuerpo por evento
    │   └── service/
    │       └── LoanNotificationDomainService   # Construye asunto y cuerpo según el evento
    ├── application/
    │   ├── ports/
    │   │   ├── in/  ILoanNotificationServicePort
    │   │   └── out/ IEmailNotificationPort
    │   └── usecase/
    │       └── LoanNotificationUseCase
    └── infrastructure/
        ├── adapters/
        │   ├── driven/
        │   │   └── logger/
        │   │       └── LoggerEmailNotificationAdapter   # Simula envío de correo via log
        │   └── driving/
        │       └── rest/
        │           ├── controller/ LoanNotificationController
        │           ├── dto/request/ LoanNotificationRequest
        │           └── util/        RestConstants
        └── configuration/
            └── bean/ BeanConfiguration
```

## Eventos soportados

| Evento               | Cuándo se dispara                                          | Asunto del correo                                    |
|----------------------|------------------------------------------------------------|------------------------------------------------------|
| `BOOK_BORROWED`      | El estudiante registra un nuevo préstamo                   | Confirmación de préstamo - Bio Library               |
| `BOOK_RETURNED`      | El estudiante realiza una devolución manual                | Devolución registrada - Bio Library                  |
| `LOAN_USAGE_WARNING` | El libro lleva más de 2 días sin usarse (job diario 8:00)  | Aviso: tu licencia vence en 24 horas - Bio Library   |
| `LOAN_USAGE_REVOKED` | El libro lleva más de 3 días sin usarse (job diario 8:30)  | Licencia revocada por inactividad - Bio Library      |
| `LOAN_EXPIRED`       | El préstamo supera los 15 días activos (job diario 9:00)   | Préstamo vencido - Bio Library                       |

## Endpoint

### `POST /api/v1/notifications/loan-event`

Recibe un evento de préstamo y envía la notificación al estudiante.

**Body:**
```json
{
  "studentId": 1,
  "studentEmail": "estudiante@universidad.edu.co",
  "bookId": "abc123",
  "eventType": "BOOK_BORROWED"
}
```

**Respuesta:** `202 Accepted`

## Integración con préstamos

El microservicio de préstamos (`loans`, puerto 8083) llama a este servicio vía Feign Client de forma no bloqueante:

| Acción en loans                   | Evento enviado         |
|-----------------------------------|------------------------|
| Crear préstamo                    | `BOOK_BORROWED`        |
| Devolución manual del estudiante  | `BOOK_RETURNED`        |
| Job: libro sin usar a las 48 h    | `LOAN_USAGE_WARNING`   |
| Job: libro sin usar a las 72 h    | `LOAN_USAGE_REVOKED`   |
| Job: préstamo activo +15 días     | `LOAN_EXPIRED`         |

Si el servicio de notificaciones no está disponible, el error se registra en log sin afectar la operación principal.

## Adaptador de email actual

`LoggerEmailNotificationAdapter` imprime el correo en consola. Para producción, reemplazar con un adaptador SMTP (JavaMailSender) o proveedor externo (SendGrid, AWS SES, etc.) implementando `IEmailNotificationPort`.
