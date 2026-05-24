# Loans — Bio Library

## Objetivo

`loans` es el microservicio que gestiona el **ciclo de vida de los préstamos** de libros digitales en Bio Library.

Sus responsabilidades son:

- Crear un préstamo vinculando un estudiante con un libro, notificando al catálogo para actualizar la disponibilidad.
- Marcar un libro como utilizado dentro del período de préstamo.
- Permitir la **devolución manual** de un préstamo, liberando la licencia del libro.
- Aplicar reglas de negocio: bloqueo por GPA, bloqueo por sanción activa, límite de licencias.
- Ejecutar **jobs automáticos** para gestionar préstamos vencidos e inactividad.
- Publicar **eventos a RabbitMQ** para que el micro `notification` envíe SMS al estudiante.

---

## Stack tecnológico

| Tecnología                  | Versión  |
| --------------------------- | -------- |
| Java                        | 21       |
| Spring Boot                 | 3.4.3    |
| Spring Security             | —        |
| JWT (JJWT)                  | 0.11.5   |
| Spring Cloud OpenFeign      | 2024.0.1 |
| Spring Data JPA             | —        |
| PostgreSQL                  | —        |
| Spring AMQP (RabbitMQ)      | —        |
| MapStruct                   | 1.6.3    |
| Lombok                      | —        |
| SpringDoc OpenAPI (Swagger) | 2.8.4    |
| Gradle                      | —        |

---

## Arquitectura

```
driving (entrada)
  └── LoanController  ──▶  ILoanServicePort (in)
                                  │
                           LoanUseCase
                                  │
        ┌─────────────────────────┼──────────────────────────┐
        │                         │                          │
ILoanPersistencePort   ICatalogFeignClientPort   IUserFeignClientPort
        │                         │                          │
  PostgreSQL             catalog:8082             user:8080
  (schema:loans)         (licencias)              (email/teléfono/sanción)
        │
        │              INotificationPort
        │                     │
        └─────────────▶ RabbitMQ ──▶ notification:8084

Scheduler:
  LoanScheduledJobs ──▶ ILoanSchedulerServicePort
                               │
                        LoanSchedulerUseCase
```

---

## Dependencias con otros micros

| Micro          | Tipo                | Propósito                                                                                |
| -------------- | ------------------- | ---------------------------------------------------------------------------------------- |
| `user`         | JWT compartido      | El token contiene `id`, `role` y `gpa` del estudiante                                    |
| `catalog`      | OpenFeign           | `PATCH /api/v1/books/{id}/loan-count` — actualiza licencias disponibles                  |
| `user`         | OpenFeign (interno) | `GET /api/v1/internal/students/{id}/email` — obtiene email, teléfono y estado de sanción |
| `notification` | RabbitMQ            | Publica eventos de préstamo para envío de SMS                                            |

---

## Reglas de negocio

| Regla                      | Respuesta                                                              |
| -------------------------- | ---------------------------------------------------------------------- |
| **Sanción activa**         | Si el estudiante tiene sanción activa, se bloquea el préstamo → 403    |
| **Bloqueo por GPA**        | Si `gpa < 3.2` y el estudiante ya tiene ≥ 1 préstamo activo → 422      |
| **Límite de licencias**    | El catálogo rechaza si `availableLicenses == 0` → 422                  |
| **Propiedad del préstamo** | Solo el dueño puede devolver su préstamo → 403                         |
| **Estado activo**          | Un préstamo ya devuelto no puede devolverse de nuevo → 409             |
| **Duración**               | Todo préstamo tiene una duración fija de **10 días** desde la creación |

---

## Jobs automáticos (Scheduler)

Los tres jobs se ejecutan cada **5 minutos** (configurable vía `fixedRate`).

| Job                       | Condición                                                | Acción                                   |
| ------------------------- | -------------------------------------------------------- | ---------------------------------------- |
| `processUsageWarnings`    | Préstamo activo, sin usar, entre 2 y 3 días desde inicio | Envía SMS de advertencia                 |
| `processUsageRevocations` | Préstamo activo, sin usar, con ≥ 3 días desde inicio     | Devuelve el libro automáticamente + SMS  |
| `processExpiredLoans`     | Préstamo activo con ≥ 15 días desde inicio               | Cierra el préstamo automáticamente + SMS |

---

## Eventos RabbitMQ publicados

| Evento               | Disparado en                  |
| -------------------- | ----------------------------- |
| `BOOK_BORROWED`      | `createLoan()`                |
| `BOOK_RETURNED`      | `returnLoan()`                |
| `LOAN_USAGE_WARNING` | Job `processUsageWarnings`    |
| `LOAN_USAGE_REVOKED` | Job `processUsageRevocations` |
| `LOAN_EXPIRED`       | Job `processExpiredLoans`     |

Cada evento incluye `studentId`, `studentEmail`, `studentPhone` y `bookId`.

---

## Endpoints

**Base URL:** `http://localhost:8083/api/v1/loans`

Todos los endpoints requieren autenticación JWT.
**Header:** `Authorization: Bearer <token>`

---

### POST `/`

Crea un nuevo préstamo para el estudiante autenticado.

**Request Body**

```json
{ "bookId": "64a1b2c3d4e5f6a7b8c9d0e1" }
```

**Response 201 — Préstamo creado**

```json
{
  "id": 1,
  "studentId": 42,
  "bookId": "64a1b2c3d4e5f6a7b8c9d0e1",
  "startDate": "2026-05-17",
  "endDate": "2026-05-27",
  "hasUsed": false,
  "active": true
}
```

**Respuestas de error**

| Código | Causa                         |
| ------ | ----------------------------- |
| 400    | `bookId` faltante             |
| 401    | Token ausente o expirado      |
| 403    | Estudiante con sanción activa |
| 404    | Libro no existe en catálogo   |
| 422    | Sin licencias disponibles     |
| 422    | GPA < 3.2 con préstamo activo |

**Response 403 — Estudiante sancionado**

```json
{
  "message": "Loan blocked: student has an active sanction.",
  "status": "FORBIDDEN",
  "timestamp": "2026-05-17T10:00:00"
}
```

---

### PATCH `/{id}/mark-used`

Marca el libro como utilizado dentro del período de préstamo.

**Response 200** — Préstamo con `hasUsed: true`.

**Response 404** — Préstamo no encontrado.

---

### PATCH `/{id}/return`

Devuelve un libro. Solo el estudiante dueño del préstamo puede ejecutar esta acción.

**Response 200** — Préstamo con `active: false`.

**Respuestas de error**

| Código | Causa                               |
| ------ | ----------------------------------- |
| 403    | Préstamo no pertenece al estudiante |
| 404    | Préstamo no encontrado              |
| 409    | Préstamo ya devuelto                |

---

### GET `/my-loans`

Devuelve los préstamos del estudiante autenticado (paginados).

**Query Parameters**

| Parámetro | Tipo    | Default     | Descripción                                           |
| --------- | ------- | ----------- | ----------------------------------------------------- |
| `active`  | Boolean | —           | `true` = activos, `false` = devueltos, omitir = todos |
| `page`    | Integer | `0`         | Número de página                                      |
| `size`    | Integer | `10`        | Tamaño de página                                      |
| `sortBy`  | String  | `startDate` | `startDate`, `endDate`, `bookId`                      |
| `sortDir` | String  | `desc`      | `asc` o `desc`                                        |

---

### GET `/student/{studentId}`

Devuelve los préstamos de un estudiante específico. **Solo ADMIN.**

Mismos query params que `/my-loans`.

---

## Variables de entorno

| Variable               | Default                 | Descripción                                                 |
| ---------------------- | ----------------------- | ----------------------------------------------------------- |
| `SERVER_PORT`          | `8083`                  | Puerto HTTP del servidor                                    |
| `DB_HOST`              | `localhost`             | Host de PostgreSQL                                          |
| `DB_PORT`              | `5432`                  | Puerto de PostgreSQL                                        |
| `DB_USERNAME`          | `postgres`              | Usuario de PostgreSQL                                       |
| `DB_PASSWORD`          | `postgres`              | Contraseña de PostgreSQL                                    |
| `DB_SCHEMA`            | `loans`                 | Schema donde se crea la tabla `loans`                       |
| `JWT_SECRET`           | `586B633A...`           | Clave secreta para validar JWTs (debe coincidir con `user`) |
| `CATALOG_URL`          | `http://localhost:8082` | URL base del microservicio `catalog`                        |
| `USER_URL`             | `http://localhost:8080` | URL base del microservicio `user`                           |
| `RABBITMQ_HOST`        | `localhost`             | Host de RabbitMQ                                            |
| `RABBITMQ_USER`        | `guest`                 | Usuario de RabbitMQ                                         |
| `RABBITMQ_PASS`        | `guest`                 | Contraseña de RabbitMQ                                      |
| `RABBITMQ_EXCHANGE`    | `bio.library.exchange`  | Exchange donde se publican eventos                          |
| `RABBITMQ_ROUTING_KEY` | `loan.event`            | Routing key de los eventos                                  |

---

## Swagger / API Docs

| Recurso      | URL                                     |
| ------------ | --------------------------------------- |
| Swagger UI   | `http://localhost:8083/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8083/v3/api-docs`     |

---

## Métricas

Expone `/actuator/prometheus` en el puerto `8083` para scraping con Prometheus.

---

## Patrones de diseño

### Strategy — Validación de préstamos

Cada regla de negocio está encapsulada en su propia clase que implementa la interfaz correspondiente:

**Reglas de creación (`ILoanCreationRule`):**

| Estrategia            | Orden | Regla                                                                      |
| --------------------- | ----- | -------------------------------------------------------------------------- |
| `GpaLoanCreationRule` | 1     | Si `gpa < 3.2` y el estudiante ya tiene ≥ 1 préstamo activo, bloquea → 422 |

> La validación de sanción se ejecuta antes de las reglas, directamente en el `LoanUseCase` vía Feign al user service → 403.

**Reglas de devolución (`ILoanReturnRule`):**

| Estrategia                  | Orden | Regla                                                  |
| --------------------------- | ----- | ------------------------------------------------------ |
| `LoanOwnershipReturnRule`   | 1     | Solo el estudiante dueño del préstamo puede devolverlo |
| `LoanActiveStateReturnRule` | 2     | Un préstamo ya devuelto no puede devolverse de nuevo   |

---

### Factory Method — Construcción del Loan

`LoanFactory.newLoan(studentId, bookId)` centraliza la construcción del objeto `Loan` con fechas, estado inicial (`hasUsed=false`, `active=true`) y período de 10 días.

---

### Builder — Transiciones de estado del Loan

| Método           | Transición                        |
| ---------------- | --------------------------------- |
| `withUsed()`     | `hasUsed → true`                  |
| `withReturned()` | `active → false`, `endDate → hoy` |

---

### Adapter — Conversión entre dominio e infraestructura

| Adaptador                     | Puerto                    | Tecnología                   |
| ----------------------------- | ------------------------- | ---------------------------- |
| `LoanPersistenceAdapter`      | `ILoanPersistencePort`    | Spring Data JPA + PostgreSQL |
| `CatalogFeignClientAdapter`   | `ICatalogFeignClientPort` | OpenFeign → `catalog:8082`   |
| `UserFeignClientAdapter`      | `IUserFeignClientPort`    | OpenFeign → `user:8080`      |
| `NotificationRabbitMqAdapter` | `INotificationPort`       | Spring AMQP → RabbitMQ       |
| `JwtAdapter`                  | `IJwtPort`                | JJWT 0.11.5                  |
