# University Mock — Bio Library

## Objetivo

`university-mock` es un microservicio Spring Boot que **simula la caja negra del sistema académico** de cada universidad participante en el ecosistema Bio Library.

Su propósito es centralizar en un solo servicio las consultas que el resto del sistema necesita hacer al repositorio académico de cada institución, sin necesidad de integrarse directamente con los sistemas internos de cada universidad. Permite consultar:

- Si un estudiante existe en una universidad (por carnet).
- Si el estudiante está **matriculado activamente**.
- El **promedio académico (GPA)** del estudiante.
- Los datos básicos de contacto e identificación.

---

## Stack tecnológico

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.3 |
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
  └── REST Controller  ──▶  Service Port (in)
                                  │
                          UniversityStudentUseCase
                                  │
                         Persistence Port (out)
                                  │
                      JPA Persistence Adapter
                                  │
                           PostgreSQL (schema: university)
```

---

## Endpoints

**Base URL:** `http://localhost:8081/api/v1/university/students`

---

### GET `/{carnet}/{university}`

Retorna el perfil académico de un estudiante por su carnet y universidad.

**Path Parameters**

| Parámetro | Tipo | Descripción | Ejemplo |
|---|---|---|---|
| `carnet` | String | Número de carnet del estudiante | `20210001` |
| `university` | Enum | Identificador de la universidad | `ITM` |

**Curl**

```bash
curl -X GET "http://localhost:8081/api/v1/university/students/20210001/ITM"
```

**Response 200 — Estudiante encontrado**

```json
{
  "carnet": "20210001",
  "dni": "1020100001",
  "name": "Carlos",
  "lastName": "García",
  "email": "carlos.garcia@itm.edu.co",
  "university": "ITM",
  "gpa": 3.8,
  "enrolled": true
}
```

**Response 404 — Estudiante no encontrado**

```json
{
  "message": "No student found with carnet: 20210001",
  "status": "Not Found",
  "timestamp": "2026-05-11T10:30:00",
  "code": 404
}
```

---

### GET `/university/{university}`

Retorna todos los estudiantes registrados en una universidad.

**Path Parameters**

| Parámetro | Tipo | Descripción | Ejemplo |
|---|---|---|---|
| `university` | Enum | Identificador de la universidad | `ITM` |

**Curl**

```bash
curl -X GET "http://localhost:8081/api/v1/university/students/university/ITM"
```

**Response 200 — Lista de estudiantes**

```json
[
  {
    "carnet": "20210001",
    "dni": "1020100001",
    "name": "Carlos",
    "lastName": "García",
    "email": "carlos.garcia@itm.edu.co",
    "university": "ITM",
    "gpa": 3.8,
    "enrolled": true
  },
  {
    "carnet": "20210002",
    "dni": "1020100002",
    "name": "Valentina",
    "lastName": "López",
    "email": "valentina.lopez@itm.edu.co",
    "university": "ITM",
    "gpa": 4.2,
    "enrolled": false
  }
]
```

> Si la universidad no tiene estudiantes registrados, se retorna un arreglo vacío `[]` con estado 200.

---

## Universidades disponibles

Valores válidos para el parámetro `university`:

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
| `SERVER_PORT` | `8081` | Puerto HTTP del servidor |
| `DB_HOST` | `localhost` | Host de la base de datos PostgreSQL |
| `DB_PORT` | `5432` | Puerto de PostgreSQL |
| `DB_NAME` | `biolibrary` | Nombre de la base de datos |
| `DB_USER` | `postgres` | Usuario de PostgreSQL |
| `DB_PASSWORD` | `postgres` | Contraseña de PostgreSQL |
| `DB_SCHEMA` | `university` | Schema donde se crean las tablas |

---

## Correr localmente

**Requisitos:** PostgreSQL corriendo con la base de datos `biolibrary` disponible.

```bash
# Clonar el repositorio
git clone <repo-url>
cd bio-library/university-mock

# Ejecutar con valores por defecto (PostgreSQL en localhost:5432)
./gradlew bootRun

# Ejecutar con variables de entorno personalizadas
SERVER_PORT=8082 DB_HOST=my-db-host ./gradlew bootRun
```

Al iniciar, el microservicio carga automáticamente los datos de muestra desde `src/main/resources/data.sql` (220 estudiantes distribuidos en las 11 universidades).

---

## Métricas

Expone `/actuator/prometheus` en el puerto `8081` para scraping con Prometheus.

---

## Swagger / API Docs

Una vez levantado el servicio:

| Recurso | URL |
|---|---|
| Swagger UI | `http://localhost:8081/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8081/v3/api-docs` |
