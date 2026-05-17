# Catalog — Bio Library

## Objetivo

`catalog` es el microservicio que gestiona el **catálogo de libros digitales** de Bio Library.

Sus responsabilidades son:

- CRUD de libros (crear, listar, buscar, eliminar).
- Paginación y filtro por categoría.
- Controlar las **licencias disponibles** por libro, garantizando que ningún título supere su límite de préstamos concurrentes.

> **Regla de negocio central:** cada libro tiene un número de `totalLicenses`. El campo `availableLicenses` indica cuántas licencias quedan libres. Cuando llega a 0 el libro no puede prestarse hasta que se devuelva alguno.

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

**No tiene seguridad propia** — sus endpoints son llamados directamente por `loans` vía Feign para actualizar licencias, y pueden ser consumidos por el frontend para listar y gestionar libros.

---

## Modelo de libro

```json
{
  "id": "64a1b2c3d4e5f6a7b8c9d0e1",
  "isbn": "9780132350884",
  "title": "Clean Code: A Handbook of Agile Software Craftsmanship",
  "author": "Robert C. Martin",
  "category": "SOFTWARE_ENGINEERING",
  "description": "Manual de buenas prácticas para escribir código limpio y mantenible.",
  "pdfUrl": "https://...",
  "imagenUrl": "https://...",
  "totalLicenses": 5,
  "availableLicenses": 3
}
```

| Campo | Descripción |
|---|---|
| `totalLicenses` | Total de licencias concurrentes permitidas para el título |
| `availableLicenses` | Licencias disponibles en este momento (totalLicenses − préstamos activos) |

---

## Endpoints

**Base URL:** `http://localhost:8082/api/v1/books`

---

### POST `/`

Crea un nuevo libro. `availableLicenses` se establece automáticamente igual a `totalLicenses`.

**Request Body**

```json
{
  "isbn": "9780132350884",
  "title": "Clean Code: A Handbook of Agile Software Craftsmanship",
  "author": "Robert C. Martin",
  "category": "SOFTWARE_ENGINEERING",
  "description": "Manual de buenas prácticas para escribir código limpio y mantenible.",
  "pdfUrl": "https://www.example.com/clean-code.pdf",
  "imagenUrl": "https://www.example.com/clean-code.jpg",
  "totalLicenses": 5
}
```

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| `isbn` | String | Sí | ISBN del libro (debe ser único) |
| `title` | String | Sí | Título |
| `author` | String | Sí | Nombre completo del autor |
| `category` | Enum | Sí | Ver sección Enums |
| `description` | String | Sí | Descripción del libro |
| `pdfUrl` | String | Sí | URL del PDF |
| `imagenUrl` | String | No | URL de la portada |
| `totalLicenses` | int | Sí | Mínimo 1 |

**Response 201**

```json
{
  "id": "64a1b2c3d4e5f6a7b8c9d0e1",
  "isbn": "9780132350884",
  "title": "Clean Code: A Handbook of Agile Software Craftsmanship",
  "author": "Robert C. Martin",
  "category": "SOFTWARE_ENGINEERING",
  "description": "Manual de buenas prácticas para escribir código limpio y mantenible.",
  "pdfUrl": "https://www.example.com/clean-code.pdf",
  "imagenUrl": "https://www.example.com/clean-code.jpg",
  "totalLicenses": 5,
  "availableLicenses": 5
}
```

**Response 409 — ISBN duplicado**

```json
{
  "message": "A book with isbn '9780132350884' already exists.",
  "status": "Conflict",
  "timestamp": "2026-05-16T22:00:00",
  "statusCode": 409
}
```

---

### GET `/`

Retorna el catálogo paginado. Acepta filtro opcional por categoría.

**Query Parameters**

| Parámetro | Tipo | Default | Descripción |
|---|---|---|---|
| `category` | Enum | — | Filtra por categoría (opcional) |
| `page` | int | `0` | Número de página (base 0) |
| `size` | int | `10` | Registros por página |

**Curl — todos los libros**

```bash
curl "http://localhost:8082/api/v1/books?page=0&size=10"
```

**Curl — filtrado por categoría**

```bash
curl "http://localhost:8082/api/v1/books?category=MATHEMATICS&page=0&size=10"
```

**Response 200**

```json
{
  "content": [ { "id": "...", "isbn": "...", "title": "...", "..." : "..." } ],
  "page": 0,
  "size": 10,
  "totalElements": 17,
  "totalPages": 2
}
```

---

### GET `/categories`

Retorna la lista de todas las categorías disponibles del enum.

**Curl**

```bash
curl "http://localhost:8082/api/v1/books/categories"
```

**Response 200**

```json
[
  "SOFTWARE_ENGINEERING",
  "COMPUTER_SCIENCE",
  "MATHEMATICS",
  "PHYSICS",
  "CHEMISTRY",
  "BIOLOGY",
  "HISTORY",
  "LITERATURE",
  "ECONOMICS",
  "MEDICINE",
  "PHILOSOPHY",
  "LAW",
  "OTHER"
]
```

---

### GET `/isbn/{isbn}`

Busca un libro por su ISBN.

**Curl**

```bash
curl "http://localhost:8082/api/v1/books/isbn/9780132350884"
```

**Response 200** — esquema `BookResponse` completo.

**Response 404**

```json
{
  "message": "Book with isbn '9780132350884' was not found.",
  "status": "Not Found",
  "timestamp": "2026-05-16T22:00:00",
  "statusCode": 404
}
```

---

### GET `/{id}`

Busca un libro por su ID de MongoDB.

**Curl**

```bash
curl "http://localhost:8082/api/v1/books/64a1b2c3d4e5f6a7b8c9d0e1"
```

**Response 200** — esquema `BookResponse` completo.

**Response 404**

```json
{
  "message": "Book with id '64a1b2c3d4e5f6a7b8c9d0e1' was not found.",
  "status": "Not Found",
  "timestamp": "2026-05-16T22:00:00",
  "statusCode": 404
}
```

---

### DELETE `/{id}`

Elimina un libro por su ID.

**Curl**

```bash
curl -X DELETE "http://localhost:8082/api/v1/books/64a1b2c3d4e5f6a7b8c9d0e1"
```

**Response 204** — sin cuerpo.

**Response 404** — mismo esquema que los demás 404.

---

### PATCH `/{id}/loan-count`

Actualiza las licencias disponibles. Llamado internamente por el micro `loans` al crear o devolver un préstamo.

| Acción | Efecto |
|---|---|
| `INCREMENT` | `availableLicenses - 1` (nuevo préstamo) |
| `DECREMENT` | `availableLicenses + 1` (devolución) |

**Request Body**

```json
{ "action": "INCREMENT" }
```

**Curl — nuevo préstamo**

```bash
curl -X PATCH "http://localhost:8082/api/v1/books/64a1b2c3d4e5f6a7b8c9d0e1/loan-count" \
  -H "Content-Type: application/json" \
  -d '{"action": "INCREMENT"}'
```

**Response 200** — `BookResponse` con `availableLicenses` actualizado.

**Response 422 — sin licencias disponibles**

```json
{
  "message": "Book has no available licenses (total: 5).",
  "status": "Unprocessable Entity",
  "timestamp": "2026-05-16T22:00:00",
  "statusCode": 422
}
```

---

## Enums

### `Category`

`SOFTWARE_ENGINEERING`, `COMPUTER_SCIENCE`, `MATHEMATICS`, `PHYSICS`, `CHEMISTRY`, `BIOLOGY`, `HISTORY`, `LITERATURE`, `ECONOMICS`, `MEDICINE`, `PHILOSOPHY`, `LAW`, `OTHER`

---

## Variables de entorno

| Variable | Default | Descripción |
|---|---|---|
| `SERVER_PORT` | `8082` | Puerto HTTP del servidor |
| `MONGODB_URI` | `mongodb://localhost:27017/biolibrary` | URI de conexión a MongoDB |

---

## Datos iniciales (seed)

Al primer arranque del contenedor MongoDB (volumen vacío), el script `catalog/src/main/resources/data/seed.js` se ejecuta automáticamente vía `mongo-init/01-seed.sh` e inserta **18 libros** de referencia con `totalLicenses: 5` y `availableLicenses: 5`.

Para forzar un re-seed:

```bash
docker compose down
docker volume rm bio-library_mongo_data
docker compose up --build
```

---

## Correr localmente

**Requisito:** MongoDB corriendo y accesible.

```bash
cd bio-library/catalog
./gradlew bootRun

# Con variables personalizadas
SERVER_PORT=8082 MONGODB_URI=mongodb://my-mongo:27017/biolibrary ./gradlew bootRun
```

---

## Swagger / API Docs

| Recurso | URL |
|---|---|
| Swagger UI | `http://localhost:8082/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8082/v3/api-docs` |
