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
  "startDate": "2026-05-11",
  "endDate": "2026-05-21",
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
  "startDate": "2026-05-11",
  "endDate": "2026-05-21",
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
  "startDate": "2026-05-11",
  "endDate": "2026-05-11",
  "hasUsed": true,
  "active": false
}
```

> `endDate` es actualizado a la fecha de devolución. `active` pasa a `false`.

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

### GET `/my-loans`

Devuelve los préstamos del estudiante autenticado (paginados y filtrables). Cualquier usuario autenticado puede acceder.

**Query Parameters**

| Parámetro | Tipo | Requerido | Default | Descripción |
|---|---|---|---|---|
| `active` | Boolean | No | — | `true` = activos, `false` = devueltos, omitir = todos |
| `page` | Integer | No | `0` | Número de página (base 0) |
| `size` | Integer | No | `10` | Tamaño de página |
| `sortBy` | String | No | `startDate` | Campo de ordenación (`startDate`, `endDate`, `bookId`) |
| `sortDir` | String | No | `desc` | Dirección: `asc` o `desc` |

**Curl**

```bash
curl -X GET "http://localhost:8083/api/v1/loans/my-loans?active=true&page=0&size=5&sortBy=startDate&sortDir=desc" \
  -H "Authorization: Bearer <token>"
```

**Response 200**

```json
{
  "content": [
    {
      "id": 3,
      "studentId": 42,
      "bookId": "64a1b2c3d4e5f6a7b8c9d0e1",
      "startDate": "2026-05-11",
      "endDate": "2026-05-21",
      "hasUsed": false,
      "active": true
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "number": 0,
  "size": 5
}
```

---

### GET `/student/{studentId}`

Devuelve los préstamos de un estudiante específico (paginados y filtrables). **Solo ADMIN.**

**Path Parameters**

| Parámetro | Tipo | Descripción |
|---|---|---|
| `studentId` | Long | ID del estudiante |

**Query Parameters**

| Parámetro | Tipo | Requerido | Default | Descripción |
|---|---|---|---|---|
| `active` | Boolean | No | — | `true` = activos, `false` = devueltos, omitir = todos |
| `page` | Integer | No | `0` | Número de página (base 0) |
| `size` | Integer | No | `10` | Tamaño de página |
| `sortBy` | String | No | `startDate` | Campo de ordenación (`startDate`, `endDate`, `bookId`) |
| `sortDir` | String | No | `desc` | Dirección: `asc` o `desc` |

**Curl**

```bash
curl -X GET "http://localhost:8083/api/v1/loans/student/42?active=false&page=0&size=10" \
  -H "Authorization: Bearer <admin-token>"
```

**Response 200** — misma estructura paginada que `/my-loans`

**Response 403 — Rol insuficiente**

```json
{
  "message": "Access Denied",
  "status": "FORBIDDEN",
  "timestamp": "2026-05-12T10:00:00"
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

---

## Patrones de diseño

### Strategy — Validación de préstamos

**Ubicación:** `domain/validation/ILoanCreationRule`, `domain/validation/ILoanReturnRule` + `domain/validation/rules/`

Cada regla de negocio está encapsulada en su propia clase que implementa la interfaz de validación correspondiente. Se usan dos familias de estrategias según el momento del ciclo de vida del préstamo:

**Reglas de creación (`ILoanCreationRule`):**

| Estrategia | Orden | Regla |
|---|---|---|
| `GpaLoanCreationRule` | 1 | Si `gpa < 3.2` y el estudiante ya tiene ≥ 1 préstamo activo, bloquea el nuevo préstamo |

**Reglas de devolución (`ILoanReturnRule`):**

| Estrategia | Orden | Regla |
|---|---|---|
| `LoanOwnershipReturnRule` | 1 | Solo el estudiante dueño del préstamo puede devolverlo |
| `LoanActiveStateReturnRule` | 2 | Un préstamo ya devuelto no puede devolverse de nuevo |

Cada estrategia es un `@Component` de Spring con `@Order`. El `LoanUseCase` recibe `List<ILoanCreationRule>` y `List<ILoanReturnRule>` por inyección y ejecuta cada lista con `forEach`.

**Beneficio:** agregar una nueva regla de validación no requiere tocar el use case — solo crear una clase que implemente la interfaz.

Los `if` procedurales son reemplazados por cadenas funcionales con `Optional`:

```java
// creación
Optional.ofNullable(gpa)
    .filter(g -> g < MIN_GPA && activeLoans >= MAX_LOANS)
    .ifPresent(g -> { throw new LoanBlockedException(...); });

// devolución — ownership
Optional.of(loan)
    .filter(l -> l.getStudentId().equals(studentId))
    .orElseThrow(() -> new LoanOwnershipException(...));

// devolución — estado activo
Optional.of(loan)
    .filter(l -> Boolean.TRUE.equals(l.getActive()))
    .orElseThrow(() -> new LoanNotActiveException(...));
```

```
ILoanCreationRule (interface)
  └── GpaLoanCreationRule   @Order(1)

ILoanReturnRule (interface)
  ├── LoanOwnershipReturnRule    @Order(1)
  └── LoanActiveStateReturnRule  @Order(2)
```

---

### Factory Method — Construcción del Loan

**Ubicación:** `domain/factory/LoanFactory`

`LoanFactory.newLoan(studentId, bookId)` centraliza la construcción del objeto `Loan` listo para persistir: asigna fechas, estado inicial (`hasUsed=false`, `active=true`) y el período de 10 días.

**Beneficio:** el `LoanUseCase` delega la construcción al factory y queda como orquestador puro sin lógica de inicialización.

```java
// LoanFactory
public static Loan newLoan(Long studentId, String bookId) {
    LocalDate today = LocalDate.now();
    return Loan.builder()
            .studentId(studentId).bookId(bookId)
            .startDate(today).endDate(today.plusDays(LOAN_DURATION_DAYS))
            .hasUsed(false).active(true)
            .build();
}
```

---

### Builder — Transiciones de estado del Loan

**Ubicación:** `domain/model/Loan`

El modelo `Loan` expone métodos de dominio que producen una nueva instancia inmutable via `toBuilder()`, sin exponer lógica de construcción en el use case ni en el domain service:

| Método | Transición |
|---|---|
| `withUsed()` | `hasUsed → true` |
| `withReturned()` | `active → false`, `endDate → now` |

```java
// use case — limpio, sin builders en línea
Loan updated = loan.withUsed();
Loan closed  = loan.withReturned();
```

**Beneficio:** el use case orquesta sin conocer los detalles de construcción del modelo; la lógica de qué campos cambia en cada transición vive en el propio dominio.

---

### Adapter — Conversión entre dominio e infraestructura

**Ubicación:** `infrastructure/adapters/driven/jpa/adapter/LoanPersistenceAdapter`, `infrastructure/adapters/driven/feign/adapter/CatalogFeignClientAdapter`, `infrastructure/adapters/driven/security/adapter/JwtAdapter`

Cada adaptador implementa un puerto de salida del dominio y convierte entre el modelo de dominio y la tecnología concreta:

| Adaptador | Puerto | Tecnología |
|---|---|---|
| `LoanPersistenceAdapter` | `ILoanPersistencePort` | Spring Data JPA + PostgreSQL |
| `CatalogFeignClientAdapter` | `ICatalogFeignClientPort` | Spring Cloud OpenFeign → `catalog:8082` |
| `JwtAdapter` | `IJwtPort` | JJWT 0.11.5 |

```
ILoanPersistencePort (puerto de dominio)
      │
LoanPersistenceAdapter (adaptador)
      │
ILoanRepository (Spring Data JPA)
      │
PostgreSQL
```

**Beneficio:** el dominio nunca importa clases de Spring Data, Feign ni JJWT. Cambiar de PostgreSQL a otro motor solo requiere un nuevo adaptador, sin tocar el dominio.

---

### Facade — LoanUseCase como orquestador único

**Ubicación:** `application/usecase/LoanUseCase`

Desde la perspectiva del `LoanController`, toda la complejidad del sistema (base de datos, llamadas Feign al catálogo, validación de reglas, construcción de modelos) queda oculta detrás de una interfaz simple: `ILoanServicePort`.

```
LoanController
    │
    ▼  ILoanServicePort (fachada)
    │
LoanUseCase
    ├── ILoanPersistencePort   → PostgreSQL
    ├── ICatalogFeignClientPort → catalog:8082
    ├── List<ILoanCreationRule> → validación GPA
    └── List<ILoanReturnRule>  → validación devolución
```

**Beneficio:** el controller no conoce la existencia de Feign, JPA ni las reglas de validación. Añadir una nueva fuente de datos o regla no cambia el contrato del controller.
