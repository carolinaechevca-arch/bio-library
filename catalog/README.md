# Catalog — Bio Library

## Objetivo

`catalog` es el microservicio que gestiona el **catálogo de libros digitales** de Bio Library.

Sus responsabilidades son:

- Exponer el inventario de libros con paginación.
- Consultar el detalle de un libro específico.
- Controlar el **conteo de préstamos activos** por libro, garantizando que ningún título supere su límite de licencias concurrentes.

> **Regla de negocio central:** un libro puede ser prestado por un máximo de **5 estudiantes de forma simultánea**. Este límite está definido en el campo `license.maxConcurrentLoans` de cada libro y es aplicado por este microservicio cada vez que el micro `loans` solicita un nuevo préstamo.

---

## Stack tecnológico

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.3 |
| Spring Data MongoDB | — |
| MongoDB | — |
| MapStruct | 1.6.3 |
| Lombok | — |
| SpringDoc OpenAPI (Swagger) | 2.8.4 |
| Gradle | — |

---

## Arquitectura

El microservicio implementa **Arquitectura Hexagonal (Puertos y Adaptadores)**:

```
driving (entrada)
  └── BookController  ──▶  IBookServicePort (in)
                                  │
                           BookUseCase
                                  │
                        IBookPersistencePort (out)
                                  │
                       BookPersistenceAdapter
                                  │
                            MongoDB (colección: books)
```

**No tiene seguridad propia** — sus endpoints son llamados directamente por `loans` vía Feign para actualizar el conteo, y pueden ser consumidos por el frontend para listar libros.

---

## Modelo de licencia

Cada libro tiene un objeto `license` embebido:

```json
"license": {
  "maxConcurrentLoans": 5,
  "activeLoanCount": 2
}
```

| Campo | Descripción |
|---|---|
| `maxConcurrentLoans` | Máximo de préstamos simultáneos permitidos para el título |
| `activeLoanCount` | Préstamos activos en este momento |

Cuando `activeLoanCount` alcanza `maxConcurrentLoans`, el libro queda **no disponible** hasta que algún estudiante devuelva su copia.

---

## Endpoints

**Base URL:** `http://localhost:8082/api/v1/books`

---

### GET `/`

Retorna el catálogo de libros paginado.

**Query Parameters**

| Parámetro | Tipo | Default | Descripción |
|---|---|---|---|
| `page` | int | `0` | Número de página (base 0) |
| `size` | int | `10` | Cantidad de registros por página |

**Curl**

```bash
curl "http://localhost:8082/api/v1/books?page=0&size=10"
```

**Response 200**

```json
{
  "content": [
    {
      "id": "64a1b2c3d4e5f6a7b8c9d0e1",
      "isbn": "978-0-13-468599-1",
      "title": "Clean Code",
      "author": { "name": "Robert", "lastName": "Martin" },
      "category": "SOFTWARE_ENGINEERING",
      "publisher": "Prentice Hall",
      "year": 2008,
      "edition": 1,
      "language": "ENGLISH",
      "synopsis": "A handbook of agile software craftsmanship.",
      "pages": 431,
      "pdfUrl": "https://storage.googleapis.com/biolibrary/pdfs/clean-code.pdf",
      "coverImageUrl": "https://storage.googleapis.com/biolibrary/covers/clean-code.jpg",
      "license": { "maxConcurrentLoans": 5, "activeLoanCount": 0 },
      "active": true
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 10,
  "totalPages": 1
}
```

---

### GET `/{id}`

Retorna el detalle de un libro por su ID de MongoDB.

**Path Parameters**

| Parámetro | Tipo | Descripción |
|---|---|---|
| `id` | String | ID del documento MongoDB |

**Curl**

```bash
curl "http://localhost:8082/api/v1/books/64a1b2c3d4e5f6a7b8c9d0e1"
```

**Response 200** — mismo esquema que el objeto dentro de `content` en el endpoint anterior.

**Response 404**

```json
{
  "message": "Book with id 64a1b2c3d4e5f6a7b8c9d0e1 was not found.",
  "status": "Not Found",
  "timestamp": "2026-05-11T10:30:00",
  "statusCode": 404
}
```

---

### PATCH `/{id}/loan-count`

Actualiza el conteo de préstamos activos de un libro. Llamado internamente por el micro `loans` al crear o devolver un préstamo.

**Path Parameters**

| Parámetro | Tipo | Descripción |
|---|---|---|
| `id` | String | ID del libro |

**Request Body**

```json
{ "action": "INCREMENT" }
```

| Campo | Tipo | Requerido | Valores válidos |
|---|---|---|---|
| `action` | Enum | Sí | `INCREMENT`, `DECREMENT` |

**Curl — nuevo préstamo**

```bash
curl -X PATCH "http://localhost:8082/api/v1/books/64a1b2c3d4e5f6a7b8c9d0e1/loan-count" \
  -H "Content-Type: application/json" \
  -d '{"action": "INCREMENT"}'
```

**Curl — devolución**

```bash
curl -X PATCH "http://localhost:8082/api/v1/books/64a1b2c3d4e5f6a7b8c9d0e1/loan-count" \
  -H "Content-Type: application/json" \
  -d '{"action": "DECREMENT"}'
```

**Response 200** — mismo esquema `BookResponse` con `license.activeLoanCount` actualizado.

**Response 404 — libro no encontrado**

```json
{
  "message": "Book with id 64a1b2c3d4e5f6a7b8c9d0e1 was not found.",
  "status": "Not Found",
  "timestamp": "2026-05-11T10:30:00",
  "statusCode": 404
}
```

**Response 422 — límite de licencias alcanzado (INCREMENT)**

```json
{
  "message": "Book has reached its maximum concurrent loan limit of 5.",
  "status": "Unprocessable Entity",
  "timestamp": "2026-05-11T10:30:00",
  "statusCode": 422
}
```

**Response 422 — conteo ya en cero (DECREMENT)**

```json
{
  "message": "Book active loan count is already zero.",
  "status": "Unprocessable Entity",
  "timestamp": "2026-05-11T10:30:00",
  "statusCode": 422
}
```

**Response 400 — action faltante**

```json
{
  "message": "action: Action is required (INCREMENT or DECREMENT)",
  "status": "Bad Request",
  "timestamp": "2026-05-11T10:30:00",
  "statusCode": 400
}
```

---

## Enums

### `Category`

`SOFTWARE_ENGINEERING`, `MATHEMATICS`, `PHYSICS`, `CHEMISTRY`, `BIOLOGY`, `HISTORY`, `LITERATURE`, `ECONOMICS`, `MEDICINE`, `PHILOSOPHY`, `LAW`, `OTHER`

### `Language`

`SPANISH`, `ENGLISH`, `FRENCH`, `GERMAN`, `PORTUGUESE`, `ITALIAN`, `OTHER`

---

## Variables de entorno

| Variable | Default | Descripción |
|---|---|---|
| `SERVER_PORT` | `8082` | Puerto HTTP del servidor |
| `MONGODB_URI` | `mongodb://localhost:27017/biolibrary` | URI de conexión a MongoDB |

---

## Datos iniciales

Al iniciar, si la colección `books` está vacía, el `DataInitializer` carga automáticamente **10 libros** de referencia con `maxConcurrentLoans: 5` y `activeLoanCount: 0`.

---

## Correr localmente

**Requisitos:** MongoDB corriendo y accesible.

```bash
cd bio-library/catalog
./gradlew bootRun

# Con variables personalizadas
SERVER_PORT=8083 MONGODB_URI=mongodb://my-mongo:27017/biolibrary ./gradlew bootRun
```

---

## Swagger / API Docs

| Recurso | URL |
|---|---|
| Swagger UI | `http://localhost:8082/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8082/v3/api-docs` |
