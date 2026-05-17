# Notification — Bio Library

## Objetivo

`notification` es el microservicio que **consume eventos de préstamos desde RabbitMQ** y envía notificaciones SMS a los estudiantes vía **Twilio**.

No expone endpoints REST. Es un consumidor puro de mensajes asíncronos.

---

## Stack tecnológico

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.3 |
| Spring AMQP (RabbitMQ) | — |
| Twilio SDK | 10.6.6 |
| Lombok | — |
| Gradle | — |

---

## Arquitectura

```
RabbitMQ (exchange: bio.library.exchange)
  │  routing-key: loan.event
  ▼
LoanNotificationListener  ──▶  ILoanNotificationServicePort (in)
                                        │
                               LoanNotificationUseCase
                                        │
                               LoanNotificationDomainService  (construye asunto y cuerpo)
                                        │
                               ISmsNotificationPort (out)
                                        │
                          TwilioSmsNotificationAdapter  ──▶  Twilio API
```

```
notification/
└── src/main/java/com/bio_library/notification/
    ├── domain/
    │   ├── enums/    LoanEventType
    │   ├── model/    LoanNotification
    │   ├── service/  LoanNotificationDomainService
    │   └── constants/ DomainConstants
    ├── application/
    │   ├── ports/in/  ILoanNotificationServicePort
    │   ├── ports/out/ ISmsNotificationPort
    │   └── usecase/   LoanNotificationUseCase
    └── infrastructure/
        ├── adapters/driven/twilio/   TwilioSmsNotificationAdapter
        ├── adapters/driving/rabbitmq/listener/ LoanNotificationListener
        │                            dto/       LoanNotificationMessage
        └── configuration/
            ├── bean/    BeanConfiguration
            └── rabbitmq/ RabbitMqConfiguration
```

---

## Eventos soportados

| Evento | Cuándo se dispara | Mensaje SMS |
|---|---|---|
| `BOOK_BORROWED` | El estudiante registra un nuevo préstamo | Confirmación de préstamo |
| `BOOK_RETURNED` | El estudiante realiza una devolución manual | Devolución registrada |
| `LOAN_USAGE_WARNING` | Libro sin usar durante 2 días (job cada 5 min) | Aviso: licencia vence en 24 h |
| `LOAN_USAGE_REVOKED` | Libro sin usar durante 3 días (job cada 5 min) | Licencia revocada por inactividad |
| `LOAN_EXPIRED` | Préstamo activo supera 15 días (job cada 5 min) | Préstamo vencido |

---

## Mensaje RabbitMQ

El microservicio `loans` publica mensajes con este esquema:

```json
{
  "studentId": 42,
  "studentEmail": "carlos.garcia@itm.edu.co",
  "studentPhone": "+573012345678",
  "bookId": "64a1b2c3d4e5f6a7b8c9d0e1",
  "eventType": "BOOK_BORROWED"
}
```

El SMS se envía al `studentPhone`. Si el teléfono es nulo o vacío, el evento se descarta con un log de advertencia.

---

## Configuración de Twilio

Para que los SMS lleguen debes configurar las siguientes variables en `.env`:

| Variable | Descripción |
|---|---|
| `TWILIO_ACCOUNT_SID` | Account SID de tu cuenta Twilio |
| `TWILIO_AUTH_TOKEN` | Auth Token de tu cuenta Twilio |
| `TWILIO_PHONE_NUMBER` | Número Twilio remitente en formato E.164 (`+1234567890`) |

> Si las credenciales están vacías el servicio arranca correctamente pero los SMS no se envían — solo se registra una advertencia en log.

El estudiante también debe tener `phoneNumber` registrado en el sistema en formato E.164 (`+573012345678`).

---

## Variables de entorno

| Variable | Default | Descripción |
|---|---|---|
| `SERVER_PORT` | `8084` | Puerto del servidor |
| `RABBITMQ_HOST` | `localhost` | Host de RabbitMQ |
| `RABBITMQ_PORT` | `5672` | Puerto de RabbitMQ |
| `RABBITMQ_USER` | `guest` | Usuario de RabbitMQ |
| `RABBITMQ_PASS` | `guest` | Contraseña de RabbitMQ |
| `RABBITMQ_EXCHANGE` | `bio.library.exchange` | Exchange donde escucha |
| `RABBITMQ_QUEUE` | `loan.notification.queue` | Cola de mensajes |
| `RABBITMQ_ROUTING_KEY` | `loan.event` | Routing key |
| `TWILIO_ACCOUNT_SID` | — | Credencial Twilio |
| `TWILIO_AUTH_TOKEN` | — | Credencial Twilio |
| `TWILIO_PHONE_NUMBER` | — | Número remitente Twilio |

---

## Métricas

Expone el endpoint `/actuator/prometheus` en el puerto `8084` para scraping con Prometheus.
