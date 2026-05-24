# User — Bio Library

## Objetivo

`user` es el microservicio encargado de la **gestión de usuarios y autenticación** dentro del ecosistema Bio Library.

Sus responsabilidades son:

- **Autenticar** usuarios (estudiantes y administradores) retornando un JWT válido.
- **Registrar estudiantes** validando su existencia y estado de matrícula contra el sistema universitario (`university-mock`) antes de crear la cuenta.
- Garantizar que el email del estudiante pertenezca al dominio de su universidad y que los datos (DNI, email) coincidan con los registros académicos.

---

## Stack tecnológico

| Tecnología                  | Versión  |
| --------------------------- | -------- |
| Java                        | 21       |
| Spring Boot                 | 3.4.3    |
| Spring Security             | —        |
| JWT (JJWT)                  | 0.11.5   |
| Spring Cloud OpenFeign      | 2024.0.0 |
| Spring Data JPA             | —        |
| PostgreSQL                  | —        |
| MapStruct                   | 1.6.3    |
| Lombok                      | 1.18.36  |
| SpringDoc OpenAPI (Swagger) | 2.8.4    |
| Gradle                      | —        |

---

## Arquitectura

El microservicio implementa **Arquitectura Hexagonal (Puertos y Adaptadores)**:

```
driving (entrada)
  ├── AuthController       ──▶  IAuthServicePort
  │                                    │
  │                              AuthUseCase
  │                                    │
  │                    IUserPersistencePort / IJwtPersistencePort
  │                    IPasswordEncoderPersistencePort
  │
  └── StudentController   ──▶  IStudentServicePort
                                       │
                               StudentUseCase
                                       │
                    IStudentPersistencePort / IUniversityFeignClientPort
                                       │
                              university-mock (Feign)
                                       │
                                PostgreSQL (schema: users)
```

**Roles:**
- `ADMIN` — acceso a gestión de estudiantes (`GET /students`, `GET /students/{id}`, `PATCH /students/{id}/sanction`).
- `STUDENT` — acceso a recursos del sistema de préstamos (otros micros).

> `POST /api/v1/students/create` es una ruta pública — no requiere ningún rol.

---

## Dependencia con university-mock

Al registrar un estudiante, este micro llama a `university-mock` via **OpenFeign** para:

1. Verificar que el carnet existe en la universidad indicada.
2. Validar que el DNI y email del request coincidan con los registros universitarios.
3. Confirmar que el estudiante está **matriculado activamente**.
4. Obtener el **GPA** del sistema universitario (no se acepta el que envíe el cliente).

---

## Endpoints

**Base URL:** `http://localhost:8080`

---

### POST `/api/v1/auth/login`

Autentica un usuario y retorna un JWT.

**No requiere autenticación previa.**

**Request Body**

```json
{
  "email": "admin@unacional.edu.co",
  "password": "password123"
}
```

| Campo      | Tipo   | Requerido | Restricciones                            |
| ---------- | ------ | --------- | ---------------------------------------- |
| `email`    | String | Sí        | Formato email válido, máx 200 caracteres |
| `password` | String | Sí        | 8–100 caracteres                         |

**Curl**

```bash
curl -X POST "http://localhost:8080/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@unacional.edu.co","password":"password123"}'
```

**Response 200 — Autenticación exitosa**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "accessExpirationTime": 3600000
}
```

**Response 400 — Datos de request inválidos**

```json
{
  "message": "Email is required",
  "status": "Bad Request",
  "timestamp": "2026-05-11T10:30:00",
  "code": 400
}
```

**Response 401 — Credenciales inválidas**

```json
{
  "message": "Invalid email or password",
  "status": "Unauthorized",
  "timestamp": "2026-05-11T10:30:00",
  "code": 401
}
```

---

### GET `/api/v1/auth/me`

Retorna el perfil del usuario autenticado. Disponible para **ADMIN** y **STUDENT**.

**Header requerido:** `Authorization: Bearer <token>`

**Curl**

```bash
curl -X GET "http://localhost:8080/api/v1/auth/me" \
  -H "Authorization: Bearer <token>"
```

**Response 200 — Usuario STUDENT**

```json
{
  "id": 1,
  "dni": "1020100001",
  "name": "Carlos",
  "lastName": "García",
  "email": "carlos.garcia@itm.edu.co",
  "phoneNumber": "+573012345678",
  "role": "STUDENT",
  "university": "ITM",
  "carnet": "20210001",
  "gpa": 3.8,
  "hasSanction": false,
  "sanctionEndDate": null,
  "activeLoans": 0
}
```

**Response 200 — Usuario ADMIN**

```json
{
  "id": 5,
  "dni": "9999999999",
  "name": "Admin",
  "lastName": "ITM",
  "email": "admin@itm.edu.co",
  "phoneNumber": null,
  "role": "ADMIN",
  "university": "ITM",
  "carnet": null,
  "gpa": null,
  "hasSanction": null,
  "sanctionEndDate": null,
  "activeLoans": null
}
```

**Response 401 — Token ausente o inválido**

```json
{
  "message": "Authentication is required to access this resource.",
  "status": "Unauthorized",
  "timestamp": "2026-05-12T10:00:00",
  "code": 401
}
```

---

### GET `/api/v1/students`

Lista paginada de estudiantes de la **misma universidad del admin autenticado** (derivada automáticamente del JWT). Requiere rol **ADMIN**.

**Header requerido:** `Authorization: Bearer <token>`

**Query params**

| Parámetro | Tipo    | Requerido | Default  | Descripción                                                          |
| --------- | ------- | --------- | -------- | -------------------------------------------------------------------- |
| `page`    | Integer | No        | `0`      | Número de página (0-based)                                           |
| `size`    | Integer | No        | `10`     | Elementos por página                                                 |
| `sortBy`  | String  | No        | `carnet` | Campo de ordenamiento: `carnet`, `gpa`, `user.name`, `user.lastName` |
| `sortDir` | String  | No        | `asc`    | Dirección: `asc` o `desc`                                            |

> La universidad no se filtra como parámetro — se toma automáticamente del email del admin en el token JWT.

**Curl**

```bash
curl -X GET "http://localhost:8080/api/v1/students?page=0&size=10" \
  -H "Authorization: Bearer <token>"
```

**Response 200**

```json
{
  "content": [ { "id": 1, "carnet": "20210001", "gpa": 3.8, ... } ],
  "totalElements": 42,
  "totalPages": 5,
  "number": 0,
  "size": 10
}
```

---

### GET `/api/v1/students/{id}`

Retorna el perfil completo de un estudiante por su ID de usuario. Requiere rol **ADMIN**.

**Header requerido:** `Authorization: Bearer <token>`

**Curl**

```bash
curl -X GET "http://localhost:8080/api/v1/students/1" \
  -H "Authorization: Bearer <token>"
```

**Response 200**

```json
{
  "id": 1,
  "carnet": "20210001",
  "dni": "1020100001",
  "name": "Carlos",
  "lastName": "García",
  "email": "carlos.garcia@itm.edu.co",
  "phoneNumber": "+573012345678",
  "role": "STUDENT",
  "university": "ITM",
  "gpa": 3.8,
  "hasSanction": false,
  "sanctionEndDate": null,
  "activeLoans": 0
}
```

**Response 404 — Estudiante no encontrado**

```json
{
  "message": "Student with id 99 was not found.",
  "status": "Not Found",
  "timestamp": "2026-05-12T10:00:00",
  "code": 404
}
```

---

### PATCH `/api/v1/students/{id}/sanction`

Aplica o levanta una sanción sobre un estudiante. Requiere rol **ADMIN**.

**Header requerido:** `Authorization: Bearer <token>`

**Request Body**

```json
{
  "active": true,
  "sanctionEndDate": "2026-08-01"
}
```

| Campo             | Tipo      | Requerido | Descripción                                                           |
| ----------------- | --------- | --------- | --------------------------------------------------------------------- |
| `active`          | Boolean   | Sí        | `true` aplica la sanción, `false` la levanta                          |
| `sanctionEndDate` | LocalDate | No        | Fecha de fin (`YYYY-MM-DD`). Se ignora (limpia) cuando `active=false` |

> La sanción se levanta automáticamente al día siguiente de `sanctionEndDate`. El job de sanciones corre cada 5 minutos y elimina las sanciones donde `sanctionEndDate < hoy`.

**Curl — aplicar sanción**

```bash
curl -X PATCH "http://localhost:8080/api/v1/students/1/sanction" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"active": true, "sanctionEndDate": "2026-08-01"}'
```

**Curl — levantar sanción**

```bash
curl -X PATCH "http://localhost:8080/api/v1/students/1/sanction" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"active": false}'
```

**Response 200 — Sanción actualizada**

```json
{
  "id": 1,
  "hasSanction": true,
  "sanctionEndDate": "2026-08-01",
  ...
}
```

---

### POST `/api/v1/students/create`

Registra un nuevo estudiante. **No requiere autenticación** — es una ruta pública tanto en el API gateway como en la configuración de Spring Security del servicio (`PUBLIC_ENDPOINTS`).

**Request Body**

```json
{
  "carnet": "20210001",
  "dni": "1020100001",
  "email": "carlos.garcia@itm.edu.co",
  "password": "SecurePass123",
  "phoneNumber": "+573012345678",
  "university": "ITM"
}
```

| Campo         | Tipo   | Requerido | Restricciones                                                            |
| ------------- | ------ | --------- | ------------------------------------------------------------------------ |
| `carnet`      | String | Sí        | Debe existir en el sistema universitario                                 |
| `dni`         | String | Sí        | Debe coincidir con el registro universitario                             |
| `email`       | String | Sí        | Debe pertenecer al dominio de la universidad y coincidir con el registro |
| `password`    | String | Sí        | 8–100 caracteres                                                         |
| `phoneNumber` | String | Sí        | —                                                                        |
| `university`  | String | Sí        | Valor del enum `University` (ver tabla de universidades)                 |


**Curl**

```bash
curl -X POST "http://localhost:8080/api/v1/students/create" \
  -H "Content-Type: application/json" \
    -d '{
    "carnet": "20210001",
    "dni": "1020100001",
    "email": "carlos.garcia@itm.edu.co",
    "password": "SecurePass123",
    "phoneNumber": "+573012345678",
    "university": "ITM"
  }'
```

**Response 201 — Estudiante creado**

```json
{
  "id": 1,
  "carnet": "20210001",
  "dni": "1020100001",
  "name": "Carlos",
  "lastName": "García",
  "email": "carlos.garcia@itm.edu.co",
  "phoneNumber": "+573012345678",
  "role": "STUDENT",
  "university": "ITM",
  "gpa": 3.8,
  "hasSanction": false,
  "sanctionEndDate": null,
  "activeLoans": 0
}
```

**Response 400 — Error de validación o email no pertenece al dominio universitario**

```json
{
  "message": "Email must belong to the university domain: @itm.edu.co",
  "status": "Bad Request",
  "timestamp": "2026-05-11T10:30:00",
  "code": 400
}
```

**Response 409 — Estudiante ya existe**

```json
{
  "message": "Student with email carlos.garcia@itm.edu.co already exists.",
  "status": "Conflict",
  "timestamp": "2026-05-11T10:30:00",
  "code": 409
}
```

**Response 422 — Datos no válidos con el sistema universitario**

```json
{
  "message": "The student is not currently enrolled and cannot register.",
  "status": "Unprocessable Entity",
  "timestamp": "2026-05-11T10:30:00",
  "code": 422
}
```

> Otros mensajes posibles para 422: carnet no encontrado en la universidad, DNI no coincide, email no coincide.

---

## Universidades disponibles

Valores válidos para el campo `university`:

| Enum                                 | Dominio de email    |
| ------------------------------------ | ------------------- |
| `UNIVERSIDAD_NACIONAL`               | unacional.edu.co    |
| `UNIVERSIDAD_DE_ANTIOQUIA`           | udea.edu.co         |
| `UNIVERSIDAD_EAFIT`                  | eafit.edu.co        |
| `UNIVERSIDAD_DE_LOS_ANDES`           | uniandes.edu.co     |
| `UNIVERSIDAD_PONTIFICIA_BOLIVARIANA` | upb.edu.co          |
| `ITM`                                | itm.edu.co          |
| `PASCUAL_BRAVO`                      | pascualbravo.edu.co |
| `COLMAYOR`                           | colmayor.edu.co     |
| `UNIREMINGTON`                       | uniremington.edu.co |
| `UNIVERSIDAD_DE_MEDELLIN`            | udem.edu.co         |
| `UNIVERSIDAD_CES`                    | ces.edu.co          |

---

## Variables de entorno

| Variable              | Default                 | Descripción                                             |
| --------------------- | ----------------------- | ------------------------------------------------------- |
| `SERVER_PORT`         | `8080`                  | Puerto HTTP del servidor                                |
| `DB_HOST`             | `localhost`             | Host de PostgreSQL                                      |
| `DB_PORT`             | `5432`                  | Puerto de PostgreSQL                                    |
| `DB_USERNAME`         | `postgres`              | Usuario de PostgreSQL                                   |
| `DB_PASSWORD`         | `postgres`              | Contraseña de PostgreSQL                                |
| `DB_SCHEMA`           | `users`                 | Schema donde se crean las tablas                        |
| `JWT_SECRET`          | `586B633A...`           | Clave secreta para firmar los JWT (HMAC-SHA256)         |
| `JWT_EXPIRATION`      | `3600000`               | Tiempo de expiración del token en milisegundos (1 hora) |
| `UNIVERSITY_MOCK_URL` | `http://localhost:8081` | URL base del microservicio `university-mock`            |

> En producción siempre sobreescribir `JWT_SECRET` con un valor seguro generado aleatoriamente.

---

## Usuarios iniciales (data.sql)

Al iniciar la aplicación se crean automáticamente **11 usuarios ADMIN**, uno por universidad:

| Email                     | Universidad                        |
| ------------------------- | ---------------------------------- |
| admin@unacional.edu.co    | UNIVERSIDAD_NACIONAL               |
| admin@udea.edu.co         | UNIVERSIDAD_DE_ANTIOQUIA           |
| admin@eafit.edu.co        | UNIVERSIDAD_EAFIT                  |
| admin@uniandes.edu.co     | UNIVERSIDAD_DE_LOS_ANDES           |
| admin@upb.edu.co          | UNIVERSIDAD_PONTIFICIA_BOLIVARIANA |
| admin@itm.edu.co          | ITM                                |
| admin@pascualbravo.edu.co | PASCUAL_BRAVO                      |
| admin@colmayor.edu.co     | COLMAYOR                           |
| admin@uniremington.edu.co | UNIREMINGTON                       |
| admin@udem.edu.co         | UNIVERSIDAD_DE_MEDELLIN            |
| admin@ces.edu.co          | UNIVERSIDAD_CES                    |

**Contraseña por defecto de todos:** `password123`

---

## Correr localmente

**Requisitos:** PostgreSQL corriendo con la base de datos `biolibrary` disponible, y `university-mock` levantado en el puerto 8081.

```bash
# Clonar el repositorio
git clone <repo-url>
cd bio-library/user

# Ejecutar con valores por defecto
./gradlew bootRun

# Ejecutar con variables de entorno personalizadas
SERVER_PORT=8082 JWT_SECRET=mi-clave-segura UNIVERSITY_MOCK_URL=http://university:8081 ./gradlew bootRun
```

---

## Swagger / API Docs

Una vez levantado el servicio:

| Recurso      | URL                                     |
| ------------ | --------------------------------------- |
| Swagger UI   | `http://localhost:8080/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs`     |

Los endpoints protegidos requieren agregar el JWT en Swagger UI mediante el botón **Authorize** usando el esquema `Bearer <token>`.

---

## Patrones de diseño

### Strategy — Validación de estudiantes

**Ubicación:** `domain/validation/StudentValidationStrategy` + `domain/validation/strategies/`

Cada regla de negocio que debe verificarse al registrar un estudiante está encapsulada en su propia clase que implementa `StudentValidationStrategy`:

| Estrategia                      | Orden | Regla                                                                |
| ------------------------------- | ----- | -------------------------------------------------------------------- |
| `EmailDomainValidationStrategy` | 1     | El email pertenece al dominio de la universidad seleccionada         |
| `DniValidationStrategy`         | 2     | El DNI del request coincide con el registrado en `university-mock`   |
| `EmailMatchValidationStrategy`  | 3     | El email del request coincide con el registrado en `university-mock` |
| `EnrollmentValidationStrategy`  | 4     | El estudiante tiene matrícula activa en `university-mock`            |

Cada estrategia es un `@Component` de Spring. El `StudentUseCase` recibe `List<StudentValidationStrategy>` por inyección y ejecuta todas con `strategies.forEach(s -> s.validate(student, uniData))`.

**Beneficio:** agregar o remover una regla de validación no requiere tocar el use case — solo crear o eliminar una clase que implemente la interfaz.

```
StudentValidationStrategy (interface)
  ├── EmailDomainValidationStrategy  @Order(1)
  ├── DniValidationStrategy          @Order(2)
  ├── EmailMatchValidationStrategy   @Order(3)
  └── EnrollmentValidationStrategy   @Order(4)
```

---

### Factory Method — Construcción del Student

**Ubicación:** `domain/factory/StudentFactory`

`StudentFactory.create(student, uniData, encodedPassword)` centraliza la construcción del objeto `Student` listo para persistir, encadenando los dos pasos de transformación:

1. `student.withUniversityData(uniData)` — sobrescribe nombre, apellido y GPA con datos universitarios
2. `.withRegistrationDefaults(encodedPassword)` — asigna password encriptado, rol `STUDENT`, sanciones iniciales en cero

**Beneficio:** el `StudentUseCase` delega la creación al factory y queda como orquestador puro sin lógica de construcción.

---

## Job de sanciones (Scheduler)

El job `SanctionScheduledJob` se ejecuta cada **5 minutos** y levanta automáticamente las sanciones cuya `sanctionEndDate` ya pasó (`sanctionEndDate < hoy`).

Ejemplo: si la sanción termina el `2026-08-01`, el job la elimina a partir del `2026-08-02`.

---

## Endpoint interno

El endpoint `GET /api/v1/internal/students/{userId}/email` es de uso exclusivo entre microservicios (no pasa por el API Gateway). Retorna:

```json
{
  "email": "carlos.garcia@itm.edu.co",
  "phone": "+573012345678",
  "hasSanction": false
}
```

Es usado por el micro `loans` para validar sanción antes de crear un préstamo y para obtener datos de contacto al enviar notificaciones SMS.

---

## Métricas

Expone `/actuator/prometheus` en el puerto `8080` para scraping con Prometheus.
