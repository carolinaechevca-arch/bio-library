# User — Bio Library

## Objetivo

`user` es el microservicio encargado de la **gestión de usuarios y autenticación** dentro del ecosistema Bio Library.

Sus responsabilidades son:

- **Autenticar** usuarios (estudiantes y administradores) retornando un JWT válido.
- **Registrar estudiantes** validando su existencia y estado de matrícula contra el sistema universitario (`university-mock`) antes de crear la cuenta.
- Garantizar que el email del estudiante pertenezca al dominio de su universidad y que los datos (DNI, email) coincidan con los registros académicos.

---

## Stack tecnológico

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.3 |
| Spring Security | — |
| JWT (JJWT) | 0.11.5 |
| Spring Cloud OpenFeign | 2024.0.0 |
| Spring Data JPA | — |
| PostgreSQL | — |
| MapStruct | 1.6.3 |
| Lombok | 1.18.36 |
| SpringDoc OpenAPI (Swagger) | 2.8.4 |
| Gradle | — |

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
- `ADMIN` — puede registrar estudiantes.
- `STUDENT` — acceso a recursos del sistema de préstamos (otros micros).

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

| Campo | Tipo | Requerido | Restricciones |
|---|---|---|---|
| `email` | String | Sí | Formato email válido, máx 200 caracteres |
| `password` | String | Sí | 8–100 caracteres |

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

### POST `/api/v1/students/create`

Registra un nuevo estudiante. Requiere rol **ADMIN**.

**Header requerido:** `Authorization: Bearer <token>`

**Request Body**

```json
{
  "carnet": "20210001",
  "dni": "1020100001",
  "name": "Carlos",
  "lastName": "García",
  "email": "carlos.garcia@itm.edu.co",
  "password": "SecurePass123",
  "phoneNumber": "+573012345678",
  "university": "ITM"
}
```

| Campo | Tipo | Requerido | Restricciones |
|---|---|---|---|
| `carnet` | String | Sí | Debe existir en el sistema universitario |
| `dni` | String | Sí | Debe coincidir con el registro universitario |
| `name` | String | Sí | Máx 100 caracteres (sobrescrito por dato universitario) |
| `lastName` | String | Sí | Máx 100 caracteres (sobrescrito por dato universitario) |
| `email` | String | Sí | Debe pertenecer al dominio de la universidad y coincidir con el registro |
| `password` | String | Sí | 8–100 caracteres |
| `phoneNumber` | String | Sí | — |
| `university` | String | Sí | Valor del enum `University` (ver tabla de universidades) |

> `name`, `lastName` y `gpa` son sobrescritos por los datos que retorna `university-mock`.

**Curl**

```bash
curl -X POST "http://localhost:8080/api/v1/students/create" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "carnet": "20210001",
    "dni": "1020100001",
    "name": "Carlos",
    "lastName": "García",
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

**Response 401 — Autenticación requerida**

```json
{
  "message": "Authentication is required to access this resource.",
  "status": "Unauthorized",
  "timestamp": "2026-05-11T10:30:00",
  "code": 401
}
```

**Response 403 — Permisos insuficientes**

```json
{
  "message": "You do not have the necessary permissions to perform this action.",
  "status": "Forbidden",
  "timestamp": "2026-05-11T10:30:00",
  "code": 403
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

| Enum | Dominio de email |
|---|---|
| `UNIVERSIDAD_NACIONAL` | unacional.edu.co |
| `UNIVERSIDAD_DE_ANTIOQUIA` | udea.edu.co |
| `UNIVERSIDAD_EAFIT` | eafit.edu.co |
| `UNIVERSIDAD_DE_LOS_ANDES` | uniandes.edu.co |
| `UNIVERSIDAD_PONTIFICIA_BOLIVARIANA` | upb.edu.co |
| `ITM` | itm.edu.co |
| `PASCUAL_BRAVO` | pascualbravo.edu.co |
| `COLMAYOR` | colmayor.edu.co |
| `UNIREMINGTON` | uniremington.edu.co |
| `UNIVERSIDAD_DE_MEDELLIN` | udem.edu.co |
| `UNIVERSIDAD_CES` | ces.edu.co |

---

## Variables de entorno

| Variable | Default | Descripción |
|---|---|---|
| `SERVER_PORT` | `8080` | Puerto HTTP del servidor |
| `DB_HOST` | `localhost` | Host de PostgreSQL |
| `DB_PORT` | `5432` | Puerto de PostgreSQL |
| `DB_USERNAME` | `postgres` | Usuario de PostgreSQL |
| `DB_PASSWORD` | `postgres` | Contraseña de PostgreSQL |
| `DB_SCHEMA` | `users` | Schema donde se crean las tablas |
| `JWT_SECRET` | `586B633A...` | Clave secreta para firmar los JWT (HMAC-SHA256) |
| `JWT_EXPIRATION` | `3600000` | Tiempo de expiración del token en milisegundos (1 hora) |
| `UNIVERSITY_MOCK_URL` | `http://localhost:8081` | URL base del microservicio `university-mock` |

> En producción siempre sobreescribir `JWT_SECRET` con un valor seguro generado aleatoriamente.

---

## Usuarios iniciales (data.sql)

Al iniciar la aplicación se crean automáticamente **11 usuarios ADMIN**, uno por universidad:

| Email | Universidad |
|---|---|
| admin@unacional.edu.co | UNIVERSIDAD_NACIONAL |
| admin@udea.edu.co | UNIVERSIDAD_DE_ANTIOQUIA |
| admin@eafit.edu.co | UNIVERSIDAD_EAFIT |
| admin@uniandes.edu.co | UNIVERSIDAD_DE_LOS_ANDES |
| admin@upb.edu.co | UNIVERSIDAD_PONTIFICIA_BOLIVARIANA |
| admin@itm.edu.co | ITM |
| admin@pascualbravo.edu.co | PASCUAL_BRAVO |
| admin@colmayor.edu.co | COLMAYOR |
| admin@uniremington.edu.co | UNIREMINGTON |
| admin@udem.edu.co | UNIVERSIDAD_DE_MEDELLIN |
| admin@ces.edu.co | UNIVERSIDAD_CES |

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

| Recurso | URL |
|---|---|
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |

Los endpoints protegidos requieren agregar el JWT en Swagger UI mediante el botón **Authorize** usando el esquema `Bearer <token>`.
