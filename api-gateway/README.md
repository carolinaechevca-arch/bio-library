# API Gateway

Punto de entrada único del sistema Bio Library. Centraliza seguridad JWT, CORS y enrutamiento hacia los microservicios.

## Puerto

`8090`

## Responsabilidades

| Responsabilidad | Detalle |
|-----------------|---------|
| **Enrutamiento** | Redirige cada ruta al microservicio correspondiente |
| **Autenticación JWT** | Valida el token en cada petición protegida antes de enrutar |
| **Headers de contexto** | Inyecta `X-User-Id`, `X-User-Email`, `X-User-Role`, `X-User-Gpa` en la petición downstream |
| **CORS centralizado** | Única configuración CORS para todos los orígenes y métodos |
| **Bloqueo de rutas internas** | Las rutas `/api/v1/internal/**` devuelven 403 desde el gateway |
| **Logging de tráfico** | Loguea método, path, status y tiempo de respuesta de cada petición |

## Rutas expuestas

| Ruta del Gateway              | Microservicio destino | Puerto |
|-------------------------------|-----------------------|--------|
| `/api/v1/auth/**`             | user                  | 8080   |
| `/api/v1/students/**`         | user                  | 8080   |
| `/api/v1/books/**`            | catalog               | 8082   |
| `/api/v1/loans/**`            | loans                 | 8083   |

### Rutas públicas (sin JWT)

- `POST /api/v1/auth/login`
- `POST /api/v1/students/create`
- `GET  /actuator/health`

### Rutas bloqueadas (nunca expuestas al exterior)

- `/api/v1/internal/**` → `403 Forbidden` (usadas solo para comunicación interna entre servicios)
- Microservicios `notification` y `university-mock` (no tienen rutas expuestas por el gateway)

## Estructura

```
api-gateway/
└── src/main/java/com/bio_library/api_gateway/
    ├── domain/
    │   └── constants/
    │       └── GatewayConstants        # Rutas públicas, rutas bloqueadas, nombres de headers
    └── infrastructure/
        ├── security/
        │   └── JwtProvider             # Parseo y validación del JWT (JJWT)
        ├── filter/
        │   ├── RequestLoggingFilter    # GlobalFilter orden -2: loguea request y response
        │   └── JwtAuthenticationFilter # GlobalFilter orden -1: valida JWT, inyecta headers
        └── configuration/
            ├── CorsGatewayConfiguration  # CorsWebFilter global (todos los orígenes)
            └── RouteConfiguration        # RouteLocator con las 4 rutas expuestas
```

## Orden de los filtros

```
Request entrante
   │
   ▼  Orden -2
RequestLoggingFilter  →  loguea método + path
   │
   ▼  Orden -1
JwtAuthenticationFilter
   ├─ BLOCKED PATH?  →  403 Forbidden
   ├─ PUBLIC PATH?   →  pasa directo
   ├─ Sin token?     →  401 Unauthorized
   ├─ Token inválido?→  401 Unauthorized
   └─ Token válido?  →  agrega X-User-* headers → upstream
   │
   ▼
RouteLocator  →  reenvía al microservicio destino
   │
   ▼  (post-chain)
RequestLoggingFilter  →  loguea status + tiempo de respuesta
```

## Headers inyectados al downstream

Cuando el JWT es válido, el gateway añade a la petición saliente:

| Header          | Contenido                |
|-----------------|--------------------------|
| `X-User-Id`     | ID del usuario (Long)    |
| `X-User-Email`  | Email del usuario        |
| `X-User-Role`   | Rol (`STUDENT`, `ADMIN`) |
| `X-User-Gpa`    | Promedio académico       |

## Variables de entorno

| Variable           | Default                  | Descripción          |
|--------------------|--------------------------|----------------------|
| `SERVER_PORT`      | `8090`                   | Puerto del gateway   |
| `JWT_SECRET`       | (secreto de desarrollo)  | Clave HMAC del JWT   |
| `USER_URL`         | `http://localhost:8080`  | URL del user service |
| `CATALOG_URL`      | `http://localhost:8082`  | URL del catalog      |
| `LOANS_URL`        | `http://localhost:8083`  | URL del loans        |

## Respuestas de error del gateway

```json
{ "status": 401, "message": "Missing or invalid Authorization header" }
{ "status": 401, "message": "Token expired or invalid" }
{ "status": 403, "message": "Access denied" }
```
