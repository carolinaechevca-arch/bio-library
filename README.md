# Bio Library

Sistema de gestión de biblioteca universitaria basado en microservicios con arquitectura hexagonal.

## Servicios

| Servicio | Puerto | Descripción |
|---|---|---|
| api-gateway | 8090 | Punto de entrada único — enruta y valida JWT |
| user | 8080 | Autenticación y gestión de estudiantes |
| university-mock | 8081 | Mock del sistema universitario |
| catalog | 8082 | Catálogo de libros (MongoDB) |
| loans | 8083 | Gestión de préstamos |
| notification | 8084 | Notificaciones de eventos de préstamo |

**Infraestructura incluida:** PostgreSQL · MongoDB · RabbitMQ

---

## Requisitos

- Docker >= 24
- Docker Compose >= 2.20

---

## Comandos

### Levantar todo (primera vez o tras cambios de código)

```bash
docker compose up --build
```

### Levantar en segundo plano

```bash
docker compose up --build -d
```

### Levantar solo la infraestructura (BD + RabbitMQ)

```bash
docker compose up postgres mongodb rabbitmq -d
```

### Ver logs de un servicio

```bash
docker compose logs -f api-gateway
docker compose logs -f loans
```

### Ver logs de todos los servicios

```bash
docker compose logs -f
```

### Apagar todo (conserva volúmenes)

```bash
docker compose down
```

### Apagar y borrar volúmenes (reset total de BD)

```bash
docker compose down -v
```

### Reconstruir un solo servicio

```bash
docker compose build loans
docker compose up -d loans
```

---

## URLs una vez levantado

| Recurso | URL |
|---|---|
| API Gateway (entrada principal) | http://localhost:8090 |
| RabbitMQ Management UI | http://localhost:15672 (guest/guest) |
| Swagger user | http://localhost:8080/swagger-ui.html |
| Swagger catalog | http://localhost:8082/swagger-ui.html |
| Swagger loans | http://localhost:8083/swagger-ui.html |
| Swagger university-mock | http://localhost:8081/swagger-ui.html |
| Health gateway | http://localhost:8090/actuator/health |

---

## Flujo de prueba

1. **Registrar estudiante**
   ```
   POST http://localhost:8090/api/v1/students/create
   ```
   Body (nombre y apellido se toman automáticamente del sistema universitario):
   ```json
   {
     "carnet": "2021-0001",
     "dni": "123456789",
     "email": "estudiante@universidad.edu",
     "password": "Password1!",
     "phoneNumber": "55551234",
     "university": "UNAH"
   }
   ```

2. **Login — obtener JWT**
   ```
   POST http://localhost:8090/api/v1/auth/login
   ```

3. **Usar el JWT en el header para las demás rutas**
   ```
   Authorization: Bearer <token>
   ```

4. **Ver libros disponibles**
   ```
   GET http://localhost:8090/api/v1/books
   ```

5. **Crear préstamo**
   ```
   POST http://localhost:8090/api/v1/loans
   ```

---

## Arquitectura

```
Cliente
  │
  ▼
api-gateway :8090  (JWT auth + routing)
  ├──▶ user :8080  ──▶ university-mock :8081
  ├──▶ catalog :8082  (MongoDB)
  └──▶ loans :8083
         ├──▶ catalog :8082
         ├──▶ user :8080
         └──▶ notification :8084

Infraestructura:
  PostgreSQL :5432  (schemas: users · loans · university)
  MongoDB    :27017 (db: biolibrary)
  RabbitMQ   :5672  / UI :15672
```

---

## Seed de MongoDB

Al primer arranque, MongoDB carga automáticamente `mongo-init/seed.js` con 6 libros de ejemplo en la colección `books`.

Para verificar:
```bash
docker exec bio-mongodb mongosh biolibrary --eval "db.books.find().pretty()"
```
