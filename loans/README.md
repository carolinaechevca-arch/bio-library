# Loans — Bio Library

## Objetivo

`loans` es el microservicio que gestiona el **ciclo de vida de los préstamos** de libros digitales en Bio Library.

Sus responsabilidades son:

- Crear un préstamo vinculando un estudiante con un libro, notificando al catálogo para actualizar la disponibilidad.
- Marcar un libro como utilizado dentro del período de préstamo.
- Permitir la **devolución manual** de un préstamo, liberando la licencia del libro.
- Aplicar la regla de negocio de **bloqueo por GPA**: un estudiante con promedio menor a 3.2 no puede tener más de 1 préstamo activo simultáneamente.

---

## Stack tecnológico

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.3 |
| Spring Security | — |
| JWT (JJWT) | 0.11.5 |
| Spring Cloud OpenFeign | 2024.0.1 |
| Spring Data JPA | — |
| PostgreSQL | — |
| MapStruct | 1.6.3 |
| Lombok | — |
| SpringDoc OpenAPI (Swagger) | 2.8.4 |
| Gradle | — |

---

## Arquitectura

El microservicio implementa **Arquitectura Hexagonal (Puertos y Adaptadores)**:

```
driving (entrada)
  └── LoanController  ──▶  ILoanServicePort (in)
                                  │
                           LoanUseCase
                                  │
              ┌───────────────────┼────────────────────┐
              │                   │                    │
   ILoanPersistencePort   ICatalogFeignClientPort    (JWT)
              │                   │
    PostgreSQL (loans)    catalog:8082 (Feign)
```

---

## Dependencias con otros micros

| Micro | Tipo | Propósito |
|---|---|---|
| `user` | JWT compartido | El token generado en `user` contiene el `id`, `role` y `gpa` del estudiante |
| `catalog` | OpenFeign | Al crear un préstamo: `PATCH /api/v1/books/{id}/loan-count` con `INCREMENT`. Al devolver: con `DECREMENT` |

---

## Reglas de negocio

| Regla | Descripción |
|---|---|
| **Bloqueo por GPA** | Si `gpa < 3.2` y el estudiante ya tiene ≥ 1 préstamo activo, se bloquea el nuevo préstamo → 422 |
| **Límite de licencias** | El catálogo impide el préstamo si el libro ya alcanzó `maxConcurrentLoans` (máx. 5) → 422 |
| **Propiedad del préstamo** | Solo el estudiante dueño del préstamo puede devolverlo → 403 |
| **Estado activo** | Un préstamo ya devuelto no puede volver a devolverse → 409 |
| **Duración** | Todo préstamo tiene una duración fija de **10 días** desde la creación |

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

| Campo | Tipo | Requerido |
|---|---|---|
| `bookId` | String | Sí |

**Curl**

```bash
curl -X POST "http://localhost:8083/api/v1/loans" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"bookId": "64a1b2c3d4e5f6a7b8c9d0e1"}'
```

**Response 201 — Préstamo creado**

```json
{
  "id": 1,
  "studentId": 42,
  "bookId": "64a1b2c3d4e5f6a7b8c9d0e1",
  "startDate": "2026-05-11T10:00:00",
  "endDate": "2026-05-21T10:00:00",
  "hasUsed": false,
  "active": true
}
```

**Response 400 — bookId faltante**

```json
{
  "message": "bookId: Book ID is required",
  "status": "BAD_REQUEST",
  "timestamp": "2026-05-11T10:00:00"
}
```

**Response 401 — Token no enviado o expirado**

```json
{
  "message": "Authentication is required to access this resource.",
  "status": "UNAUTHORIZED",
  "timestamp": "2026-05-11T10:00:00"
}
```

**Response 404 — Libro no existe en el catálogo**

```json
{
  "message": "Book with id 64a1b2c3d4e5f6a7b8c9d0e1 was not found in the catalog.",
  "status": "NOT_FOUND",
  "timestamp": "2026-05-11T10:00:00"
}
```

**Response 422 — Libro sin licencias disponibles (máx. 5 simultáneos)**

```json
{
  "message": "Book with id 64a1b2c3d4e5f6a7b8c9d0e1 has no available copies for loan.",
  "status": "UNPROCESSABLE_ENTITY",
  "timestamp": "2026-05-11T10:00:00"
}
```

**Response 422 — Estudiante bloqueado por GPA**

```json
{
  "message": "Loan blocked: students with GPA below 3.2 cannot have more than 1 active loan.",
  "status": "UNPROCESSABLE_ENTITY",
  "timestamp": "2026-05-11T10:00:00"
}
```

---

### PATCH `/{id}/mark-used`

Marca el libro como utilizado dentro del período de préstamo.

**Path Parameters**

| Parámetro | Tipo | Descripción |
|---|---|---|
| `id` | Long | ID del préstamo |

**Curl**

```bash
curl -X PATCH "http://localhost:8083/api/v1/loans/1/mark-used" \
  -H "Authorization: Bearer <token>"
```

**Response 200 — Préstamo actualizado**

```json
{
  "id": 1,
  "studentId": 42,
  "bookId": "64a1b2c3d4e5f6a7b8c9d0e1",
  "startDate": "2026-05-11T10:00:00",
  "endDate": "2026-05-21T10:00:00",
  "hasUsed": true,
  "active": true
}
```

**Response 404 — Préstamo no encontrado**

```json
{
  "message": "Loan with id 1 was not found.",
  "status": "NOT_FOUND",
  "timestamp": "2026-05-11T10:00:00"
}
```

---

### PATCH `/{id}/return`

Devuelve un libro, libera la licencia en el catálogo y cierra el préstamo. Solo el estudiante dueño del préstamo puede ejecutar esta acción.

**Path Parameters**

| Parámetro | Tipo | Descripción |
|---|---|---|
| `id` | Long | ID del préstamo |

**Curl**

```bash
curl -X PATCH "http://localhost:8083/api/v1/loans/1/return" \
  -H "Authorization: Bearer <token>"
```

**Response 200 — Préstamo cerrado**

```json
{
  "id": 1,
  "studentId": 42,
  "bookId": "64a1b2c3d4e5f6a7b8c9d0e1",
  "startDate": "2026-05-11T10:00:00",
  "endDate": "2026-05-11T14:35:00",
  "hasUsed": true,
  "active": false
}
```

> `endDate` es actualizado al momento de la devolución. `active` pasa a `false`.

**Response 403 — El préstamo no pertenece al estudiante**

```json
{
  "message": "You are not authorized to return this loan.",
  "status": "FORBIDDEN",
  "timestamp": "2026-05-11T10:00:00"
}
```

**Response 404 — Préstamo no encontrado**

```json
{
  "message": "Loan with id 1 was not found.",
  "status": "NOT_FOUND",
  "timestamp": "2026-05-11T10:00:00"
}
```

**Response 409 — El préstamo ya fue devuelto**

```json
{
  "message": "Loan with id 1 is already returned.",
  "status": "CONFLICT",
  "timestamp": "2026-05-11T10:00:00"
}
```

---

## Variables de entorno

| Variable | Default | Descripción |
|---|---|---|
| `SERVER_PORT` | `8083` | Puerto HTTP del servidor |
| `DB_HOST` | `localhost` | Host de PostgreSQL |
| `DB_PORT` | `5432` | Puerto de PostgreSQL |
| `DB_USERNAME` | `postgres` | Usuario de PostgreSQL |
| `DB_PASSWORD` | `postgres` | Contraseña de PostgreSQL |
| `DB_SCHEMA` | `loans` | Schema donde se crea la tabla `loans` |
| `JWT_SECRET` | `586B633A...` | Clave secreta para validar JWTs (debe coincidir con `user`) |
| `CATALOG_URL` | `http://localhost:8082` | URL base del microservicio `catalog` |

---

## Correr localmente

**Requisitos:** PostgreSQL disponible y microservicio `catalog` levantado.

```bash
cd bio-library/loans
./gradlew bootRun

# Con variables personalizadas
SERVER_PORT=8084 CATALOG_URL=http://catalog:8082 JWT_SECRET=mi-clave ./gradlew bootRun
```

> El `JWT_SECRET` debe ser idéntico al configurado en el micro `user` para que los tokens sean válidos.

---

## Swagger / API Docs

| Recurso | URL |
|---|---|
| Swagger UI | `http://localhost:8083/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8083/v3/api-docs` |
